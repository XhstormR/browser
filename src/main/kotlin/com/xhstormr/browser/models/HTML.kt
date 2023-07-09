package com.xhstormr.browser.models

import kotlinx.html.FormEncType
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.fileInput
import kotlinx.html.footer
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.header
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.main
import kotlinx.html.meta
import kotlinx.html.onChange
import kotlinx.html.onKeyUp
import kotlinx.html.postForm
import kotlinx.html.script
import kotlinx.html.span
import kotlinx.html.styleLink
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.title
import kotlinx.html.tr
import kotlinx.html.unsafe
import java.nio.file.Path
import kotlin.collections.set

fun createHTML(
    key: Path,
    enableUpload: Boolean,
    paths: List<PathWrapper>,
    breadcrumbs: List<Breadcrumb>,
): HTML.() -> Unit = {
    head {
        meta("viewport", "width=device-width, initial-scale=1.0", "utf-8")
        title(breadcrumbs.lastOrNull()?.text ?: "/")
        styleLink("/css/main.css")
    }
    body {
        header {
            h1 {
                a(BASE_HREF) { text("/") }
                breadcrumbs.forEach {
                    a(BASE_HREF + it.link) { text(it.text) }
                    text("/")
                }
            }
        }
        main {
            div("meta") {
                div {
                    id = "summary"
                    span("meta-item") { text(paths.count { it.isDir }.toString() + " directory") }
                    span("meta-item") { text(paths.count { !it.isDir }.toString() + " file") }
                    span("meta-item") {
                        input {
                            id = "filter"
                            onKeyUp = "filter()"
                            placeholder = "过滤"
                        }
                    }
                }
            }
            div("listing") {
                table {
                    thead {
                        tr {
                            th { text("Name") }
                            th { text("Size") }
                            th(classes = "hideable") { text("Modified") }
                        }
                    }
                    tbody {
                        if (breadcrumbs.isNotEmpty()) {
                            tr {
                                td { a(BASE_HREF + (key.parent ?: "")) { span("name") { text("Go up") } } }
                                td { text("—") }
                                td("hideable") { text("—") }
                            }
                        }
                        paths.forEach {
                            tr("file") {
                                td {
                                    a(BASE_HREF + key.resolve(it.name)) {
                                        unsafe { raw(if (it.isDir) SVG_DIR else SVG_FILE) }
                                        span("name") { text(it.name) }
                                    }
                                }
                                td { text(if (it.isDir) "—" else it.formatSize()) }
                                td("hideable") { text(it.formatTime()) }
                            }
                        }
                        if (enableUpload) {
                            tr {
                                td {
                                    colSpan = "3"
                                    postForm(BASE_HREF + key, FormEncType.multipartFormData) {
                                        onChange = "submit()"
                                        label {
                                            id = "box"
                                            htmlFor = "uploadFile"
                                            text("选择文件进行上传")
                                        }
                                        fileInput(name = "file") {
                                            id = "uploadFile"
                                            attributes["style"] = "display: none;"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        footer {
            text("© 2023 ")
            a("https://github.com/XhstormR/browser") {
                text("XhstormR")
            }
        }
        script { src = "/js/main.js" }
    }
}

private const val BASE_HREF = "/browser?key=/"

private const val SVG_DIR =
    """<svg width="1.5em" height="1em" viewBox="0 0 317 259"><use xlink:href="/images/main.svg#folder"></use></svg>"""
private const val SVG_FILE =
    """<svg width="1.5em" height="1em" viewBox="0 0 265 323"><use xlink:href="/images/main.svg#file"></use></svg>"""
