package neverlang.core.lsp.defaults;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import neverlang.core.lsp.capabilities.BaseCapability;
import neverlang.core.lsp.capabilities.HoverCapability;
import neverlang.core.lsp.utils.Conversions;
import neverlang.core.typesystem.SymbolTableEntry;
import org.eclipse.lsp4j.*;

public class DefaultHover extends BaseCapability implements HoverCapability {

  public final Map<Class<?>, String> hoverMap = new HashMap<>();

  public DefaultHover(Stream<String> packageNames) {
    packageNames.forEach(this::updateHoverMap);
  }

  public DefaultHover(String packageName) {
    updateHoverMap(packageName);
  }

  private void updateHoverMap(String packageName) {
    hoverMap.putAll(AnnotationHandler.findAnnotationInMethod(
            packageName,
            neverlang.core.typelang.annotations.Hover.class,
            new Class[] {SymbolTableEntry.class},
            Hover.class)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }

  @Override
  public void setCapability(ServerCapabilities serverCapabilities) {
    serverCapabilities.setHoverProvider(true);
  }

  @Override
  public Hover getHover(HoverParams params) {
    return getWorkspaceHandler()
        .<SymbolTableEntry>lookupToken(
            Conversions.extractPath(params).get(),
            params.getPosition().getLine(),
            params.getPosition().getCharacter())
        .flatMap(this::toHover)
        .orElse(null);
  }

  private Optional<Hover> toHover(SymbolTableEntry entry) {
    var cls = entry.type().getClass();
    return Optional.ofNullable(hoverMap.get(cls)).map(methodName -> {
      try {
        var method = cls.getMethod(methodName, SymbolTableEntry.class);
        return (Hover) method.invoke(entry.type(), entry);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        return null;
      }
    });
  }
}
