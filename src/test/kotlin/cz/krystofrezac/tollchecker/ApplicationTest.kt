package cz.krystofrezac.tollchecker

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import junit.framework.TestCase.assertEquals

// TODO: tests
class ApplicationTest {
//    @Test
    fun testRoot() =
        testApplication {
            client.get("/").apply {
                assertEquals(HttpStatusCode.OK, status)
                assertEquals("Hello World!", bodyAsText())
            }
        }
}
