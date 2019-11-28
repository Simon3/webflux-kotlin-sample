package org.taktik.spring.security.kotlin

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.ReactorContext
import kotlinx.coroutines.reactor.asCoroutineContext
import kotlinx.coroutines.reactor.asFlux
import org.springframework.security.core.context.SecurityContext
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.coroutines.CoroutineContext

fun injectReactorContext(flow: Flow<Map<String, String>>): Flux<Map<String, String>> {
    return Mono.subscriberContext().flatMapMany { reactorCtx ->
        flow.flowOn(reactorCtx.asCoroutineContext()).asFlux()
    }
}


suspend fun securityContext(coroutineContext: CoroutineContext) =
    coroutineContext[ReactorContext]?.context?.get<Mono<SecurityContext>>(SecurityContext::class.java)?.asFlow()?.single()