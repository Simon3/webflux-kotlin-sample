package org.taktik.example.controllers

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.ReactorContext
import kotlinx.coroutines.reactor.asFlux
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.context.Context
import java.util.function.Consumer
import kotlin.coroutines.coroutineContext

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

    @GetMapping("/suspend")
    suspend fun suspend(): Map<String,String> {
        delay(100L)
        return coroutineContext.get(ReactorContext.Key)?.context?.get<Mono<SecurityContext>>(SecurityContext::class.java)?.map { ctx ->
            mapOf("user" to ((ctx?.authentication?.principal as? User)?.username ?: "<NONE>"))
        }?.asFlow()?.single() ?: mapOf()
    }

    @GetMapping("/flow")
    fun flow(): Flow<Map<String,String>> = flow {
        val ctx = coroutineContext.get(ReactorContext.Key)?.context?.get<Mono<SecurityContext>>(SecurityContext::class.java)?.asFlow()?.single()
        emit(mapOf("user" to ((ctx?.authentication?.principal as? User)?.username ?: "<NONE>")))
    }

    @GetMapping("/flux")
    fun flux(): Flux<Map<String, String>> = Flux.fromIterable(listOf(mapOf("user" to "<NONE>")))

    @GetMapping("/fluxflow")
    fun fluxflow(): Flux<Map<String, String>> = Flux.create(Consumer {sink ->
        ReactiveSecurityContextHolder.getContext().map { ctx ->
            println("User is ${(ctx?.authentication?.principal as? User)?.username ?: "<NONE>"}")
            GlobalScope.launch {
                flow<Map<String,String>> {
                    emit(mapOf("user" to ((ctx?.authentication?.principal as? User)?.username ?: "<NONE>")))
                }.collect { sink.next(it) }
            }
        }
    })

}
