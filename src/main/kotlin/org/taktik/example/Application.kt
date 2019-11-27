package org.taktik.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
