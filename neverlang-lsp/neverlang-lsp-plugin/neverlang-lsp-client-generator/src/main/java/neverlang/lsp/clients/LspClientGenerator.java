package neverlang.lsp.clients;

import neverlang.parser.symbols.Symbol;
import neverlang.runtime.Language;
import picocli.CommandLine;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@CommandLine.Command(name = "lsp-client-generator")
public class LspClientGenerator implements Callable<Integer> {
    @CommandLine.Option(
            names = {"-l", "--language"}
    )
    String languageName;
    @CommandLine.Option(
            names = {"-e", "--extension"}
    )
    String fileExt;
    @CommandLine.Option(
            names = {"-j", "--jarPath"}
    )
    String jarPath;

    @CommandLine.Option(
            names = {"-g", "--generators"},
            split = ":"
    )
    String[] templateGeneratorNames;

    @CommandLine.Option(
            names = {"-p", "--unzipPath"}
    )
    String unzipPath;


    public LspClientGenerator() {

    }


    private Map<SyntaxElement, List<Symbol>> mapOfSyntaxElement(Map<String, List<Symbol>> categories) {
//        Map<SyntaxElement, List<Symbol>> map = new HashMap<>();
//        categories.forEach((k, v) -> map.put(SyntaxElement.valueOf(k.toUpperCase()), v));
//        return map;
        Map<SyntaxElement, List<Symbol>> map = new HashMap<>();
                var values = Arrays.stream(SyntaxElement.values()).map(Enum::name).collect(Collectors.toSet());
                categories.entrySet().stream().filter(e -> values.contains(e.getKey().toUpperCase())).forEach(e -> map.put(SyntaxElement.valueOf(e.getKey().toUpperCase()), e.getValue()));
                return map;
    }


    private Map<String, List<Symbol>> mergeMapElements(Stream<Map<String, List<Symbol>>> categories) {
        Map<String, List<Symbol>> merged = new HashMap<>();
        categories.forEach(e -> e.forEach((k, v) -> {
            if (merged.containsKey(k)) {
                merged.get(k).addAll(v);
            } else {
                merged.put(k, new ArrayList<>(v));
            }
        }));
        return merged;
    }

    private Map<String, List<Symbol>> mergedCategories() {;

        String classNameWithPackage = languageName; // languageName.toLowerCase() + "." + languageName;
        System.out.println(classNameWithPackage);
        Language clazz;
        Map<String, List<Symbol>> merged = new HashMap<>();
        try {
            clazz = (Language) Class.forName(classNameWithPackage).getConstructor().newInstance();
            clazz.loadLanguage();
            merged = mergeMapElements(clazz.getSlices().values().stream().map(e -> e.getSyntaxRole().getDeclaredCategories()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return merged;
    }

    @Override
    public Integer call() {

        try {
            TemplateGenerator templateGenerator;
            for (var generatorName : templateGeneratorNames) {
                templateGenerator = (TemplateGenerator) Class.forName(generatorName).getConstructor().newInstance();
                templateGenerator.accept(languageName, fileExt, jarPath, unzipPath)
                                 .generateLSP()
                                 .generateSyntaxHighlighter(mapOfSyntaxElement(mergedCategories()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new LspClientGenerator());
//        commandLine.parseArgs(args);
        int exit = commandLine.execute(args);
        System.exit(exit);
    }
}
