plugins {
    `maven-publish`
    id("hytale-plugin")
}

group = "com.example"
version = "0.1.0"
val javaVersion = 25

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.jspecify)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }

    withSourcesJar()
}

val generateMetadataFile = tasks.register("generateMetadataFile", ProcessResources::class) {
    var replaceProperties = mapOf(
        "plugin_group" to project.group,
        "plugin_name" to project.name,
        "plugin_version" to project.version,
        "server_version" to findProperty("server_version"),

        "plugin_description" to findProperty("plugin_description"),
        "plugin_website" to findProperty("plugin_website"),

        "plugin_main_entrypoint" to findProperty("plugin_main_entrypoint"),
        "plugin_author" to findProperty("plugin_author")
    )

    val inputDir = "src/main/templates"
    val outputDir = "build/generated/sources/customMetadata"

    inputs.properties(replaceProperties)
    inputs.dir(inputDir)
    outputs.dir(outputDir)

    expand(replaceProperties)
    from(inputDir)
    into(outputDir)
}

hytale {
    syncTask = generateMetadataFile
}

sourceSets.main.configure {
    resources.srcDir(generateMetadataFile)
}

tasks.withType<Jar> {
    manifest {
        attributes["Specification-Title"] = rootProject.name
        attributes["Specification-Version"] = version
        attributes["Implementation-Title"] = project.name
        attributes["Implementation-Version"] =
            providers.environmentVariable("COMMIT_SHA_SHORT")
                .map { "${version}-${it}" }
                .getOrElse(version.toString())
    }
}

publishing {
    repositories {
        // This is where you put repositories that you want to publish to.
        // Do NOT put repositories for your dependencies here.
    }

    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
