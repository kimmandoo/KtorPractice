package com.example.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        // ContentNegotiation이 설치되어있음
        json() // json을 반환하겠다는 의미
    }
    routing {
        get("/json/kotlinx-serialization") {
            // json body가 응답으로 온다.
                call.respond(mapOf("hello" to "world"))
            }
    }
}
