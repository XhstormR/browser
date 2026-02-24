package com.xhstormr.browser.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import org.slf4j.event.Level

fun Application.configureCallLogging() {
    install(CallLogging) {
        level = Level.INFO
        disableForStaticContent()
        // format { call ->
        //     val status = call.response.status()
        //     val method = call.request.httpMethod.value
        //     val path = call.request.path()
        //     val remoteHost = call.request.origin.remoteHost
        //     val remotePort = call.request.origin.remotePort
        //     "[$status, $method $path, client=/$remoteHost:$remotePort]"
        // }
    }
}
