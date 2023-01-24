# browser
![](https://i.loli.net/2017/08/25/599fcabc4815a.png "Browse")

基于以下技术，实现了类似于 Nginx 的 autoindex 功能，能够 **索引本地文件**，但额外提供了 **文件上传功能** 和更加 **友好的界面**。

UPDATE：发布 6 年后还有人记得这个项目（我都忘了😂），由于长达 6 年毫无更新，所以从技术栈到代码几乎完全重写。

* Kotlin
* Ktor
* Kotlinx.html
* ~~Spring~~
* ~~Spring MVC~~
* ~~Spring Boot~~
* ~~Thymeleaf~~
* Gradle Kotlin DSL

## Run

访问 http://127.0.0.1:8080/ ，默认账号密码 admin:123456。

```bash
java -jar browser-2.0-SNAPSHOT.jar -P:resource.location=/Users/user/Downloads
```

## Configuration

使用 `-P:` 标志设置命令行参数。

* `security.username`: 登录用户，默认 `admin`
* `security.password`: 登录密码，默认 `123456`
* `resource.location`: 索引目录，默认 `/Users/user/Downloads`
* `resource.enable-upload`: 上传功能，默认 `true`
