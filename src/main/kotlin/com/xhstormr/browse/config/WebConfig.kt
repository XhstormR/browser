package com.xhstormr.browse.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import java.nio.file.Path
import java.nio.file.Paths

@Configuration
@ConfigurationProperties("config")
open class WebConfig : WebMvcConfigurerAdapter() {
    var basePath = "D:/"
    var enableUpload = false

    @Bean
    open fun basePath(): Path = Paths.get(basePath)

    @Bean
    open fun enableUpload(): Boolean = enableUpload
}
