package neverlang.core.typelang;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import neverlang.compiler.plugins.translator.JavaTranslatorPlugin;
import neverlang.core.typesystem.NeverlangTypesystemException;
import neverlang.core.typesystem.TypeMismatchException;
import neverlang.runtime.ASTNode;
import neverlang.runtime.UndefinedAttributeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// Should not extend a JavaTranslatorPlugin
public class TypeLangFormatting extends JavaTranslatorPlugin implements Formatting {

  public static final String ATTRIBUTE_ATTRIBUTE = "Attribute";

  @Named(TypeMapperModule.variance)
  @Inject
  private Map<String, String> varianceMap;

  @Named(TypeMapperModule.priorities)
  @Inject
  public Map<String, String> prioritiesMap;

  @Named(TypeMapperModule.entries)
  @Inject
  public Map<String, String> entriesMap;

  @Named(TypeMapperModule.signatures)
  @Inject
  public Map<String, String> signatureMap;

  @Named(TypeMapperModule.types)
  @Inject
  public Map<String, String> typesMap;

  @Named(TypeMapperModule.details)
  @Inject
  public Map<String, String> attributes;

  @Named(TypeMapperModule.symbolTableEntry)
  @Inject
  public String symbolTableEntry;

  @Named(TypeMapperModule.callbacks)
  @Inject
  public Map<String, Method> validationMap;

  @Named(TypeMapperModule.symbolTableEntryFactory)
  @Inject
  public String symbolTableEntryFactory;

  @Named(TypeMapperModule.compilationHelper)
  @Inject
  public String compilationHelper;

  @Named(TypeMapperModule.compilationUnit)
  @Inject
  public String compilationUnit;

  String typeAttr = "type";

  private final String unitId = "unit";
  private final String helperID = "helper";
  private final NamingHelper namingHelper = new NamingHelper();

  String catchFormat = "try {\n%s\n} catch (%s e) {\n%s\n}";
  String submitFormat = "e.submit(%s);";

  private String extractFrom(ASTNode node, Map<String, String> map) {
    return extractFrom(node, map, Function.identity());
  }

  private <T, R> R extractFrom(ASTNode node, Map<String, T> map, Function<T, R> extractor) {
    return node.streamChildren()
        .findFirst()
        .map(e -> extractor.apply(map.get(e.getToken().getText())))
        .orElseThrow(() ->
            new RuntimeException("Unable to find this keyword in " + node.toShortString(128)));
  }

  @Nullable
  @Override
  public String getPriority(ASTNode node) {
    return extractFrom(node, prioritiesMap);
  }

  @Nullable
  public String getEntryKind(String str) {
    return Optional.ofNullable(entriesMap.get(str))
        .orElseThrow(() -> new RuntimeException("Unable to find this keyword in " + str));
  }

  @Override
  public void callback(ASTNode to, int validation) {
    namingHelper.hasHelper();
    var method = to.ntchild(validation).<Method>getValue("method");
    var res = "%s.<%s>type().%s(%s, %s);"
        .formatted(
            "%s", method.getDeclaringClass().getCanonicalName(), method.getName(), "%s", helperID);
    ;
    to.setValue(attr(), res);
  }

  @Override
  public Method withCallback(ASTNode node) {
    return extractFrom(node, validationMap, Function.identity());
  }

  @Override
  public String symbolTableEntry() {
    return symbolTableEntry;
  }

  @Override
  public void tokenFromIdentifier(ASTNode to, int identifier) {
    var id = to.ntchild(identifier).getValue("Text");
    to.setValue(attr(), "Token.of(\"%s\")".formatted(id));
  }

  @Nullable
  public String withType(ASTNode node) {
    try {
      return node.<String>getValue("Text");
    } catch (UndefinedAttributeException e) {
      return String.format("new %s()", extractFrom(node, typesMap));
    }
  }

  @Override
  public String withSignature(ASTNode astNode) {
    return extractFrom(astNode, signatureMap);
  }

  @Override
  public String withVariance(ASTNode node) {
    return extractFrom(node, varianceMap);
  }

  @Override
  public void check(ASTNode to, int token, int type, int variance, int type2) {
    StringBuilder sb = new StringBuilder();
    sb.append("if(!");
    sb.append(String.format(
        "%s.isAssignableFrom(%s,%s)",
        to.ntchild(type).getValue("Text"),
        to.ntchild(type2).getValue("Text"),
        to.ntchild(variance).getValue("Text")));
    sb.append("){").append("\n");
    sb.append("throw new ").append(TypeMismatchException.class.getCanonicalName());
    sb.append(String.format(
        "(%s,%s,%s);",
        to.ntchild(type2).getValue("Text"),
        to.ntchild(type).getValue("Text"),
        to.ntchild(token).getValue("Text")));
    sb.append("\n}\n");
    to.setValue(attr(), sb.toString());
  }

  @Override
  public void enterScope(ASTNode node) {
    if (!namingHelper.setUnit()) {
      throw new RuntimeException("No unit found");
    }
    node.setValue(attr(), "%s.enterScope(%s);".formatted(unitId, namingHelper.current("scope")));
  }

  @Override
  public void exitScope(ASTNode node) {
    node.setValue(attr(), "%s.exitScope();".formatted(unitId));
  }

  public String assignHelper() {
    return "var " + helperID + " = " + MessageFormat.format(this.singletonRead, compilationHelper);
  }

  public String assignUnit() {
    return "var " + unitId + " = " + MessageFormat.format(this.singletonRead, compilationUnit);
  }

  @Override
  public void eval(ASTNode to, ASTNode from) {
    String res = MessageFormat.format(
        evalStatement,
        MessageFormat.format(childReference, from.<String>lookForValue(attr()).get()));
    to.setValue(attr(), res);
  }

  @Override
  public void catchStmt(
      ASTNode to, @Nullable Integer exception, @NotNull String txt, @Nullable String txt2) {
    var exceptionName = Optional.ofNullable(exception)
        .map(e -> to.ntchild(exception))
        .flatMap(e -> e.<String>getOptValue("Text"))
        .orElseGet(NeverlangTypesystemException.class::getCanonicalName);
    var catchBranch = txt2 == null || txt2.isEmpty()
        ? submitFormat.formatted(MessageFormat.format(this.singletonRead, compilationHelper))
        : txt2;
    var value = String.format(catchFormat, txt, exceptionName, catchBranch);
    to.setValue(attr(), value);
  }

  @Override
  public void catchEval(ASTNode to, ASTNode from) {
    String res = MessageFormat.format(
        evalStatement,
        MessageFormat.format(childReference, from.<String>lookForValue(attr()).get()));
    catchStmt(to, null, res, null);
  }

  @Override
  public void withIdentifier(ASTNode to, String str, boolean isFile) {
    String res = isFile ? ".fromSource($ctx, \"%s\")" : ".withIdentifier(\"%s\")";
    to.setValue(attr(), res.formatted(str));
  }

  public String readAttribute(
      ASTNode from, String attribute, @Nullable String classToCast, boolean isArray) {
    if (classToCast == null) {
      return MessageFormat.format(
          childAttributeRead,
          from.<String>getValue(attr()),
          from.getOptValue(ATTRIBUTE_ATTRIBUTE).orElse(attribute),
          "");
    } else {
      return MessageFormat.format(
          childAttributeRead,
          from.<String>getValue(attr()),
          from.getOptValue(ATTRIBUTE_ATTRIBUTE).orElse(attribute),
          "<%s>".formatted(isArray ? classToCast + "[]" : classToCast));
    }
  }

  @Override
  public void readAttribute(
      ASTNode to, ASTNode from, String attribute, @Nullable String classToCast) {
    to.setValue(attr(), readAttribute(from, attribute, classToCast, false));
  }

  @Override
  public void withToken(ASTNode to, int n) {
    String res = MessageFormat.format(
        childAttributeRead,
        to.ntchild(n).<String>getValue(attr()),
        to.ntchild(n).getOptValue(ATTRIBUTE_ATTRIBUTE).orElse("token"),
        "");
    to.setValue(attr(), String.format(".withToken(%s)", res));
  }

  @Override
  public void withFoldingRange(ASTNode to, int n1, int n2) {
    to.setValue(
        attr(),
        String.format(
            "\n\t.withFoldingRange(Range.foldingRangeFrom($n,%s,%s))",
            to.ntchild(n1).getValue(attr()), to.ntchild(n2).getValue(attr())));
  }

  @Override
  public void getTaskBuilder(
      ASTNode to, boolean isRoot, int root, int astNodes, int priority, int then) {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("\n%s.getTaskBuilder()", helperID));
    sb.append("\n\t.withContext($ctx)");
    sb.append("%s");
    if (isRoot) {
      sb.append("\n\t.withParentCompilationUnit(helper.getRootCompilationUnit())");
    }
    sb.append(to.getOptValue(attr()).orElse(""));
    sb.append(
        String.format("\n\t.withPriority(%s)", to.ntchild(priority).<String>getValue(attr())));
    var ntList = to.ntchild(astNodes)
        .preorder()
        .filter(e -> e.getSymbol().getSymbolIdentifier().equals("NonTerminal"))
        .map(e -> e.<String>getOptValue(attr()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(e -> MessageFormat.format(this.childReference, e))
        .collect(Collectors.joining(","));
    sb.append(String.format("\n\t.withAstNodes(%s)", ntList));
    Optional.ofNullable(to.ntchild(then).<String>getOptValue(attr()))
        .flatMap(Function.identity())
        .ifPresent(e -> {
          var typeName = namingHelper.currentPlus("scope", 1);
          sb.append(
              String.format("\n\t.withCallback(() -> {%s})", e.formatted(typeName, typeName)));
        });
    sb.append("\n\t.createAndRegisterTask();");
    to.setValue(attr(), sb.toString());
  }

  public void buildArray(ASTNode to, String symbolIdentifier, String attribute, String arrayType) {
    var list = to.preorder()
        .filter(e -> e.getSymbol().getSymbolIdentifier().equals(symbolIdentifier))
        .map(e -> e.<String>getOptValue(attr()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(e -> MessageFormat.format(this.childAttributeRead, e, attribute, ""))
        .toList();
    if (list.size() == 1) {
      to.setValue(
          attr(), list.get(0).replace(".getValue", String.format(".<%s[]>getValue", arrayType)));
    } else if (list.size() > 1) {
      var joinedList = list.stream()
          .map(e -> e.replace(".getValue", String.format(".<%s>getValue", arrayType)))
          .collect(Collectors.joining(","));
      to.setValue(attr(), String.format("new %s[]{%s}", arrayType, joinedList));
    }
  }

  @Override
  public void buildEntryType(ASTNode to, int n, boolean isArray) {
    StringBuilder sb = new StringBuilder();
    if (isArray) {
      sb.append(readAttribute(to.ntchild(n), "type", symbolTableEntry, true));
    } else {
      sb.append("new %s[]{".formatted(symbolTableEntry));
      sb.append(to.ntchild(n)
          .streamSymbolList("NonTerminalList", "NonTerminal")
          .map(e -> readAttribute(e, "type", symbolTableEntry, false))
          .collect(Collectors.joining(",")));
      sb.append("}");
    }
    to.setValue(attr(), sb.toString());
  }

  @Override
  public void buildSignatureParamType(ASTNode to, int n) {
    String res = to.ntchild(n)
        .streamSymbolList("NonTerminalParamList", "WrappedNonTerminalList")
        .map(e -> e.<String>getValue(attr()))
        .collect(Collectors.joining(","));
    to.setValue(attr(), res);
  }

  @Override
  public void childAttributeWriteFormat(ASTNode to, int node, String defaultAttribute) {
    var nonTerminalNode = to.ntchild(node);
    to.setValue(
        attr(),
        MessageFormat.format(
            childAttributeWrite,
            nonTerminalNode.<String>getValue("Text"),
            nonTerminalNode.<String>getOptValue(ATTRIBUTE_ATTRIBUTE).orElse(defaultAttribute),
            "%s",
            "%s"));
  }

  @Override
  public void extractAttribute(ASTNode to) {
    to.setValue(attr(), extractFrom(to, attributes));
  }

  @Override
  public void withAttribute(ASTNode to, int attribute, int nt) {
    StringBuilder sb = new StringBuilder();
    sb.append("\n\t.");
    sb.append(to.ntchild(attribute).<String>getValue(attr()));
    sb.append("(");
    sb.append(readAttribute(to.ntchild(nt), "token", null, false));
    sb.append(")");
    to.setValue(attr(), sb.toString());
  }

  @Override
  public void resolve(ASTNode to, int first, int snd) {
    StringBuilder sb = new StringBuilder();
    sb.append(to.ntchild(first).<String>getValue(attr()));
    sb.append(".refType().set(");
    sb.append(to.ntchild(snd).<String>getValue(attr()));
    sb.append(".type());");
    ;
    to.setValue(attr(), sb.toString());
  }

  @Override
  public void getScopeDefinition(
      ASTNode to,
      String entry,
      Integer type,
      Integer scopeId,
      Integer inside,
      Integer attributes,
      Integer block) {
    StringBuilder sb = new StringBuilder();
    var typeName = namingHelper.next("type");
    sb.append("var %s = %s;\n".formatted(typeName, to.ntchild(type).<String>getValue(attr())));
    sb.append(String.format("%s %s = ", symbolTableEntry, namingHelper.next("scope")));
    bindGenericType(to, entry, typeName, scopeId, inside, attributes, sb);
    sb.append("\n\t.bindScopeOrReuse();");
    to.ntchild(block)
        .<String>getOptValue(attr())
        .ifPresent(
            e -> sb.append(e.formatted("\n\t.insideScope(" + namingHelper.current("scope") + ")")));
    sb.append("\n\n");
    to.setValue(attr(), sb.toString());
  }

  @Override
  public void currentScopeTask(ASTNode to, int id, int block) {
    var sb = new StringBuilder();
    namingHelper.setHelper();
    var str = to.ntchild(id)
        .<String>getOptValue(attr())
        .map("\n\t.withId(\"%s\")"::formatted)
        .orElse("");
    to.ntchild(block).<String>getOptValue(attr()).ifPresent(e -> sb.append(e.formatted(str)));
    to.setValue(attr(), sb.toString());
  }

  @Override
  public void getTypeDefinition(
      ASTNode to,
      String entry,
      Integer type,
      Integer scopeId,
      @Nullable Integer inside,
      @Nullable Integer attributes,
      @Nullable Integer then) {
    StringBuilder sb = new StringBuilder();
    var typeName = namingHelper.next("type");
    sb.append("var %s = %s;\n".formatted(typeName, to.ntchild(type).<String>getValue(attr())));
    bindGenericType(to, entry, typeName, scopeId, inside, attributes, sb);
    sb.append("\n\t.bind();");
    Optional.ofNullable(then)
        .flatMap(e -> to.ntchild(e).<String>getOptValue(attr()))
        .ifPresent(e -> sb.append("\n").append(e.formatted(typeName, typeName)));
    to.setValue(attr(), sb.toString());
  }

  @Override
  public void getTypeFromSymbolTableEntry(ASTNode to, int type) {
    String symbolTableEntry = to.ntchild(type).getValue(attr());
    to.setValue(attr(), symbolTableEntry + ".type()");
  }

  public void getInferType(
      ASTNode to, String entry, int signature, int nonTerminal, int from, int with, int assignTo) {
    StringBuilder sb = new StringBuilder();
    namingHelper.setUnit();
    namingHelper.setHelper();

    sb.append(String.format("Token %s = ", namingHelper.next("token")))
        .append(to.ntchild(nonTerminal).<String>getValue(attr()))
        .append(";\n");
    sb.append(String.format("var %s = new ", namingHelper.next("signature")))
        .append(to.ntchild(signature).<String>getValue(attr()))
        .append("(");
    sb.append(to.ntchild(with).<String>getOptValue(attr()).orElse(""));
    sb.append(")").append(";\n");

    var fromOptional = to.ntchild(from)
        .<Optional<String>>getValue("Text")
        .map(e -> "%s.generateCompilationUnit(%s)\n\t".formatted(helperID, e))
        .orElse(unitId);
    Optional<String> assignToOp = to.ntchild(assignTo).getOptValue(attr());
    sb.append("var %s = %s.typeInference(%s,%s);"
        .formatted(
            namingHelper.next("entryType"),
            fromOptional,
            namingHelper.current("token"),
            namingHelper.current("signature")));
    sb.append(String.format("\nnew %s()", symbolTableEntryFactory));
    sb.append("\n\t.withCompilationUnit(%s)".formatted(unitId));
    sb.append("\n\t.withCompilationHelper(%s)".formatted(helperID));
    sb.append("\n\t.withEntryType(%s)".formatted(namingHelper.current("entryType")));
    sb.append(String.format("\n\t.withEntryKind(%s)", getEntryKind(entry)));
    sb.append("\n\t.withToken(%s)".formatted(namingHelper.current("token")));
    sb.append("\n\t.bind();");
    assignToOp.ifPresent(e -> sb.append("\n")
        .append(e.formatted(
            symbolTableEntry,
            "%s.typeResolution(%s)"
                .formatted(namingHelper.current("signature"), namingHelper.current("entryType")))));
    to.setValue(attr(), sb.toString());
  }

  @Override
  public void getImport(ASTNode to, int type, int token) {
    StringBuilder sb = new StringBuilder();
    bindGenericType(to, "import", to.ntchild(type).getValue(attr()), null, null, null, sb);
    sb.append("\n\t.withToken(")
        .append(to.ntchild(token).<String>getValue(attr()))
        .append(")");
    sb.append("\n\t.bind();");
    to.setValue(attr(), sb.toString());
  }

  private void bindGenericType(
      ASTNode to,
      String entry,
      String type,
      @Nullable Integer scopeId,
      @Nullable Integer inside,
      @Nullable Integer attributes,
      StringBuilder sb) {
    namingHelper.setUnit();
    namingHelper.setHelper();
    sb.append("new %s()".formatted(symbolTableEntryFactory));
    sb.append("\n\t.withCompilationHelper(").append(helperID).append(")");
    sb.append("\n\t.withCompilationUnit(").append(unitId).append(")");
    Optional.ofNullable(scopeId)
        .ifPresent(id -> sb.append("\n\t").append(to.ntchild(id).<String>getValue(attr())));
    sb.append("\n\t.withEntryType(").append(type).append(")");
    Optional.ofNullable(inside)
        .map(to::ntchild)
        .flatMap(e -> e.<String>getOptValue(attr()))
        .ifPresent(sb::append);
    Optional.ofNullable(attributes)
        .map(to::ntchild)
        .flatMap(e -> e.<String>getOptValue("Text"))
        .ifPresent(sb::append);
    sb.append(String.format("\n\t.withEntryKind(%s)", getEntryKind(entry)));
  }

  @Override
  public void initRoot(ASTNode to, int priority) {
    namingHelper.setHelper();
    var initRoot =
        "helper.initRoot($ctx, %s);".formatted(to.ntchild(priority).<String>getValue(attr()));
    to.setValue(attr(), initRoot);
  }

  @Override
  public String collectAll(List<String> list, boolean init) {
    StringBuilder sb = new StringBuilder();
    if (init && namingHelper.hasHelper()) {
      sb.append(assignHelper()).append(";\n");
    }
    if (init && namingHelper.hasUnit()) {
      sb.append(assignUnit()).append(";\n");
    }
    sb.append(String.join("\n", list));
    return sb.toString();
  }
}
