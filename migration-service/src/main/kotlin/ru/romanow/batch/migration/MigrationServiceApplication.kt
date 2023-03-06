package ru.romanow.batch.migration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import ru.romanow.migration.domain.AggregationResultEntity

@EntityScan(basePackageClasses = [AggregationResultEntity::class])
@SpringBootApplication
class MigrationServiceApplication

fun main(args: Array<String>) {
    runApplication<MigrationServiceApplication>(*args)
}