package cz.krystofrezac.tollchecker.services

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class EdalniceClientImpl : EdalniceClient {
    private val logger = KotlinLogging.logger {}

    private val client =
        HttpClient(Java) {
            install(Resources)
            install(ContentNegotiation) {
                json(
                    Json {
                        // TODO: remove after oauth refactor
                        ignoreUnknownKeys = true
                    },
                )
            }
            expectSuccess = true
        }

    override suspend fun getIsTollValid(licensePlate: String): Boolean {
        // TODO: auth plugin?
        val response =
            client.submitForm(
                url = "https://auth.edalnice.cz/auth/connect/token",
                formParameters =
                    parameters {
                        append("grant_type", "client_credentials")
                        append("scope", "eshop.api eshoppayment.api")
                    },
            ) {
                headers.append(HttpHeaders.Authorization, "Basic ZXNob3AuY2xpZW50OjVxejNYUXVBbV9fYkpVZ0FEVEN5UCo=")
            }

        val accessToken = response.body<TokenResponse>().accessToken
        logger.info { "accessToken [$accessToken]" }

        val url =
            URLBuilder(
                protocol = URLProtocol.HTTPS,
                // TODO: configuration
                host = "eshop.edalnice.cz",
                pathSegments = listOf("api", "v3", "charge_registrations", CZ_COUNTRY_CODE, licensePlate.uppercase()),
            ).buildString()
        val res =
            client.get(url) {
                headers.append(HttpHeaders.Authorization, "Bearer $accessToken")
                headers.append(HttpHeaders.ContentType, "application/json,text/json")
            }
        val body = res.body<CheckLicensePlateResponse>()
        return body.charges.any {
            it.isCurrentlyValid
        }
    }

    @Serializable
    private class TokenResponse(
        @SerialName("access_token")
        val accessToken: String,
    )

    @Serializable
    private class CheckLicensePlateResponse(
        val charges: List<CheckLicensePlateResponseCharge>,
    )

    @Serializable
    private class CheckLicensePlateResponseCharge(
        val isCurrentlyValid: Boolean,
        val validSince: Instant,
        val validUntil: Instant,
        val priceListItemId: String,
    )

    companion object {
        // TODO: configuration
        const val CZ_COUNTRY_CODE = "3906ba89-153c-4038-8e36-0ca1deb76076"
    }
}
