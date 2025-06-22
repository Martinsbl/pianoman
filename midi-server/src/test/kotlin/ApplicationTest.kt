package net.testiprod

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import net.testiprod.midi.server.module

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }
        client.get("/midi/devices").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

}
