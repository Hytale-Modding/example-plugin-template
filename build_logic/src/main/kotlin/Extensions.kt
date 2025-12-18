import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.getByName
import org.gradle.plugins.ide.idea.model.IdeaModel

internal val ExtensionContainer.hytale: HytaleExtension
    get() = this.getByName<HytaleExtension>(HytaleExtension.EXTENSION_NAME)

internal val ExtensionContainer.ideaExt: IdeaModel
    get() = this.getByName<IdeaModel>("idea")
