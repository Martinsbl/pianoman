package net.testiprod.pianoman.app.desktop

import kotlin.test.Test
import kotlin.test.assertEquals
import net.testiprod.pianoman.app.config.AppConfig

class DesktopConfigTest {

    @Test
    fun `it reads the configs`() {
        val config = AppConfig.getConfig(DesktopConfigMapper())
        assertEquals(8080, config.midiServer.port)
        assertEquals(1000, config.aiConfig.maxTokens)
    }
}