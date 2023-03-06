package ru.romanow.batch.migration.repository

import org.springframework.data.repository.CrudRepository
import ru.romanow.migration.domain.AggregationResultEntity

interface AggregationResultRepository : CrudRepository<AggregationResultEntity, Long>