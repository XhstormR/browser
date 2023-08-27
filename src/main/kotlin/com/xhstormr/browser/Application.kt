package com.xhstormr.browser

import com.xhstormr.browser.plugins.configureAttachment
import com.xhstormr.browser.plugins.configureAuthentication
import com.xhstormr.browser.plugins.configureCallLogging
import com.xhstormr.browser.plugins.configureForwardedHeaders
import com.xhstormr.browser.plugins.configureRouting
import com.xhstormr.browser.plugins.configureSessions
import com.xhstormr.browser.plugins.configureStatusPages
import io.ktor.server.application.Application
import io.ktor.server.cio.EngineMain

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureAuthentication(environment.config)
    configureAttachment()
    configureForwardedHeaders()
    configureStatusPages()
    configureCallLogging()
    configureSessions()
    configureRouting(environment.config)
}
