package neverlang.lsp.clients.vim;

import neverlang.lsp.clients.SyntaxElement;
import neverlang.lsp.clients.TemplateGenerator;
import neverlang.parser.symbols.Symbol;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class VimTemplateGenerator implements TemplateGenerator {
    public String name;
    public String ext;
    public String jarPath;
    public String unzipPath;

    private String unzipFolder;

    public VimTemplateGenerator() {
    }

    @Override
    public TemplateGenerator accept(String name, String ext, String path, String unzipPath) {
        this.name = name;
        this.ext = ext;
        this.jarPath = path;
        this.unzipPath = unzipPath;
        createPathAndFolder();
        return this;
    }

    private void createPathAndFolder() {
        try {
            // Create a folder called VimClient
            var unzipFolder = this.unzipPath + "/" + name.toLowerCase() + "-vim-client-0.0.1";
            var unzipFolderCreate = new File(unzipFolder);
            if (!unzipFolderCreate.mkdir()) {
                throw new IOException("Failed to create folder");
            }
            this.unzipFolder = unzipFolder;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TemplateGenerator generateLSP() throws IOException {
        var cocSettingsFile = this.getClass().getClassLoader().getResourceAsStream("coc-settings.json");
        var cocSettingsContent = new String(cocSettingsFile.readAllBytes(), StandardCharsets.UTF_8);
        var cocSettingsString = MessageFormat.format(cocSettingsContent, name, jarPath);

        var langFile = this.getClass().getClassLoader().getResourceAsStream("lang.vim");
        var langContent = new String(langFile.readAllBytes(), StandardCharsets.UTF_8);
        var langString = MessageFormat.format(langContent, ext, name);

        // Create two file with the content of lspString and langString
        var lspPath = unzipFolder + "/coc-settings.json";
        var langPath = unzipFolder + "/" + this.name + ".vim";
        var lspFileWriter = new FileWriter(lspPath);
        var langFileWriter = new FileWriter(langPath);
        lspFileWriter.write(cocSettingsString);
        langFileWriter.write(langString);
        lspFileWriter.close();
        langFileWriter.close();

        return this;
    }

    private String escapeCharactersVim(String s) {
        return escapeCharacters(s, List.of('*', '.', '[', ']', '\\', '^', '$', '?', '|', '+', '='));
    }

    @Override
    public void generateSyntaxHighlighter(Map<SyntaxElement, List<Symbol>> m) throws Exception {
        var syntaxFile = this.getClass().getClassLoader().getResourceAsStream("syntax.vim");
        var syntexContent = new String(syntaxFile.readAllBytes(), StandardCharsets.UTF_8);
        String multilineCommentStart;
        String multilineCommentEnd;
        if (toListOfString(m.get(SyntaxElement.MULTILINECOMMENT)).size() > 1 ) {
            multilineCommentStart = escapeCharactersVim(toListOfString(m.get(SyntaxElement.MULTILINECOMMENT)).get(0));
            multilineCommentEnd = escapeCharactersVim(toListOfString(m.get(SyntaxElement.MULTILINECOMMENT)).get(1));
        } else {
            multilineCommentStart = "";
            multilineCommentEnd = "";
        }
        var lspString = MessageFormat.format(syntexContent,
                name.toLowerCase(),
                /* KEYWORDS */ String.join(" ", toListOfString(m.get(SyntaxElement.KEYWORD))),
                /* OPERATOR */ String.join(" ", toListOfString(m.get(SyntaxElement.OPERATOR))),
                /* CONSTANT */ String.join(" ", toListOfString(m.get(SyntaxElement.CONSTANT))),
                /* LINECOMMENT */ String.join(" ", toListOfString(m.get(SyntaxElement.LINECOMMENT))),
                /* MULTILINECOMMENT START */String.join(" ", multilineCommentStart),
                /* MULTILINECOMMENT END */String.join(" ", multilineCommentEnd),
                /* STRING */ "/\".*\"/"
        );
        var syntaxPath = unzipFolder + "/syntax.vim";
        var syntaxFileWriter = new FileWriter(syntaxPath);
        syntaxFileWriter.write(lspString);
        syntaxFileWriter.close();
    }
}