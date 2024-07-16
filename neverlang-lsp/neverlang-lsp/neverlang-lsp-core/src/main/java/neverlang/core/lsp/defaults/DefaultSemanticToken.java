package neverlang.core.lsp.defaults;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import neverlang.core.lsp.capabilities.BaseCapability;
import neverlang.core.lsp.capabilities.SemanticTokenCapability;
import neverlang.core.lsp.capabilities.Subscribable;
import neverlang.core.lsp.utils.Conversions;
import neverlang.core.typelang.annotations.SemanticToken;
import neverlang.core.typelang.annotations.SemanticTokenModifier;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.compiler.CompilationEndEvent;
import neverlang.core.typesystem.graph.IndexNode;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.LanguageClient;

public class DefaultSemanticToken extends BaseCapability
    implements Subscribable, SemanticTokenCapability {

  private final Map<String, Integer> semanticTokens = new HashMap<>();
  private final Map<String, Integer> semanticTokenModifiers = new HashMap<>();
  private Map<Class<?>, String> semanticTokenMap = new HashMap<>();
  private Map<Class<?>, String> semanticTokenModifierMap = new HashMap<>();

  private final Class<?>[] parametersTypes = new Class[] {SymbolTableEntry.class};

  public DefaultSemanticToken(Stream<String> packageNames) {
    AtomicInteger counter = new AtomicInteger(0);
    AtomicInteger counter2 = new AtomicInteger(1);
    packageNames.forEach(e -> {
      updateSemanticTokenMap(e, counter);
      updateSemanticTokenModifierMap(e, counter2);
    });
  }

  public DefaultSemanticToken(String packageName) {
    //        AtomicInteger counter = new AtomicInteger(0);
    //        Class<?> returnType = String.class;
    //        semanticTokenMap = AnnotationHandler.findAnnotationInMethod(packageName,
    // SemanticToken.class,parametersTypes , returnType)
    //                .peek(e -> {
    //                    try {
    //                        String[] values = e.getKey().getMethod(e.getValue(),
    // parametersTypes).getAnnotation(SemanticToken.class).value();
    //                        Arrays.stream(values).filter(str ->
    // !semanticTokens.containsKey(str)).forEach(str -> semanticTokens.put(str,
    // counter.getAndIncrement()));
    //                    } catch (NoSuchMethodException ex) {
    //                        throw new RuntimeException(ex);
    //                    }
    //                })
    //                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    //
    //        // Start from 1 because `semanticTokenModifiers` modifier start from 1
    //        counter.set(1);
    //        semanticTokenModifierMap = AnnotationHandler.findAnnotationInMethod(packageName,
    // SemanticTokenModifier.class, parametersTypes, returnType)
    //                .peek(e -> {
    //                    try {
    //                        String[] values =
    // e.getKey().getMethod(e.getValue(),parametersTypes).getAnnotation(SemanticTokenModifier.class).value();
    //                        Arrays.stream(values).filter(str ->
    // !semanticTokenModifiers.containsKey(str)).forEach(str -> semanticTokenModifiers.put(str,
    // counter.getAndIncrement()));
    //                    } catch (NoSuchMethodException ex) {
    //                        throw new RuntimeException(ex);
    //                    }
    //                })
    //                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    updateSemanticTokenMap(packageName, new AtomicInteger(0));
    updateSemanticTokenModifierMap(packageName, new AtomicInteger(1));
  }

  private void updateSemanticTokenMap(String packageName, AtomicInteger counter) {
    semanticTokenMap.putAll(AnnotationHandler.findAnnotationInMethod(
            packageName, SemanticToken.class, parametersTypes, String.class)
        .peek(e -> {
          try {
            String[] values = e.getKey()
                .getMethod(e.getValue(), parametersTypes)
                .getAnnotation(SemanticToken.class)
                .value();
            Arrays.stream(values)
                .filter(str -> !semanticTokens.containsKey(str))
                .forEach(str -> semanticTokens.put(str, counter.getAndIncrement()));
          } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
          }
        })
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }

  private void updateSemanticTokenModifierMap(String packageName, AtomicInteger counter) {
    semanticTokenModifierMap.putAll(AnnotationHandler.findAnnotationInMethod(
            packageName, SemanticTokenModifier.class, parametersTypes, String.class)
        .peek(e -> {
          try {
            String[] values = e.getKey()
                .getMethod(e.getValue(), parametersTypes)
                .getAnnotation(SemanticTokenModifier.class)
                .value();
            Arrays.stream(values)
                .filter(str -> !semanticTokenModifiers.containsKey(str))
                .forEach(str -> semanticTokenModifiers.put(str, counter.getAndIncrement()));
          } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
          }
        })
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }

  @Override
  public void setCapability(ServerCapabilities serverCapabilities) {
    serverCapabilities.setSemanticTokensProvider(new SemanticTokensWithRegistrationOptions(
        new SemanticTokensLegend(semanticTokenList(), semanticTokenModifierList()), true));
  }

  @Override
  public Flow.Subscriber<Object> getSubscriber() {
    return new CompilationSubscriber(
        e -> e instanceof CompilationEndEvent,
        () -> getLanguageServer().getClient().ifPresent(LanguageClient::refreshSemanticTokens));
  }

  public List<String> semanticTokenList() {
    return sortedMap(semanticTokens);
  }

  public List<String> semanticTokenModifierList() {
    return sortedMap(semanticTokenModifiers);
  }

  private List<String> sortedMap(Map<String, Integer> map) {
    return map.entrySet().stream()
        .sorted(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
  }

  // line, start char, len, token type, modifier = 0
  @Override
  public SemanticTokens semanticTokensFull(SemanticTokensParams params) {
    var workspaceHandler = getWorkspaceHandler();
    var res = new SemanticTokens();
    AtomicReference<Integer[]> last = new AtomicReference<>(new Integer[] {0, 0, 0, 0, 0});
    res.setData(workspaceHandler.getIndexTree(Conversions.extractPath(params).get()).stream()
        .flatMap(IndexNode::streamAll)
        .map(IndexNode::index)
        .filter(Objects::nonNull)
        .map(e -> (SymbolTableEntry) e)
        .filter(e -> e.selectionRange() != null && e.type() != null)
        .filter(e -> semanticTokenMap.containsKey(e.type().getClass()))
        .map(e -> new Integer[] {
          Objects.requireNonNull(e.selectionRange()).startRow(),
          Objects.requireNonNull(e.selectionRange()).startCol(),
          e.length(),
          getIndex(semanticTokenMap, semanticTokens, e).orElse(-1),
          getIndex(semanticTokenModifierMap, semanticTokenModifiers, e).orElse(0)
        })
        .map(e -> relativize(e, last.getAndSet(e)))
        .flatMap(Arrays::stream)
        .toList());
    return res;
  }

  private Integer[] relativize(Integer[] e, Integer[] integers) {
    return new Integer[] {
      e[0] - integers[0], e[0].equals(integers[0]) ? e[1] - integers[1] : e[1], e[2], e[3], e[4]
    };
  }

  public Optional<Integer> getIndex(
      Map<Class<?>, String> map, Map<String, Integer> indexMap, SymbolTableEntry symbolTableEntry) {
    var cls = symbolTableEntry.type().getClass();
    return Optional.ofNullable(map.get(cls)).flatMap(e -> {
      try {
        String res = (String)
            cls.getMethod(e, parametersTypes).invoke(symbolTableEntry.type(), symbolTableEntry);
        return Optional.ofNullable(indexMap.get(res));
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
        return Optional.empty();
      }
    });
  }

  @Override
  public Either<SemanticTokens, SemanticTokensDelta> semanticTokensFullDelta(
      SemanticTokensDeltaParams params) {
    return null;
  }

  @Override
  public SemanticTokens semanticTokensRange(SemanticTokensRangeParams params) {
    return null;
  }
}
