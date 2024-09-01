package cz.krystofrezac.tollchecker.plugins

import cz.krystofrezac.tollchecker.services.CheckService
import cz.krystofrezac.tollchecker.views.homeView
import cz.krystofrezac.tollchecker.views.resultView
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting(checkService: CheckService) {
    routing {
        get("/") {
            call.respondHtml {
                homeView()
            }
        }

        get("/check") {
            val licensePlates =
                call.parameters
                    .get("licensePlates")
                    ?.let { row ->
                        row
                            .split("\n")
                            .map { it.trim() }
                            .filter { it != "" }
                    }

            if (licensePlates == null) {
                return@get call.respond(HttpStatusCode.BadRequest)
            }

            val checkResult = checkService.check(licensePlates)
            call.respondHtml {
                resultView(checkResult)
            }
        }
    }
}
