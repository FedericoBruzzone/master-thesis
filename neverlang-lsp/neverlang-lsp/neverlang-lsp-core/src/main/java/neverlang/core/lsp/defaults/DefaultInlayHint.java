package neverlang.core.lsp.defaults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import neverlang.core.lsp.capabilities.BaseCapability;
import neverlang.core.lsp.capabilities.InlayHintCapability;
import neverlang.core.lsp.capabilities.Subscribable;
import neverlang.core.lsp.utils.Conversions;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.compiler.CompilationEndEvent;
import neverlang.core.typesystem.graph.IndexNode;
import org.eclipse.lsp4j.InlayHint;
import org.eclipse.lsp4j.InlayHintParams;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.services.LanguageClient;

public class DefaultInlayHint extends BaseCapability implements InlayHintCapability, Subscribable {

  public Map<Class<?>, String> inlayHintMap = new HashMap<>();

  public DefaultInlayHint(String packageName) {
    //        inlayHintMap = AnnotationHandler.findAnnotationInMethod(packageName,
    // neverlang.core.typelang.annotations.InlayHint.class, new Class[]{SymbolTableEntry.class},
    // InlayHint.class)
    //                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    updateInlayHintsMap(packageName);
  }

  public DefaultInlayHint(Stream<String> packageNames) {
    packageNames.forEach(this::updateInlayHintsMap);
  }

  private void updateInlayHintsMap(String packageName) {
    inlayHintMap.putAll(AnnotationHandler.findAnnotationInMethod(
            packageName,
            neverlang.core.typelang.annotations.InlayHint.class,
            new Class[] {SymbolTableEntry.class},
            InlayHint.class)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }

  @Override
  public void setCapability(ServerCapabilities serverCapabilities) {
    serverCapabilities.setInlayHintProvider(true);
  }

  @Override
  public List<InlayHint> getInlayHints(InlayHintParams inlayHintParams) {
    return getWorkspaceHandler()
        .getIndexTree(Conversions.extractPath(inlayHintParams).get())
        .stream()
        .flatMap(IndexNode::streamAll)
        .flatMap(e -> AnnotationHandler.<InlayHint>invokeMethod(e, inlayHintMap).stream())
        .toList();
  }

  @Override
  public InlayHint resolveInlayHint(InlayHint unresolved) {
    return unresolved;
  }

  @Override
  public Flow.Subscriber<Object> getSubscriber() {
    return new CompilationSubscriber(
        e -> e instanceof CompilationEndEvent,
        () -> getLanguageServer().getClient().ifPresent(LanguageClient::refreshInlayHints));
  }
}
