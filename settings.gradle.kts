pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.hytale-modding.info/releases") {
            name = "HytaleModdingReleases"
            // FIXME remove once maven becomes public
            credentials {
                username = "tmp_viewer"
                password = "01KDD2ARZ795FTWBW0A97RP5RS"
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "ExamplePlugin"
