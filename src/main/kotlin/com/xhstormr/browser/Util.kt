package com.xhstormr.browser

import io.ktor.http.ContentDisposition
import io.ktor.util.normalizeAndRelativize
import kotlin.io.path.Path

fun attachmentHeaders(fileName: String) = ContentDisposition.Attachment
    .withParameter(ContentDisposition.Parameters.FileName, fileName)
    .withParameter(ContentDisposition.Parameters.FileNameAsterisk, fileName)
    .toString()

fun sanitizePath(key: String?) = Path(key ?: "/").normalizeAndRelativize()

fun formatFileSize(bytes: Long): String {
    val (divisor, unit) = when {
        bytes > 1024 * 1024 * 1024 -> 1024L * 1024 * 1024 to "GB"
        bytes > 1024 * 1024 -> 1024L * 1024 to "MB"
        bytes > 1024 -> 1024L to "KB"
        else -> 1L to "B"
    }
    return "%.1f %s".format(bytes * 1.0 / divisor, unit)
}
