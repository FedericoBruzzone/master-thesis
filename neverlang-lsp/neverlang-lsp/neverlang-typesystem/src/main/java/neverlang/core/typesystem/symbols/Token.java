package neverlang.core.typesystem.symbols;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import neverlang.parser.symbols.TerminalSym;
import neverlang.runtime.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Token(@NotNull String text, @Nullable Location location) {

  public Token(String text) {
    this(text, null);
  }

  public static Token of(String text, Location location) {
    return new Token(text, location);
  }

  public static Token of(String text) {
    return Token.of(text, 0, 0);
  }

  public static Token of(String text, int startRow, int startCol) {
    return Token.of(text, null, startRow, startCol);
  }

  public static Token of(String text, @Nullable URI uri, int startRow, int startCol) {
    return new Token(text, new Location(uri, Range.fromText(text, startRow, startCol)));
  }

  public static Token of(@NotNull URI uri) {
    return new Token(uri.toString(), new Location(uri, null));
  }

  public static Token fromASTNode(ASTNode astNode) {
    if (astNode.getSymbol() instanceof TerminalSym) {
      return convertToToken(getNeverlangToken(Stream.of(astNode)));
    } else {
      return convertToToken(getNeverlangToken(astNode.preorder()));
    }
  }

  public static Token fromNTASTNode(ASTNode astNode, int position) {
    return convertToToken(getNeverlangToken(astNode.ntchild(position).preorder()));
  }

  private static Token convertToToken(ASTNode astNode) {
    var text = astNode.getToken().getText();
    assert text != null;
    return new Token(text, Location.fromASTNode(astNode));
  }

  @NotNull
  private static ASTNode getNeverlangToken(Stream<ASTNode> astNodeStream) {
    return astNodeStream
        .filter(e -> e.getToken() != null && e.getToken().getText() != null)
        .findFirst()
        .orElseThrow(RuntimeException::new);
  }

  public Token withText(String text) {
    return new Token(text, location());
  }

  public Token withPrefix(String prefix) {
    return new Token(prefix + text(), location());
  }

  public static Token fromASTNode(ASTNode astNode, int position) {
    return Token.fromASTNode(astNode.tchild(position));
  }

  public static Token fromASTNode(ASTNode astNode, int position, int groupPosition) {
    var child = astNode.tchild(position);
    var astN = getNeverlangToken(Stream.of(child));
    return new Token(astN.getToken().matches.group(groupPosition), Location.fromASTNode(astN));
  }

  public static Token join(ASTNode astNode, int... positions) {
    return Token.join(astNode, "", positions);
  }

  public static Token join(ASTNode astNode, String join, int... positions) {
    return Token.join(
        Arrays.stream(positions).mapToObj(e -> Token.fromASTNode(astNode, e)).toList(), join);
  }

  // TODO: check ASTNode.toTerminalString() for a better implementation
  public static Token join(List<Token> tokenList, String join) {
    var text = tokenList.stream().map(Token::text).collect(Collectors.joining(join));
    var locationStart = tokenList.get(0).location;
    var locationEnd = tokenList.get(tokenList.size() - 1).location;
    return new Token(text, locationStart.merge(locationEnd));
  }

  public static Token join(Token... tokenList) {
    return Token.join(Arrays.stream(tokenList).filter(Objects::nonNull).toList(), "");
  }

  @Nullable
  public Range range() {
    return location() == null ? null : location().range();
  }

  public boolean isContainedIn(Token token) {
    return token.range() != null && token.range().contains(range());
  }

  public boolean isContainedIn(Range range) {
    return range.contains(range());
  }
}
