package com.xhstormr.browser.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import org.slf4j.event.Level

fun Application.configureCallLogging() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> !call.parameters.contains("static-content-path-parameter") }
        // format { call ->
        //     val status = call.response.status()
        //     val httpMethod = call.request.httpMethod.value
        //     val path = call.request.path()
        //     val remoteAddress = call.request.local.remoteAddress
        //     val remotePort = call.request.local.remotePort
        //     "[$httpMethod $path, $status, client=/$remoteAddress:$remotePort]"
        // }
    }
}
