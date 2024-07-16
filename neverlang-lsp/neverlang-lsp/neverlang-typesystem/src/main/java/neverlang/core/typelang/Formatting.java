package neverlang.core.typelang;

import java.lang.reflect.Method;
import java.util.List;
import neverlang.runtime.ASTNode;
import org.jetbrains.annotations.Nullable;

public interface Formatting {
  default String attr() {
    return "Text";
  }

  void eval(ASTNode to, ASTNode from);

  void catchEval(ASTNode to, ASTNode from);

  void catchStmt(ASTNode to, @Nullable Integer exception, String txt, String txt2);

  void withIdentifier(ASTNode to, String str, boolean isFile);

  void withToken(ASTNode to, int n);

  void withFoldingRange(ASTNode to, int n1, int n2);

  void getTaskBuilder(ASTNode to, boolean isRoot, int root, int astNodes, int priority, int then);

  void getScopeDefinition(
      ASTNode to,
      String entry,
      Integer type,
      Integer scopeId,
      Integer inside,
      Integer attributes,
      Integer block);

  void currentScopeTask(ASTNode to, int id, int block);

  String getPriority(ASTNode node);

  String getEntryKind(String str);

  String withType(ASTNode node);

  String withSignature(ASTNode node);

  String withVariance(ASTNode node);

  void check(ASTNode to, int token, int type, int variance, int type2);

  void enterScope(ASTNode node);

  void exitScope(ASTNode node);

  void getTypeDefinition(
      ASTNode to,
      String kind,
      Integer type,
      Integer scopeId,
      Integer inside,
      Integer attributes,
      @Nullable Integer then);

  void getImport(ASTNode to, int type, int token);

  void initRoot(ASTNode node, int priority);

  default void readAttribute(ASTNode to, ASTNode from, String attribute) {
    readAttribute(to, from, attribute, null);
  }

  void readAttribute(ASTNode to, ASTNode from, String attribute, @Nullable String classToCast);

  void getTypeFromSymbolTableEntry(ASTNode to, int type);

  void getInferType(
      ASTNode $n, String kind, int signature, int nonTerminal, int from, int with, int assignTo);

  void buildEntryType(ASTNode to, int n, boolean isArray);

  void buildSignatureParamType(ASTNode to, int n);

  void childAttributeWriteFormat(ASTNode to, int node, String defaultAttribute);

  void extractAttribute(ASTNode to);

  void withAttribute(ASTNode to, int attribute, int nt);

  void resolve(ASTNode to, int first, int snd);

  String collectAll(List<String> stream, boolean init);

  void callback(ASTNode to, int validation);

  Method withCallback(ASTNode node);

  String symbolTableEntry();

  void tokenFromIdentifier(ASTNode to, int identifier);
}
