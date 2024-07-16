package neverlang.lsp.clients.vscode;

import neverlang.lsp.clients.SyntaxElement;
import neverlang.lsp.clients.TemplateGenerator;
import neverlang.lsp.clients.UnzipFiles;
import neverlang.parser.symbols.Symbol;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VSCodeTemplateGenerator implements TemplateGenerator {

    public String langName;
    public String langExt;
    public String jarPath;
    public String unzipPath;
    public String zipName;

    public VSCodeTemplateGenerator() {
    }

    @Override
    public TemplateGenerator accept(String langName, String ext, String path, String unzipPath) {
        this.langName = langName;
        this.langExt = ext;
        this.jarPath = path;
        this.unzipPath = unzipPath + "/" + langName.toLowerCase() + "-vscode-client-0.0.1";
        this.zipName = "VsCodeTemplate.zip";
        createPathAndFolder();
        return this;
    }

    private void createPathAndFolder() {
        try {
            // zipName is in src/main/resources
            UnzipFiles.unzip(VSCodeTemplateGenerator.class.getClassLoader().getResourceAsStream(zipName), unzipPath);
            // Move the files from the unzipped folder to the parent folder
            UnzipFiles.moveFiles(unzipPath + "/" + zipName.substring(0, zipName.length() - 4), unzipPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TemplateGenerator generateLSP() throws IOException {
        var extensionFile = this.getClass().getClassLoader().getResourceAsStream("extension.ts");
        var extensionContent = new String(extensionFile.readAllBytes(), StandardCharsets.UTF_8);

        var packageJsonFile = this.getClass().getClassLoader().getResourceAsStream("package.json");
        var packageJsonContent = new String(packageJsonFile.readAllBytes(), StandardCharsets.UTF_8);

        var extensionString = MessageFormat.format(extensionContent, langName, langExt, jarPath);
        var packageJsonString = MessageFormat.format(packageJsonContent, langName, langExt);

        // Create two file with the content of extensionString and packageJsonString
        var extensionPath = unzipPath + "/src/extension.ts";
        var packageJsonPath = unzipPath + "/package.json";
        var extensionFileWriter = new FileWriter(extensionPath);
        var packageJsonFileWriter = new FileWriter(packageJsonPath);
        extensionFileWriter.write(extensionString);
        packageJsonFileWriter.write(packageJsonString);
        extensionFileWriter.close();
        packageJsonFileWriter.close();

        // Run `npm install` in the unzipped folder
        var processBuilder = new ProcessBuilder("npm", "install");
        processBuilder.directory(new File(unzipPath));
        processBuilder.start();

        return this;
    }

    private List<String> moveAsteriskToEnd(List<String> l) {
        if (l.stream().noneMatch(e -> e.contains("*"))) {
            return l;
        }
        return Stream.concat(l.stream().map(s -> s.replace("*", "")), Stream.of("*")).toList();
    }

    private String escapeCharactersVscode(String s) {
        return escapeCharacters(s, List.of('*', '\\'));
    }

    @Override
    public void generateSyntaxHighlighter(Map<SyntaxElement, List<Symbol>> m) throws Exception {
        var syntaxFile = this.getClass().getClassLoader().getResourceAsStream("lang.tmLanguage.json");
        var syntexContent = new String(syntaxFile.readAllBytes(), StandardCharsets.UTF_8);


        List<String> comments = new ArrayList<>();
        var singleLine = this.getClass().getClassLoader().getResourceAsStream("singleLine.json");
        var singleLineContent = new String(singleLine.readAllBytes(), StandardCharsets.UTF_8);
        toListOfString(m.get(SyntaxElement.LINECOMMENT)).forEach(c -> comments.add(MessageFormat.format(singleLineContent, c, langName)));

        var multiLine = this.getClass().getClassLoader().getResourceAsStream("multiLine.json");
        var multiLineContent = new String(multiLine.readAllBytes(), StandardCharsets.UTF_8);
        var mlComments = toListOfString(m.get(SyntaxElement.LINECOMMENT));
        if (mlComments.size() % 2 == 0) {
            for (int i = 0; i < mlComments.size(); i += 2) {
                var start = escapeCharactersVscode(mlComments.get(i));
                var end = escapeCharactersVscode(mlComments.get(i + 1));
                comments.add(MessageFormat.format(multiLineContent, start, end, langName));
            }
        }


        var lspString = MessageFormat.format(syntexContent,
                langExt,
                langName,
                /* KEYWORDS */ String.join("|", new HashSet<>(toListOfString(m.get(SyntaxElement.KEYWORD)))),
                /* OPERATORS */ escapeCharactersVscode(String.join("", moveAsteriskToEnd(toListOfString(m.get(SyntaxElement.OPERATOR))))),
                /* CONSTANT */ String.join("|", toListOfString(m.get(SyntaxElement.CONSTANT))),
                /* COMMENTS */ String.join(",\n", comments)

//                /* LINECOMMENT */ toListOfString(m.get(SyntaxElement.LINECOMMENT)),
//
//                /* MULTILINECOMMENT START */ escapeCharactersVscode(toListOfString(m.get(SyntaxElement.MULTILINECOMMENT)).get(0)),
//                /* MULTILINECOMMENT END */ escapeCharactersVscode(toListOfString(m.get(SyntaxElement.MULTILINECOMMENT)).get(1))
        );
        // Create syntaxes folder if it doesn't exist
        var syntaxFolder = new File(unzipPath + "/syntaxes");
        if (!syntaxFolder.exists()) {
            syntaxFolder.mkdir();
        }
        var syntaxPath = unzipPath + "/syntaxes/" + langName + ".tmLanguage.json"; // .toLowerCase()
        var syntaxFileWriter = new FileWriter(syntaxPath);
        syntaxFileWriter.write(lspString);
        syntaxFileWriter.close();

        var bracketsFile = this.getClass().getClassLoader().getResourceAsStream("language-configuration.json");
        var bracketsContent = new String(bracketsFile.readAllBytes(), StandardCharsets.UTF_8);

        Set<String> brackets = new HashSet<>();
        var bracketsList = toListOfString(m.get(SyntaxElement.BRACKETS));
        if (bracketsList.size() % 2 == 0) {
            for (int i = 0; i < bracketsList.size(); i += 2) {
                var start = escapeCharactersVscode(bracketsList.get(i));
                var end = escapeCharactersVscode(bracketsList.get(i + 1));
                brackets.add("[\"%s\", \"%s\"]".formatted(start, end));
            }
        }

        var bracketsString = MessageFormat.format(bracketsContent,
                    String.join(", \n", brackets),
                    String.join(", \n", brackets)
                );

        var bracketsPath = unzipPath + "/language-configuration.json";
        var bracketsFileWriter = new FileWriter(bracketsPath);
        bracketsFileWriter.write(bracketsString);
        bracketsFileWriter.close();

    }
}
