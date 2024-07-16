package neverlanglsp.astvisualizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import neverlang.compiler.NeverlangLang;
import neverlang.parser.symbols.TerminalSym;
import neverlang.runtime.ASTNode;
import neverlanglsp.astvisualizer.cytoscape.Edge;
import neverlanglsp.astvisualizer.cytoscape.Elements;
import neverlanglsp.astvisualizer.cytoscape.Node;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.services.TextDocumentService;

public class NeverlangASTNodeVisualizerDocumentService implements TextDocumentService {
  private final SimpleServer webSocketServer;
  private final NeverlangLang lang;

  public NeverlangASTNodeVisualizerDocumentService(SimpleServer webSocketServer) {
    this.webSocketServer = webSocketServer;
    this.lang = new NeverlangLang();
  }

  @Override
  public void didSave(DidSaveTextDocumentParams params) {
    try {
      var uri = new URI(params.getTextDocument().getUri());
      parseASTNode(uri);
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void didOpen(DidOpenTextDocumentParams params) {
    try {
      var uri = new URI(params.getTextDocument().getUri());
      parseASTNode(uri);
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void didChange(DidChangeTextDocumentParams params) {}

  @Override
  public void didClose(DidCloseTextDocumentParams params) {}

  private void parseASTNode(URI uri) throws IOException {
    var source = Files.readString(Path.of(uri));
    ASTNode astNode = lang.parse(source);
    Set<Node> nodes = new HashSet<>();
    List<Edge> edges = new ArrayList<>();
    astNode.preorder().forEachOrdered(node -> {
      var str = node.getSymbol().getSymbolIdentifier();
      var child = new Node(
          String.valueOf(node.getId()),
          str.substring(0, Math.min(str.length(), 32)),
          node.getSymbol() instanceof TerminalSym);
      nodes.add(child);
      Optional.ofNullable(node.getParent()).ifPresent(p -> {
        var strp = p.getSymbol().getSymbolIdentifier();
        var parent = new Node(
            String.valueOf(p.getId()),
            strp.substring(0, Math.min(strp.length(), 32)),
            p.getSymbol() instanceof TerminalSym);
        nodes.add(parent);
        var edge = new Edge(parent, child, p.getChildPos(node));
        edges.add(edge);
      });
    });
    ObjectMapper objectMapper = new ObjectMapper();
    var elements = Elements.fromTreeElements(nodes.stream().toList(), edges);
    String toSend = objectMapper.writeValueAsString(elements);
    webSocketServer.broadcast(toSend);
  }
}
