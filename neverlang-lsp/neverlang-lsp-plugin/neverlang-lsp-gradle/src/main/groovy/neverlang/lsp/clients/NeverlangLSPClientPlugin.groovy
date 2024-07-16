package neverlang.lsp.clients

import com.github.jengelman.gradle.plugins.shadow.ShadowBasePlugin
import com.github.jengelman.gradle.plugins.shadow.ShadowExtension
import com.github.jengelman.gradle.plugins.shadow.ShadowJavaPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.transform.CompileStatic
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property


@CompileStatic
class NeverlangLSPClientPlugin extends ShadowBasePlugin  {

    static final String GROUP = 'it.unimi.di.adaptlab'
    static final String NAME = 'neverlangLSPClient'
    static final String CONFIGURATION = 'lspClientRuntimeClasspath'
//    static final File AIDE_OUTPUT_DIR = Paths.get("build","generated","sources", NAME).toFile()

    @Override
    void apply(Project project) {
        def extension = project.extensions.create(NAME, LSPClientPluginExtension)
        project.configurations.create(CONFIGURATION)
        project.plugins.apply(ShadowBasePlugin.class)

        project.afterEvaluate {
            def shadow = project.task (
                    type: ShadowJar,
                    group: 'build',
                    description: 'Generates the LSP client for the language server',
                    ShadowJavaPlugin.SHADOW_JAR_TASK_NAME
            ) as ShadowJar
            shadow.manifest.attributes(
                    //'Main-Class': 'lsp.SocketLauncher'
                    //'Main-Class': 'lsp.PipeLauncher'
                    'Main-Class': extension.getLauncher().get()
            )
            shadow.from(project.configurations.getByName('runtimeClasspath'))
            //        excludes.remove("module-info.class")
            shadow.exclude("module-info.class", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")

            def generateLspClient = project.task(
                    type: GenerateLSPClientTask,
                    group: 'build',
                    description: GenerateLSPClientTask.TASK_DESCRIPTION,
                    GenerateLSPClientTask.TASK_NAME
            )
            generateLspClient.dependsOn(shadow)
//        def compileJava = project.tasks.named(JavaPlugin.COMPILE_JAVA_TASK_NAME).get()
//        aideFM.dependsOn compileJava

            addDependencies(project, shadow)

        }
    }

    static void addDependencies(Project project, ShadowJar shadow) {
        def extension = project.extensions.findByType(LSPClientPluginExtension.class)

        ListProperty<String> modules = extension.clientImplementations
        Property<String> version = extension.generatorVersion
        if (!(modules.isPresent() && version.isPresent())) {
            throw new GradleException("Module and version are required")
        }

//        project.buildscript.repositories.gradlePluginPortal()
//        project.repositories.maven { url = "https://plugins.gradle.org/m2/" }

//        project.tasks.named(ShadowJavaPlugin.SHADOW_JAR_TASK_NAME).collect({it as ShadowJar}).each {
//            it.archiveBaseName.set(NAME)
//            it.archiveClassifier.set("client")
//            it.archiveVersion.set('')
//            //project.tasks.named("generateLSPClient").get().dependsOn(it)
//        }


//        def shadowExtension = project.extensions.findByType(ShadowExtension.class)
//        project.tasks.findAll { it instanceof ShadowJar }.collect({it as ShadowJar }).each {
//            it.archiveBaseName.set(NAME)
//            it.archiveClassifier.set("client")
//            it.archiveVersion.set('')
//            project.tasks.getByName("generateLSPClient").dependsOn(it)
//        }

        project.configurations.named("implementation").get().allDependencies.forEach {
            project.dependencies.add(CONFIGURATION, it)
        }

        project.dependencies.add(
                CONFIGURATION,
                "it.unimi.di.adaptlab:neverlang-lsp-client-generator:${extension.getGeneratorVersion().get()}"
        )

        modules.get().forEach { m ->
            project.dependencies.add(
                    CONFIGURATION,
                    m
            )
        }

        shadow.archiveBaseName.set(extension.getLanguageName().get())
        shadow.archiveClassifier.set("client")
        shadow.archiveVersion.set('')
    }
}
