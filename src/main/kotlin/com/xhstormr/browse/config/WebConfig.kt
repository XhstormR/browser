package com.xhstormr.browse.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import java.nio.file.Path
import java.nio.file.Paths

@Configuration
open class WebConfig : WebMvcConfigurerAdapter() {
    @Bean
    open fun basePath(@Value("#{environment['base_path']?:'D:/'}") basePath: String): Path = Paths.get(basePath)
}
