# Hytale Plugin Template

A template for Hytale java plugins

---

### How to use

1. click the `Use this template` button to copy this repository to your own GitHub account.
2. clone the repository locally.
3. Set your mod name in `settings.gradle.kts`, group and version in `build.gradle.kts`, and all other project
    properties in `gradle.properties`
4. Open the project in IDEA and reload the gradle project.
5. To launch the game with your mod, simply launch the generated run config in your IDE.

> [!NOTE]
> To decompile the game and make it searchable, run the `decompileServer` gradle task, then open any game class in your
> IDE, press the `Choose Sources...` button, and select the jar file ending in `-sources.jar`.

---

### Configuring the Template
If you for example installed the game in a non-standard location, you will need to tell the project about that.
The recommended way is to create a file at `%USERPROFILE%/.gradle/gradle.properties` to set these properties globally.

```properties
# Set a custom game install location
hytale.install_dir=path/to/Hytale

# Speed up the decompilation process significantly, by only including the core hytale packages.
# Recommended if decompiling the game takes a very long time on your PC.
hytale.decompile_partial=true
```
