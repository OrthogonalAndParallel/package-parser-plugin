# 项目解析插件

## 打包
`./gradlew clean build`

## 目录说明

```
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
├── gradle.properties   # gradle配置文件
├── build.gradle.kts    # 项目构建文件
└── src
    └── main
        ├── java
        │   └── com
        │       └── yonyou
        │           └── packageparser   # 方法设计助手
        │               ├── PackageParserAction.java
        │               ├── PackageParserDialog.java
        │               └── ProjectManager.java
        │           └── losemethodparser   # 失效方法治理
        └── resources
            └── META-INF
                ├── plugin.xml
                └── pluginIcon.svg
```