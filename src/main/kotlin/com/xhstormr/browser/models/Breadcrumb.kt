package com.xhstormr.browser.models

import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.pathString

data class Breadcrumb(
    val link: String,
    val text: String,
)

fun breadcrumbs(key: Path): List<Breadcrumb> {
    if (key.name.isEmpty()) return listOf()

    val list = mutableListOf<Breadcrumb>()
    var parent = key
    while (true) {
        list.add(Breadcrumb(parent.pathString, parent.name))
        parent = parent.parent ?: break
    }
    return list.reversed()
}
