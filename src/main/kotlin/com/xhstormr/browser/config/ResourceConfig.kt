package com.xhstormr.browser.config

import kotlinx.serialization.Serializable

@Serializable
data class ResourceConfig(
    val location: String,
    val enableUpload: Boolean,
)
