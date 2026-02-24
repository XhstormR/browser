package com.xhstormr.browser

import com.xhstormr.browser.plugin.configureAttachment
import com.xhstormr.browser.plugin.configureAuthentication
import com.xhstormr.browser.plugin.configureCallLogging
import com.xhstormr.browser.plugin.configureForwardedHeaders
import com.xhstormr.browser.plugin.configureRouting
import com.xhstormr.browser.plugin.configureSessions
import com.xhstormr.browser.plugin.configureStatusPages
import io.ktor.server.application.Application
import io.ktor.server.cio.EngineMain

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureAuthentication()
    configureAttachment()
    configureForwardedHeaders()
    configureStatusPages()
    configureCallLogging()
    configureSessions()
    configureRouting()
}
