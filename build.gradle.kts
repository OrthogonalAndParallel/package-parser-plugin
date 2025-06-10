plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij.platform") version "2.3.0"
}

// 设置源码路径，包含 kotlin 目录
sourceSets {
    main {
        java {
            srcDir("src/main/java")
        }
    }
}

group = "com.yonyou"
version = "1.1-RELEASE"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    intellijPlatform {
        create("IC", "2024.2.5")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
        bundledPlugin("com.intellij.java")
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "182"      // 表示不限制最低版本
            untilBuild = "251.*"  // 表示不限制最高版本
        }
        changeNotes = """
      Initial version
    """.trimIndent()
    }
}

tasks {
    withType<org.jetbrains.intellij.platform.gradle.tasks.PrepareJarSearchableOptionsTask>().configureEach {
        enabled = false
    }
    jar {
        // 将所有依赖打包进插件 JAR
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}
