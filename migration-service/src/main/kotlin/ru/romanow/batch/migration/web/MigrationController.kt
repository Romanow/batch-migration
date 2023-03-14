package ru.romanow.batch.migration.web

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.romanow.batch.migration.service.MigrationService

@RestController
@RequestMapping("/api/v1/migration")
class MigrationController(
    private val migrationService: MigrationService,
) {

    @GetMapping("/batch/{solveId}")
    @ResponseStatus(HttpStatus.OK)
    fun batchMigration(@PathVariable solveId: String) {
        migrationService.batch(solveId)
    }

    @GetMapping("/database/{solveId}")
    @ResponseStatus(HttpStatus.OK)
    fun databaseMigration(@PathVariable solveId: String) {
    }
}
