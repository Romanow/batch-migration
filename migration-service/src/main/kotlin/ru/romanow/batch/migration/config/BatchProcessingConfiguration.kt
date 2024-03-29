package ru.romanow.batch.migration.config

import jakarta.persistence.EntityManagerFactory
import org.slf4j.LoggerFactory
import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.database.Order
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.PlatformTransactionManager
import ru.romanow.batch.migration.config.properties.BatchProcessingProperties
import ru.romanow.batch.migration.utils.ColumnRangePartitioner
import ru.romanow.migration.domain.AggregationResultEntity
import javax.sql.DataSource

@Configuration
@EnableBatchProcessing
@EnableConfigurationProperties(value = [BatchProcessingProperties::class])
class BatchProcessingConfiguration {
    private val logger = LoggerFactory.getLogger(BatchProcessingConfiguration::class.java)

    @Autowired
    private lateinit var properties: BatchProcessingProperties

    @Autowired
    private lateinit var entityManagerFactory: EntityManagerFactory

    @Autowired
    private lateinit var dataSource: DataSource

    @Bean
    fun migration(
        jobRepository: JobRepository,
        migrationManager: Step,
        delete: TaskletStep
    ): Job {
        return JobBuilder(MIGRATION_NAME, jobRepository)
            .start(migrationManager)
            .next(delete)
            .build()
    }

    @Bean
    fun migrationManager(
        jobRepository: JobRepository,
        migrate: Step
    ): Step {
        return StepBuilder("$MIGRATION_NAME:manager", jobRepository)
            .partitioner(MIGRATION_NAME, partitioner(null))
            .taskExecutor(taskExecutor())
            .gridSize(properties.threads)
            .step(migrate)
            .build()
    }

    @Bean
    fun taskExecutor() = SimpleAsyncTaskExecutor()

    @Bean
    @StepScope
    fun partitioner(
        @Value("#{jobParameters['solveId']}") solveId: String?
    ): ColumnRangePartitioner {
        return ColumnRangePartitioner(
            column = "id",
            table = "${properties.stagedSchema}.aggregation_result",
            condition = "solve_id = $solveId",
            jdbcTemplate = JdbcTemplate(dataSource)
        )
    }

    @Bean
    fun migrate(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder(MIGRATION_NAME, jobRepository)
            .chunk<AggregationResultEntity, AggregationResultEntity>(properties.chunkSize, transactionManager)
            .reader(stageSchemaReader(null, null, null))
            .processor(processor())
            .writer(mainSchemaWriter())
            .build()
    }

    @Bean
    fun delete(jobRepository: JobRepository, transactionManager: PlatformTransactionManager): TaskletStep {
        return StepBuilder(DELETE_NAME, jobRepository)
            .tasklet(deleteTask(null), transactionManager)
            .build()
    }

    // ======================================
    // =============== reader ===============
    // ======================================
    @Bean
    @StepScope
    fun stageSchemaReader(
        @Value("#{jobParameters['solveId']}") solveId: String?,
        @Value("#{stepExecutionContext['minValue']}") minValue: Long?,
        @Value("#{stepExecutionContext['maxValue']}") maxValue: Long?
    ): JdbcPagingItemReader<AggregationResultEntity> {
        val provider = PostgresPagingQueryProvider()
        provider.setSelectClause("SELECT *")
        provider.setFromClause("FROM ${properties.stagedSchema}.aggregation_result")
        provider.setWhereClause("WHERE solve_id = :solveId AND id BETWEEN $minValue AND $maxValue")
        provider.sortKeys = mapOf("id" to Order.ASCENDING)

        logger.info("Reading from $minValue to $maxValue")

        return JdbcPagingItemReaderBuilder<AggregationResultEntity>()
            .dataSource(dataSource)
            .queryProvider(provider)
            .saveState(false)
            .pageSize(properties.chunkSize)
            .beanRowMapper(AggregationResultEntity::class.java)
            .parameterValues(mapOf("solveId" to solveId))
            .build()
    }

    // ======================================
    // ============= processor ==============
    // ======================================
    @Bean
    fun processor(): ItemProcessor<AggregationResultEntity, AggregationResultEntity> {
        return ItemProcessor<AggregationResultEntity, AggregationResultEntity> {
            it.id = null
            return@ItemProcessor it
        }
    }

    // ======================================
    // =============== writer ===============
    // ======================================
    @Bean
    fun mainSchemaWriter(): ItemWriter<AggregationResultEntity> {
        return JpaItemWriterBuilder<AggregationResultEntity>()
            .entityManagerFactory(entityManagerFactory)
            .build()
    }

    // ======================================
    // ================ task ================
    // ======================================
    @Bean
    @StepScope
    fun deleteTask(@Value("#{jobParameters['solveId']}") solveId: String?): Tasklet {
        return Tasklet { _, _ ->
            val jdbcTemplate = NamedParameterJdbcTemplate(dataSource)
            val deleted = jdbcTemplate.update(
                "DELETE FROM ${properties.stagedSchema}.aggregation_result WHERE solve_id = :solveId",
                mapOf("solveId" to solveId)
            )
            logger.info("Removed $deleted items from ${properties.stagedSchema}")
            return@Tasklet RepeatStatus.FINISHED
        }
    }


    companion object {
        const val MIGRATION_NAME = "migrate"
        const val DELETE_NAME = "delete"
    }
}
