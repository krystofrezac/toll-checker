package cz.krystofrezac.tollchecker

import cz.krystofrezac.tollchecker.plugins.configureRouting
import cz.krystofrezac.tollchecker.services.CheckServiceImpl
import cz.krystofrezac.tollchecker.services.EdalniceClientImpl
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain
        .main(args)
}

fun Application.module() {
    val edalniceClient = EdalniceClientImpl()
    val checkService = CheckServiceImpl(edalniceClient)

    configureRouting(checkService)
}
