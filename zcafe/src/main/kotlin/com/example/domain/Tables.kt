package com.example.domain

import com.example.shared.CafeMenuCategory
import com.example.shared.CafeOrderStatus
import com.example.shared.CafeUserRole
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

// 테이블들은 싱글톤 객체인 object로 만들어져있다.
// 예약어와 테이블 명이 겹치면 오류가 발생할 수 있다.
object CafeMenuTable : LongIdTable(name = "cafe_menu") {
    val name = varchar("menu_name", length = 50)
    val price = integer("price")
    // enum을 컬럼값으로 쓴다.
    // enum class CafeMenuCategory { COFFEE, NONCOFFEE, DESSERT, BAKERY }
    // 그게 아니면 enumeration<>으로 해서 순서를 저장시키는데, 가독성 및 유지보수성을 좋게 하기 위해서 Enum을 써야된다.
    val category = enumerationByName("category", 10, CafeMenuCategory::class)
    val image = text("image")
}

object CafeUserTable : LongIdTable(name = "cafe_user") {
    val nickname = varchar("nickname", length = 50)
    val password = varchar("password", length = 100)
    // 컬럼에 값이 여러개 들어갈 수 있도록한것 -> 근데 type safe하게 바뀐 것 같다.
    val roles = enumList("roles", CafeUserRole::class.java, 20)
}

object CafeOrderTable : LongIdTable(name = "cafe_order") {
    val orderCode = varchar("order_code", length = 50)
    // cafe user table의 pk를 참조하고 있는 형태
    val cafeUserId = reference("cafe_user_id", CafeUserTable)
    val cafeMenuId = reference("cafe_menu_id", CafeMenuTable)
    // 메뉴의 가격이 바뀔 수도 있으니까 스냅샷 용도로
    val price = integer("price")
    val status = enumerationByName("status", 10, CafeOrderStatus::class)
    val orderedAt = datetime("ordered_at")
}