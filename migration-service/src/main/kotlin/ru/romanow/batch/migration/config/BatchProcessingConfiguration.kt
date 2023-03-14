package ru.romanow.batch.migration.config

import org.slf4j.LoggerFactory
import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.database.Order
import org.springframework.batch.item.database.PagingQueryProvider
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import ru.romanow.batch.migration.repository.AggregationResultRepository
import ru.romanow.migration.domain.AggregationResultEntity
import javax.sql.DataSource


@Configuration
@EnableBatchProcessing
class BatchProcessingConfiguration {
    private var logger = LoggerFactory.getLogger("batch-processing-$NAME")

    @Bean
    fun migration(jobRepository: JobRepository, migrate: Step): Job {
        return JobBuilder(NAME, jobRepository)
            .start(migrate)
            .build()
    }

    @Bean
    fun migrate(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
        listener: StepExecutionListener,
        stageSchemaReader: ItemReader<AggregationResultEntity>,
        mainSchemaWriter: ItemWriter<AggregationResultEntity>
    ): Step {
        return StepBuilder(NAME, jobRepository)
            .chunk<AggregationResultEntity, AggregationResultEntity>(CHUNK_SIZE, transactionManager)
            .listener(listener)
            .reader(stageSchemaReader)
            .writer(mainSchemaWriter)
            .build()
    }

    @Bean
    fun listener(): StepExecutionListener {
        return object : StepExecutionListener {
            override fun beforeStep(execution: StepExecution) {
                logger.info("Processing $execution")
            }
        }
    }

    @Bean
    @StepScope
    fun stageSchemaReader(
        dataSource: DataSource,
        queryProvider: PagingQueryProvider,
        @Value("#{jobParameters['solveId']}") solveId: String
    ): JdbcPagingItemReader<AggregationResultEntity> {
        return JdbcPagingItemReaderBuilder<AggregationResultEntity>()
            .name("stage-schema-reader")
            .dataSource(dataSource)
            .queryProvider(queryProvider)
            .saveState(false)
            .pageSize(CHUNK_SIZE)
            .beanRowMapper(AggregationResultEntity::class.java)
            .parameterValues(mapOf("solveId" to solveId))
            .build()
    }

    @Bean
    fun queryProvider(): PagingQueryProvider {
        val provider = PostgresPagingQueryProvider()
        provider.setSelectClause("SELECT *")
        provider.setFromClause("FROM $STAGED_SCHEMA.aggregation_result")
        provider.setWhereClause("WHERE solve_id = :solveId")
        provider.sortKeys = mapOf("id" to Order.ASCENDING)
        return provider
    }

    @Bean
    fun mainSchemaWriter(aggregationResultRepository: AggregationResultRepository): ItemWriter<AggregationResultEntity> {
        return ItemWriter<AggregationResultEntity> { aggregationResultRepository.saveAll(it.items) }
    }

    companion object {
        const val NAME = "migration"
        const val CHUNK_SIZE = 5000
        const val MAIN_SCHEMA = "public"
        const val STAGED_SCHEMA = "staged"
    }
}
