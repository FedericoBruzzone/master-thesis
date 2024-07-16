package neverlang.lsp.clients

import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.provider.Property

import java.util.stream.Stream

abstract class LSPClientPluginExtension {

    @Input
    abstract ListProperty<String> getClientImplementations();

    @Input
    abstract Property<String> getGeneratorVersion();

    @Input
    abstract ListProperty<String> getTemplateGeneratorClasses();

    @Input
    abstract Property<String> getLanguageName();

    @Input
    abstract Property<String> getFileExt();

    @Input
    abstract Property<String> getJarPath();

    @Input
    abstract Property<String> getLauncher();
}
