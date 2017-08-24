# Browse
![](https://i.loli.net/2017/08/24/599ecbf0167f0.png "Browse")

基于以下技术，实现了类似于 Nginx 的 autoindex 功能，能够 **索引本地文件**，但额外提供了 **文件上传功能** 和更加 **友好的界面**。

* Kotlin
* Spring
* Spring MVC
* Spring Boot
* Thymeleaf
* Gradle Kotlin DSL

## Run
```
java -jar browse.jar
```

访问 http://127.0.0.1:8080/

## Configuration

这里列出的为默认值，可以通过以下两种方式进行配置：

* 命令行参数：`java -jar browse.jar --name=Bob`
* 配置文件&#8195;：`src/main/resources/config.properties` 文件中添加 `name=Bob`

### 索引目录

config.base_path=D:/Download/

### 文件上传功能

config.enable_upload=false
