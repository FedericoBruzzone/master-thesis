package neverlang.core.lsp.capabilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.lsp4j.*;

public class CapabilitiesHolder {

  private final List<Capability> capabilityList;
  private final Map<Class<?>, Map.Entry<Method, Capability>> map;

  private static final String pkg = DocumentDiagnosticParams.class.getPackageName();

  public CapabilitiesHolder(List<Capability> capabilityList) {
    this.capabilityList = capabilityList;
    this.map = capabilityList.stream()
        .flatMap(e -> getMethodFromInterface(e.getClass()).map(m -> Map.entry(m, e)))
        .filter(e -> e.getKey().getParameterCount() == 1)
        .filter(e -> e.getKey().getParameterTypes()[0].getPackageName().startsWith(pkg))
        .collect(Collectors.toMap(e -> e.getKey().getParameterTypes()[0], Function.identity()));
  }

  public Stream<Capability> stream() {
    return capabilityList.stream();
  }

  private Stream<Method> getMethodFromInterface(Class<?> clazz) {
    return Arrays.stream(clazz.getInterfaces())
        .filter(Capability.class::isAssignableFrom)
        .map(Class::getDeclaredMethods)
        .flatMap(Arrays::stream);
  }

  public <RETURN> RETURN dispatch(Object object) {
    return Optional.ofNullable(map.get(object.getClass()))
        .map(entry -> {
          try {
            return (RETURN) entry.getKey().invoke(entry.getValue(), object);
          } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
          }
        })
        .orElse(null);
  }
}
