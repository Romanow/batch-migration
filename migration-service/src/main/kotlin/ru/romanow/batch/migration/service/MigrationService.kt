package ru.romanow.batch.migration.service

import org.awaitility.Awaitility.await
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class MigrationService(private val migration: Job, private val jobLauncher: JobLauncher) {

    fun migrate(solveId: String) {
        val params = JobParametersBuilder()
            .addString("solveId", solveId)
            .addString("key", UUID.randomUUID().toString())
            .toJobParameters()
        val execution = jobLauncher.run(migration, params)
        await()
            .atMost(5, TimeUnit.MINUTES)
            .until { execution.exitStatus !in listOf(ExitStatus.FAILED, ExitStatus.COMPLETED) }
    }
}
