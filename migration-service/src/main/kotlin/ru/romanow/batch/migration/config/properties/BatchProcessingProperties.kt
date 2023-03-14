package ru.romanow.batch.migration.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "batch-processing")
data class BatchProcessingProperties(
    val chunkSize: Int,
    val threads: Int,
    val mainSchema: String,
    val stagedSchema: String
)