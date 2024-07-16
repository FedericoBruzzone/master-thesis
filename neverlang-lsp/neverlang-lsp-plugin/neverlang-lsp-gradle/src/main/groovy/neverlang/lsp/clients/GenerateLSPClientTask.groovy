package neverlang.lsp.clients

import org.gradle.api.GradleException
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.plugins.JavaPlugin

class GenerateLSPClientTask extends JavaExec {
    public static final String TASK_NAME = "generateLSPClient"
    public static final String TASK_DESCRIPTION = "Generates the LSP client for the language server"

    GenerateLSPClientTask() {
        standardOutput = System.out
        mainClass.convention("neverlang.lsp.clients.LspClientGenerator")
    }

    @TaskAction
    void exec() {
        //Set Classpath
        def currClasspath = classpath + project.configurations.getByName(NeverlangLSPClientPlugin.CONFIGURATION)
        super.setClasspath(currClasspath)

        //Get language ClassPath
        JavaCompile javaCompile = project.tasks.named(JavaPlugin.COMPILE_JAVA_TASK_NAME).get() as JavaCompile

        def extension = project.extensions.getByType(LSPClientPluginExtension)
        def name = extension.getLanguageName()
        def ext = extension.getFileExt()
        def jarPath = extension.getJarPath()
        def templateGenerators = extension.getTemplateGeneratorClasses()

        if(name.isPresent() && ext.isPresent() && jarPath.isPresent() && templateGenerators.isPresent()) {
            def currArgs = []
            currArgs.add("--language")
            currArgs.add(name.get())
            currArgs.add("--extension")
            currArgs.add(ext.get())
            currArgs.add("--jarPath")
            currArgs.add(jarPath.get())
            currArgs.add("--generators")
            currArgs.add(templateGenerators.get().join(":"))
            currArgs.add("--unzipPath")
            currArgs.add(project.rootDir.absolutePath)

            super.setArgs(currArgs)
            super.exec()
        } else {
            throw new GradleException();
        }

    }
}
