package com.example.plugins

import com.example.shared.CafeOrderStatus
import com.example.shared.dto.OrderDto
import com.example.shared.menuList
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        route("/api") { // 최상위 path
            get("/menus") {
                val list = menuList
                // 괄호 안의 input은 this라서 call은 this.call -> this는 pipeline context
                call.respond(list) // list를 응답해줘
            }

            post("/orders") {
                // 데이터를 받을 건데, 어떤 형식으로 받을건지
                val request: OrderDto.CreateRequest = call.receive<OrderDto.CreateRequest>()
                val selectedMenu = menuList.first { it.id == request.menuId } // 메뉴아이디랑 같은 거
                val order = OrderDto.DisplayResponse(
                    orderCode = "1",
                    menuName = selectedMenu.name,
                    customerName = "몰라",
                    price = selectedMenu.price,
                    status = CafeOrderStatus.READY,
                    orderedAt = LocalDateTime.now(),
                    id = 1
                )
                call.respond(order)
            }

            // 주문 상세조회
            get("/orders/{orderCode}"){ // PathVariable
                val orderCode = call.parameters["orderCode"]!!  // PathVariable 가져오기. 원래 기본이 nullable임
                val order = OrderDto.DisplayResponse(
                    orderCode = orderCode,
                    menuName = "라떼",
                    customerName = "몰라",
                    price = 2800,
                    status = CafeOrderStatus.READY,
                    orderedAt = LocalDateTime.now(),
                    id = 1
                )
                call.respond(order)
            }
        }
    }
}
