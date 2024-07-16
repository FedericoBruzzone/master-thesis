package neverlang.lsp.clients;

import neverlang.parser.symbols.Symbol;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface TemplateGenerator {
    TemplateGenerator accept(String name, String ext, String path, String unzipPath);
    TemplateGenerator generateLSP() throws Exception;
    void generateSyntaxHighlighter(Map<SyntaxElement, List<Symbol>> m) throws Exception;

    default List<String> toListOfString(List<Symbol> l) {
        if (l == null) {
            return List.of();
        }
        return l.stream()
                .map(Symbol::getSymbolIdentifier)
                .toList();
    }

    default String escapeCharacters(String s, List<Character> chars) {
        for (Character c : chars) {
            s = s.replace(c.toString(), "\\" + c);
        }
        return s;
    }

}
