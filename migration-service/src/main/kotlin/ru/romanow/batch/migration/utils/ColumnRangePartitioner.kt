package ru.romanow.batch.migration.utils

import org.slf4j.LoggerFactory
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.item.ExecutionContext
import org.springframework.jdbc.core.JdbcTemplate

open class ColumnRangePartitioner(
    private val column: String,
    private val table: String,
    private val condition: String,
    private val jdbcTemplate: JdbcTemplate
) : Partitioner {
    private val logger = LoggerFactory.getLogger(ColumnRangePartitioner::class.java)

    override fun partition(gridSize: Int): MutableMap<String, ExecutionContext> {
        val result: MutableMap<String, ExecutionContext> = HashMap()
        val size = jdbcTemplate
            .queryForObject("SELECT COUNT($column) FROM $table WHERE $condition", Int::class.java)!!

        if (size == 0) {
            val value = ExecutionContext()
            result["partition" + 0] = value
            value.putInt("minValue", 0)
            value.putInt("maxValue", 0)
            return result
        }
        val min = jdbcTemplate
            .queryForObject("SELECT MIN($column) FROM $table where $condition", Int::class.java)!!

        val max = jdbcTemplate
            .queryForObject("SELECT MAX($column) FROM $table where $condition", Int::class.java)!!

        val targetSize = (max - min) / gridSize + 1

        var number = 0
        var start = min
        var end = start + targetSize - 1

        while (start <= max) {
            val value = ExecutionContext()
            result["partition$number"] = value
            if (end >= max) {
                end = max
            }
            value.putInt("minValue", start)
            value.putInt("maxValue", end)
            start += targetSize
            end += targetSize
            number++

            logger.info("Add partition from $start to $end")
        }
        return result
    }
}