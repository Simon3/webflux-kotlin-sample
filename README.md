# webflux-kotlin-sample

This is a minimal project using Spring Boot 2.2 and WebFlux, and written in Kotlin. 
It showcases a problem that can be encountered when switching from a Project Reactor stream (e.g. a Mono or a Flux) to a Kotlin Flow. 

Since Spring Boot is storing the Security Context in the reactive chain returned by the Controller, and I'm transforming this chain into another one before returning it, the context is unfortunately lost. 

# Usage
First, get the password from the logs. Then run one of the three endpoints, e.g. 

`curl -u user:$PASSWORD localhost:8080/context`
