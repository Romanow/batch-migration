package ru.romanow.batch.migration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DataLoaderScript

fun main(args: Array<String>) {
    runApplication<DataLoaderScript>(*args)
}