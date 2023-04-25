package com.xhstormr.browser

import io.ktor.http.ContentDisposition
import io.ktor.util.normalizeAndRelativize
import kotlin.io.path.Path

fun attachmentHeaders(fileName: String) = ContentDisposition.Attachment
    .withParameter(ContentDisposition.Parameters.FileName, fileName)
    .withParameter(ContentDisposition.Parameters.FileNameAsterisk, fileName)
    .toString()

fun sanitizePath(key: String?) = Path(key ?: "/").normalizeAndRelativize()
