package org.taktik.example.controllers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.reactive.asFlow
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class MyController {
    @GetMapping("/context")
    fun context(): Mono<SecurityContext> {
        return ReactiveSecurityContextHolder.getContext()
    }

    @GetMapping("/ko")
    fun ko(): Flow<SecurityContext> {
        return ReactiveSecurityContextHolder.getContext().flux().asFlow()
    }

    @GetMapping("/ok")
    suspend fun ok(): SecurityContext {
        return ReactiveSecurityContextHolder.getContext().flux().asFlow().single()
    }
}
