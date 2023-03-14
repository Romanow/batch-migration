package ru.romanow.batch.migration.service

import org.awaitility.Awaitility.await
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.util.concurrent.TimeUnit

@Service
class MigrationService(
    private val migration: Job,
    private val jobLauncher: JobLauncher
) {

    fun batch(solveId: String) {
        val params = JobParametersBuilder()
            .addString("solveId", solveId)
            .addString("key", ISO_LOCAL_DATE_TIME.format(now()))
            .toJobParameters()

        val execution = jobLauncher.run(migration, params)

        await()
            .atMost(5, TimeUnit.MINUTES)
            .until { !execution.isRunning }
    }
}
