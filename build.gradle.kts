plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.2.1"
}

group = "org.mrdarkimc"
version = "1.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2023.3")
        bundledPlugin("Git4Idea")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "233"
            untilBuild = provider { null }
        }
    }
}
