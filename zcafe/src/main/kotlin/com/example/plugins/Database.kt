package com.example.plugins

import com.example.domain.CafeMenuTable
import com.example.domain.CafeOrderTable
import com.example.domain.CafeUserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.h2.tools.Server
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

fun Application.configureDatabase() {
    // configure** 는 컨벤션
    configureH2()
    connectDatabase()
    initData()
}


fun Application.configureH2() {
    // h2 DB를 실행하는 함수
    // JDBC Driver같은 형태 - 인메모리(애플리케이션 상에서 접근)
    val h2Server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092")
    monitor.subscribe(ApplicationStarted) { application ->
        application.environment.log.info("H2 server started. ${h2Server.url}")
    }

    monitor.subscribe(ApplicationStopped) { application ->
        h2Server.stop()
        application.environment.log.info("H2 server stopped. ${h2Server.url}")
    }
}

private fun connectDatabase() {
    val config =
        HikariConfig().apply {
            jdbcUrl = "jdbc:h2:mem:cafedb"
            driverClassName = "org.h2.Driver"
            validate()
        }

    val dataSource: DataSource = HikariDataSource(config)
    Database.connect(dataSource)
}

private fun initData() {
    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(
            CafeMenuTable,
            CafeUserTable,
            CafeOrderTable
        )
    }
}