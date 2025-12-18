import net.harawata.appdirs.AppDirs
import net.harawata.appdirs.AppDirsFactory
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import javax.inject.Inject

const val defaultUpdateChannel = "release"

abstract class HytaleExtension @Inject constructor(factory: ProviderFactory, private val project: Project) {

    companion object {
        const val EXTENSION_NAME = "hytale"
    }

    @get:InputDirectory
    abstract val gameDir: Property<String>

    @get:InputDirectory
    abstract val assetsDir: Property<String>

    @get:InputDirectory
    abstract val serverDir: Property<String>

    @get:InputDirectory
    abstract val hytaleUserDir: Property<String>

    @get:InputDirectory
    abstract val packsDir: Property<String>

    @get:Input
    abstract val updateChannel: Property<String>

    @get:Input
    abstract val runConfigName: Property<String>

    @get:OutputDirectory
    abstract val runDir: Property<String>

    @get:Input
    abstract val syncTask: Property<Task>

    @get:Input
    abstract val allowOp: Property<Boolean>

    @get:Input
    abstract val disableSentry: Property<Boolean>

    @get:Input
    abstract val disableFileWatcher: Property<Boolean>

    @get:Input
    abstract val programArgs: ListProperty<String>

    init {
        updateChannel.convention(defaultUpdateChannel)

        gameDir.convention(factory.provider {
            val appDirs: AppDirs = AppDirsFactory.getInstance()
            appDirs.getUserConfigDir("Hytale", null, null, true)
        })
        assetsDir.convention(factory.provider { "${gameDir.get()}/install/${updateChannel.get()}/package/game/latest/Assets.zip" })
        serverDir.convention(factory.provider { "${gameDir.get()}/install/${updateChannel.get()}/package/game/latest/Server" })
        hytaleUserDir.convention(factory.provider { "${gameDir.get()}/UserData" })
        packsDir.convention(factory.provider { "${hytaleUserDir.get()}/Packs" })

        runConfigName.convention(factory.provider {
            var name = "HytaleServer"
            if (project != project.rootProject) {
                name += " (${project.name})"
            }

            name
        })

        runDir.convention(factory.provider { project.layout.projectDirectory.file("run").asFile.absolutePath })

        allowOp.convention(true)
        disableSentry.convention(true)
        disableFileWatcher.convention(false)
    }
}
