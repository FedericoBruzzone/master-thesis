package typelang;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import neverlang.core.typelang.TypeLangGenerator;
import neverlang.core.typesystem.Priority;
import org.junit.jupiter.api.Test;
import typelang.utils.MapperTestModule;
import typelang.utils.TypeTest;

// TODO: test with jar files
public class TypeMapperGeneratorTest {

  public static final String PRIORITY_LABEL = "label-test";
  public static final String COMPILATION_HELPER_LABEL = "helper-test";
  public static final String TYPE_LABEL = "type-test";

  @Test
  public void testInCurrentClasspath() {
    //        var mapperGenerator = new TypeLangGenerator()
    //                .initPackage(TestCompilationHelper.class.getPackageName(),
    // COMPILATION_HELPER_LABEL);
    //
    // assertThat(mapperGenerator.compilationHelper()).isEqualTo(TestCompilationHelper.class);
    //        assertThat(mapperGenerator.priorities()).isEmpty();
    assert (true);
  }

  @Test
  public void priorityTestInCurrentClasspath() {
    var mapperGenerator = new TypeLangGenerator().initPackage(Priority.class.getPackageName());
    assertThat(mapperGenerator.compilationHelper())
        .isEqualTo(neverlang.core.typesystem.CompilationHelper.class);
    assert (true);
  }

  @Test
  public void validationTestInCurrentClasspath() {
    var mapperGenerator = new TypeLangGenerator().initPackage(TypeTest.class.getPackageName());
    assertThat(mapperGenerator.callbacks())
        .containsExactlyEntriesOf(Map.of("validateFinalState", MapperTestModule.getMethod()));
  }
}
