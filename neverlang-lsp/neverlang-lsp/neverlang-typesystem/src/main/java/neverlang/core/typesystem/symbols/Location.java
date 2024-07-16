package neverlang.core.typesystem.symbols;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import neverlang.parser.symbols.TerminalSym;
import neverlang.runtime.ASTNode;
import neverlang.runtime.Context;
import org.jetbrains.annotations.Nullable;

public record Location(@Nullable URI uri, @Nullable Range range) {

  public Optional<String> normalizedUri() {
    return Optional.ofNullable(uri).map(Path::of).map(Path::normalize).map(Path::toString);
  }

  public static Location fromASTNode(ASTNode astNode) {
    return fromASTNode(astNode.getFile(), Range.fromNeverlangToken(astNode.getToken()));
  }

  public static Location fromASTNode(File file, Range range) {
    return new Location(file != null ? file.toURI() : null, range);
  }

  // TODO: In future this information should be stored in the ASTNode
  public static Optional<Location> of(Context $ctx, ASTNode... astNodes) {
    var finalRange = Arrays.stream(astNodes)
        .flatMap(ASTNode::preorder)
        .filter(e -> e.getSymbol() instanceof TerminalSym)
        .map(ASTNode::getToken)
        .map(Range::fromNeverlangToken)
        .filter(e -> e.stream().allMatch(r -> r >= 0))
        .reduce(Range::merge);
    var uri = Optional.ofNullable($ctx.file()).map(File::toURI).orElse(null);
    return finalRange.map(e -> new Location(uri, e));
  }

  public Location merge(Location target) {
    if (Objects.equals(this.uri, target.uri)) {
      return new Location(
          this.uri, this.range != null ? this.range.merge(target.range) : target.range);
    } else {
      throw new RuntimeException();
    }
  }

  @Override
  public String toString() {
    return String.format("%s%s", uri == null ? "" : (uri + ":"), range);
  }

  public Path toPath() {
    return uri != null ? Path.of(uri) : null;
  }
}
