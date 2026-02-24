package com.xhstormr.browser.route

import com.xhstormr.browser.attachmentHeaders
import com.xhstormr.browser.plugin.Auth
import com.xhstormr.browser.sanitizePath
import com.xhstormr.browser.view.createBrowserHtml
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.log
import io.ktor.server.auth.authenticate
import io.ktor.server.html.respondHtml
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.uri
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondPath
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.utils.io.copyAndClose
import io.ktor.utils.io.streams.asByteWriteChannel
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
import kotlin.io.path.notExists
import kotlin.io.path.outputStream

fun Route.browserRoutes(location: Path, enableUpload: Boolean) {
    val log = application.log

    authenticate(Auth.SESSION) {
        get<HomeApi.Browser> { req ->
            val key = sanitizePath(req.key)
            val path = location.resolve(key)

            when {
                path.isDirectory() -> {
                    log.info("list: {}", path.toUri())
                    call.respondHtml { createBrowserHtml(key, path, enableUpload) }
                }

                path.isRegularFile() -> {
                    log.info("download: {}", path.toUri())
                    call.response.header(HttpHeaders.ContentDisposition, attachmentHeaders(path.name))
                    call.respondPath(path)
                }

                path.notExists() -> call.respond(HttpStatusCode.NotFound)
                else -> call.respond(HttpStatusCode.BadRequest)
            }
        }

        post<HomeApi.Browser> { req ->
            if (!enableUpload) call.respond(HttpStatusCode.BadRequest)

            val key = sanitizePath(req.key)
            val path = location.resolve(key)

            call.receiveMultipart().forEachPart { part ->
                if (part is PartData.FileItem) {
                    var dest = path.resolve(part.originalFileName!!)
                    var i = 0
                    while (dest.exists()) {
                        dest = dest.resolveSibling("${part.originalFileName}.${++i}")
                    }
                    log.info("upload: {}", dest.toUri())
                    part.provider().copyAndClose(dest.outputStream().asByteWriteChannel())
                }
                part.dispose()
            }
            call.respondRedirect(call.request.uri)
        }
    }
}
