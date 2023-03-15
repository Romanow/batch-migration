package ru.romanow.batch.migration.service

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

@Service
class MigrationService(
    private val migration: Job,
    private val jobLauncher: JobLauncher
) {
    private val logger = LoggerFactory.getLogger(MigrationService::class.java)

    fun batch(solveId: String) {
        logger.info("Request to start batch processing")
        val params = JobParametersBuilder()
            .addString("solveId", solveId)
            .addString("key", ISO_LOCAL_DATE_TIME.format(now()))
            .toJobParameters()

        val execution = jobLauncher.run(migration, params)

        logger.info("Started batch process ${execution.jobId}: ${execution.status} (start: ${execution.startTime})")
    }
}
