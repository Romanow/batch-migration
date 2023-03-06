package ru.romanow.batch.migration

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import ru.romanow.batch.migration.config.DatabaseTestConfiguration

@SpringBootTest(classes = [DatabaseTestConfiguration::class])
internal class MigrationServiceApplicationTest {

    @Test
    fun runApp() {
    }
}