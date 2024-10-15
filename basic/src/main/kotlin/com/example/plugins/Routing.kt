package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") { // root에 들어갔을 때 GET메서드로 응답으로 텍스트를 주는 것
            call.respondText("Hello World!")
        }
    }
}
