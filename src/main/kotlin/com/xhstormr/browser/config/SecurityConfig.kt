package com.xhstormr.browser.config

import kotlinx.serialization.Serializable

@Serializable
data class SecurityConfig(
    val username: String,
    val password: String,
)
