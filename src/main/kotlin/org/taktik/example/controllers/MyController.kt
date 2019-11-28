package org.taktik.example.controllers

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.taktik.spring.security.kotlin.injectReactorContext
import org.taktik.spring.security.kotlin.securityContext
import kotlin.coroutines.coroutineContext

@RestController
class MyController {
    @ExperimentalCoroutinesApi
    @GetMapping("/suspend")
    suspend fun suspend(): Map<String, String> {
        val ctx = securityContext(coroutineContext)
        return mapOf("user" to ((ctx?.authentication?.principal as? User)?.username ?: "<NONE>"))
    }

    @ExperimentalCoroutinesApi
    @GetMapping("/flow")
    fun flow() = injectReactorContext(flow {
        val ctx = securityContext(coroutineContext)
        emit(mapOf("user" to ((ctx?.authentication?.principal as? User)?.username ?: "<NONE>")))
    })

    @ExperimentalCoroutinesApi
    @GetMapping("/cascadeflow")
    fun cascadeflow() = injectReactorContext(flow {
        flow {
            val ctx = securityContext(coroutineContext)
            emit(mapOf("user" to ((ctx?.authentication?.principal as? User)?.username ?: "<NONE>")))
        }.collect { emit(it)}
    })

}
