package com.xhstormr.browser.models

import kotlinx.serialization.Serializable

@Serializable
data class SecurityConfig(
    val username: String,
    val password: String,
)
