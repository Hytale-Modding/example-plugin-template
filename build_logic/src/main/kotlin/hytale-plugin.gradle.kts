
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings

plugins {
    idea
    `java-library`
    id("org.jetbrains.gradle.plugin.idea-ext")
}

val hytaleExtension = extensions.create(HytaleExtension.EXTENSION_NAME, HytaleExtension::class)

project.afterEvaluate {
    val ideaModel = rootProject.extensions.ideaExt

    dependencies {
        // TODO should we make this configurable?
        implementation(files("${hytaleExtension.serverDir.get()}/HytaleServer.jar"))
    }

    hytaleExtension.syncTask.orNull?.let {
        tasks.processResources.configure {
            dependsOn(it)
        }
    }

    mkdir(hytaleExtension.runDir)

    // TODO make server properties configurable
    val programArgs = mutableListOf(
        "--assets=${hytaleExtension.assetsDir.get()}",
        "--packs=${hytaleExtension.packsDir.get()}"
    )

    if(hytaleExtension.allowOp.get()) {
        programArgs.add("--allow-op")
    }

    if(hytaleExtension.disableSentry.get()) {
        programArgs.add("--disable-sentry")
    }

    if(hytaleExtension.disableFileWatcher.get()) {
        programArgs.add("--disable-file-watcher")
    }

    hytaleExtension.programArgs.orNull?.let { programArgs.addAll(it) }

    val aotFile = project.file("${hytaleExtension.serverDir.get()}/HytaleServer.aot")
    val aotArg = if (aotFile.exists()) "-XX:AOTCache=${aotFile.absolutePath}" else ""

    val javaArgs = listOf(aotArg)

    // FIXME IDEA bug: need to somehow get the run configs to *run* with the project's JDK not the root project's JDK version.
//    val projectModuleName = if (project == rootProject) {
//        "${ideaModel.project.name}.main"
//    } else {
//        "${rootProject.name}.${project.name}.main"
//    }
//
//    ideaModel.project.settings {
//        runConfigurations {
//            create<org.jetbrains.gradle.ext.Application>(hytaleExtension.runConfigName.get()) {
//                mainClass = "com.hypixel.hytale.Main"
//                moduleName = projectModuleName
//                programParameters = programArgs.joinToString(" ")
//                jvmArgs = javaArgs.joinToString(" ")
//                workingDirectory = hytaleExtension.runDir.get()
//
//                beforeRun {
//                    mkdir(hytaleExtension.runDir)
//                }
//
//                hytaleExtension.syncTask.orNull?.let {
//                    beforeRun.register<GradleTask>("prepareTask") {
//                        task = it
//                    }
//                }
//            }
//        }
//    }

    val runTask = tasks.register<JavaExec>("runServer") {
        mainClass = "com.hypixel.hytale.Main"
        modularity.inferModulePath = true
        classpath = sourceSets.main.get().runtimeClasspath
        args = programArgs
        jvmArgs = javaArgs
        standardInput = System.`in`

        workingDir(hytaleExtension.runDir)

        hytaleExtension.syncTask.orNull?.let {
            dependsOn(it)
        }
    }

    // Task#path but we cant access that because it's a TaskProvider
    val taskPath = buildString {
        if (project.path != rootProject.path) {
            append(project.path)
        }

        append(":${runTask.name}")
    }

    ideaModel.project.settings {
        runConfigurations {
            create<org.jetbrains.gradle.ext.Gradle>(hytaleExtension.runConfigName.get()) {
                taskNames = listOf(taskPath)
            }
        }
    }
}
