package com.xhstormr.browser.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.partialcontent.PartialContent

fun Application.configureAttachment() {
    install(PartialContent)
    install(AutoHeadResponse)
}
