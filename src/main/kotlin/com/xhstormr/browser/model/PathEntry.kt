package com.xhstormr.browser.model

import com.xhstormr.browser.formatFileSize
import java.nio.file.Path
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.io.path.extension
import kotlin.io.path.fileSize
import kotlin.io.path.getLastModifiedTime
import kotlin.io.path.isDirectory
import kotlin.io.path.name

class PathEntry(val path: Path) : Comparable<PathEntry> {

    val name = path.name

    val isDir = path.isDirectory()

    val size = path.fileSize()

    val time = path.getLastModifiedTime().toInstant()

    val timeFormatted = timeFormatter.format(time)

    val sizeFormatted = formatFileSize(size)

    override fun compareTo(other: PathEntry) =
        compareValuesBy(other, this, { it.isDir }, { it.path.extension }, { it.name })

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PathEntry) return false
        return path == other.path
    }

    override fun hashCode() = path.hashCode()

    override fun toString() = "PathEntry($path)"

    companion object {
        private val timeFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault())
    }
}
