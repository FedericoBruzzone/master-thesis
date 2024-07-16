package simplelang;

import static org.assertj.core.api.Assertions.assertThat;

import neverlang.core.lsp.defaults.signatures.FunctionSignature;
import neverlang.core.lsp.defaults.symboltable.CompilationHelper;
import neverlang.core.lsp.defaults.types.TypeFile;
import neverlang.core.lsp.defaults.types.TypeUnresolved;
import neverlang.core.typesystem.InferencingStrategy;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.defaults.DefaultEntryType;
import neverlang.core.typesystem.defaults.FindFirstInferencingStrategy;
import neverlang.core.typesystem.symbols.Token;
import neverlang.core.typesystem.typenv.EntryType;
import org.junit.jupiter.api.*;
import simplelang.typesystem.BaseLang;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InferenceTest {

  private static SymbolTableEntry functionType;

  private static final InferencingStrategy inferencingStrategy = new FindFirstInferencingStrategy();

  @BeforeAll
  public static void setUp() {
    BaseLang.compilationHelper = new CompilationHelper();

    var file = new TypeFile();
    BaseLang.bindBaseLang(file);
    var res = file.inferFromSignature(Token.of("=="), new FunctionSignature(new EntryType[] {
      new DefaultEntryType(Token.of("int"), BaseLang.intType),
      new DefaultEntryType(Token.of("int"), BaseLang.intType)
    }));
    functionType = inferencingStrategy.infer(res);
  }

  @Test
  @Order(0)
  public void testReturnType() {
    assertThat(functionType).isNotNull();
  }

  @Test
  @Order(1)
  public void testFunctionParameterInference() {
    var file = new TypeFile();
    BaseLang.bindBaseLang(file);
    var res = file.inferFromSignature(Token.of("=="), new FunctionSignature(new EntryType[] {
      new DefaultEntryType(Token.of("unknow"), new TypeUnresolved()),
      new DefaultEntryType(Token.of("int"), BaseLang.intType)
    }));
    assertThat(inferencingStrategy.infer(res)).isEqualTo(functionType);
  }

  @Test
  @Order(2)
  public void failedInference() {
    var file = new TypeFile();
    BaseLang.bindBaseLang(file);
    var res = file.inferFromSignature(Token.of("=="), new FunctionSignature(new EntryType[] {
      new DefaultEntryType(Token.of("unknow"), new TypeUnresolved()),
      new DefaultEntryType(Token.of("unknow"), new TypeUnresolved())
    }));
    assertThat(inferencingStrategy.infer(res)).isEqualTo(functionType);
  }
}
