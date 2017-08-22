package com.xhstormr.browse.entity

import java.nio.file.Files
import java.nio.file.Path
import java.util.*

data class PathWrapper(private val path: Path) {
    val isDir = Files.isDirectory(path)

    fun date() = Date.from(Files.getLastModifiedTime(path).toInstant())

    fun size() = Files.size(path) / 1024.0

    fun name() = path.fileName.toString()

    fun link() = "./${name()}".run { return@run if (isDir) this.plus('/') else this }
}
