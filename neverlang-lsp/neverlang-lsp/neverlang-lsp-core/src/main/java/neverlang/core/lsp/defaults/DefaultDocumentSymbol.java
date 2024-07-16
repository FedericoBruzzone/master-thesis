package neverlang.core.lsp.defaults;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import neverlang.core.lsp.capabilities.BaseCapability;
import neverlang.core.lsp.capabilities.DocumentSymbolCapability;
import neverlang.core.lsp.utils.Conversions;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.graph.IndexNode;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.jetbrains.annotations.NotNull;

public class DefaultDocumentSymbol extends BaseCapability implements DocumentSymbolCapability {

  public Map<Class<?>, String> documentSymbolMap = new HashMap<>();

  public DefaultDocumentSymbol(Stream<String> packageNames) {
    packageNames.forEach(this::updateDocumentSymbolMap);
  }

  public DefaultDocumentSymbol(String packageName) {
    //        documentSymbolMap = AnnotationHandler.findAnnotationInMethod(packageName,
    // neverlang.core.typelang.annotations.DocumentSymbol.class, new
    // Class[]{SymbolTableEntry.class}, SymbolKind.class)
    //                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    updateDocumentSymbolMap(packageName);
  }

  private void updateDocumentSymbolMap(String packageName) {
    documentSymbolMap.putAll(AnnotationHandler.findAnnotationInMethod(
            packageName,
            neverlang.core.typelang.annotations.DocumentSymbol.class,
            new Class[] {SymbolTableEntry.class},
            SymbolKind.class)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }

  @Override
  public void setCapability(ServerCapabilities serverCapabilities) {
    serverCapabilities.setDocumentSymbolProvider(true);
  }

  @Override
  public List<Either<SymbolInformation, DocumentSymbol>> getDocumentSymbol(
      DocumentSymbolParams params) {
    return getWorkspaceHandler()
        .getIndexTree(Conversions.extractPath(params).get())
        .map(this::toDocumentSymbol)
        .map(e -> e.filter(Objects::nonNull)
            .map(Either::<SymbolInformation, DocumentSymbol>forRight)
            .toList())
        .orElse(List.of());
  }

  @NotNull
  private Stream<DocumentSymbol> toDocumentSymbol(IndexNode entry) {
    return getDocumentSymbol(entry).map(Stream::of).orElseGet(() -> entry.children().stream()
        .flatMap(this::toDocumentSymbol));
  }

  public Optional<DocumentSymbol> getDocumentSymbol(IndexNode indexNode) {
    if (indexNode.index() instanceof SymbolTableEntry symbolTableEntry) {
      var cls = symbolTableEntry.type().getClass();
      return Optional.ofNullable(documentSymbolMap.get(cls)).map(methodName -> {
        try {
          var method = cls.getMethod(methodName, SymbolTableEntry.class);
          SymbolKind symbolKind =
              (SymbolKind) method.invoke(symbolTableEntry.type(), symbolTableEntry);
          if (symbolKind == null) return null;
          return new DocumentSymbol(
              symbolTableEntry.entryType().token().text(),
              symbolKind,
              Conversions.toRange(symbolTableEntry.range()),
              Conversions.toRange(symbolTableEntry.selectionRange()),
              "",
              indexNode.children().stream().flatMap(this::toDocumentSymbol).toList());
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
          return null;
        }
      });
    } else {
      return Optional.empty();
    }
  }
}
