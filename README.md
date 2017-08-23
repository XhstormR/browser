# Browse
![](https://i.loli.net/2017/08/23/599d85f8aa5a4.png "Browse")

类似于 Nginx 的 autoindex 功能，能够索引本地文件，但额外提供了文件上传功能，界面也更加友好。

## Run
```
java -jar Browse-1.0-SNAPSHOT.jar
```

## Configuration

### 指定索引目录

索引目录缺省为 `D:/Download/`，可以通过以下两种方式进行指定。

* 命令行参数：`java -jar Browse-1.0-SNAPSHOT.jar --base_path=D:/Download/`
* 配置文件&#8195;：`src/main/resources/config.properties` 文件中添加 `base_path=D:/Download/`
