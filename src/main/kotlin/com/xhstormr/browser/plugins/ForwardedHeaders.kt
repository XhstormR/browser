package com.xhstormr.browser.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.forwardedheaders.XForwardedHeaders

fun Application.configureForwardedHeaders() {
    install(XForwardedHeaders)
}
