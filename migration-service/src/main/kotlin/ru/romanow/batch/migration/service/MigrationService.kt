package ru.romanow.batch.migration.service

import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.romanow.batch.migration.repository.AggregationResultRepository
import ru.romanow.migration.domain.AggregationResultEntity

@Service
class MigrationService(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val aggregationResultRepository: AggregationResultRepository,
) {

    @Transactional
    fun migrate(solveId: String) {
        val schema = "staged"
        val rowMapper = BeanPropertyRowMapper(AggregationResultEntity::class.java)
        val list = jdbcTemplate
            .query("SELECT * FROM $schema.aggregation_result WHERE solve_id = :solveId",
                mapOf("solveId" to solveId),
                rowMapper)
            .map { it.id = null; it }

        aggregationResultRepository.saveAll(list)

        jdbcTemplate.update("DELETE FROM $schema.aggregation_result WHERE solve_id = :solveId",
            mapOf("solveId" to solveId))
    }

}
