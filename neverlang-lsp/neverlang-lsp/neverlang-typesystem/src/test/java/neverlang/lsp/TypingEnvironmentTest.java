package neverlang.lsp;

import static org.assertj.core.api.Assertions.assertThat;

import neverlang.core.typesystem.defaults.DefaultSymbolTableEntry;
import neverlang.core.typesystem.defaults.SingleTypeTypeBinder;
import neverlang.core.typesystem.symbols.Token;
import neverlang.core.typesystem.typenv.TypingEnvironment;
import neverlang.lsp.defaults.TypePrimitive;
import neverlang.lsp.defaults.VariableSignature;
import org.junit.jupiter.api.Test;

public class TypingEnvironmentTest {

  @Test
  public void bindingTest() {
    // We are creating a simple typing environment with a single type
    var typingEnvironment = new TypingEnvironment.Builder<String>()
        // SingleTypeTypeBinder means that a variable can only be bound to a single
        // type
        .setTypeBinder(SingleTypeTypeBinder.class)
        .build();
    // We are creating a new type primitive called "int"
    var intType = new TypePrimitive("int");
    // Typing environment is designed to be used also as symbol table so
    // we are creating a new symbol table entry with the type "int" and the token "X"
    var intEntry = new DefaultSymbolTableEntry(intType, Token.of("X"));
    // We are binding the type "int" to the identifier "X"
    typingEnvironment.bindTypeToIdentifier("X", intEntry);
    // We are looking for a variable, so we use a VariableSignature
    var signature = new VariableSignature();
    // We are getting the first type bound to the identifier "X"
    // that matches the signature
    var lookupResult = typingEnvironment
        .getTypesBoundedWith("X")
        .filter(e -> e.type().matchSignature(signature))
        .findFirst();
    // We should have a type "int" bounded to the identifier "X"
    assertThat(lookupResult).isPresent();
    // Given the type and the signature we should be able to infer the type
    var inferredType = signature.typeResolution(lookupResult.get()).type();
    // We should have a type "int"
    assertThat(inferredType).isEqualTo(intType);
  }
}
