package typelang.utils;

import java.text.MessageFormat;
import neverlang.core.typesystem.InferenceException;
import neverlang.core.typesystem.NeverlangTypesystemException;
import neverlang.core.typesystem.TypeMismatchException;
import neverlang.core.typesystem.defaults.CompilationUnit;
import neverlang.core.typesystem.defaults.DefaultSymbolTableEntry;
import neverlang.core.typesystem.defaults.DefaultSymbolTableEntryFactory;

public class Templates {

  //    public static String symbolTableEntryFactory =
  // DefaultSymbolTableEntryFactory.class.getSimpleName();
  public static String symbolTableEntryFactory =
      DefaultSymbolTableEntryFactory.class.getCanonicalName();
  public static String compilationHelper = TestCompilationHelper.class.getSimpleName();
  public static String compilationUnit = CompilationUnit.class.getSimpleName();

  public static String symbolTableEntry = DefaultSymbolTableEntry.class.getCanonicalName();

  public static String typeModuleDefinition = MessageFormat.format(
      """
var helper = $ctx.root().<{2}>getValue("${2}");
var unit = $ctx.root().<{1}>getValue("${1}");
var type0 = new TypeModule();
{3} scope0 = new {0}()
    .withCompilationHelper(helper)
    .withCompilationUnit(unit)
    .withToken($ctx.nt(1).getValue($ctx.attr(1, "token")))
    .withEntryType(type0)
    .withFoldingRange(Range.foldingRangeFrom($n,0,1))
    .withEntryKind(EntryKind.DEFINE)
    .bindScopeOrReuse();
""",
      symbolTableEntryFactory, compilationUnit, compilationHelper, symbolTableEntry);

  public static String typeModuleDefinitionWithModifier = MessageFormat.format(
      """
var helper = $ctx.root().<TestCompilationHelper>getValue("$TestCompilationHelper");
var unit = $ctx.root().<CompilationUnit>getValue("$CompilationUnit");
var type0 = new TypeFile();
{3} scope0 = new {0}()
	.withCompilationHelper(helper)
	.withCompilationUnit(unit)
	.withToken($ctx.nt(1).getValue($ctx.attr(1, "token")))
	.withEntryType(type0)
	.withModifier($ctx.nt(1).getValue($ctx.attr(1, "value")))
	.withEntryKind(EntryKind.DEFINE)
	.bindScopeOrReuse();
""",
      symbolTableEntryFactory, compilationUnit, compilationHelper, symbolTableEntry);

  public static String typeFileDefinition = MessageFormat.format(
      """
var helper = $ctx.root().<{2}>getValue("${2}");
var unit = $ctx.root().<{1}>getValue("${1}");
var type0 = new TypeFile();
{3} scope0 = new {0}()
        .withCompilationHelper(helper)
        .withCompilationUnit(unit)
        .fromSource($ctx, "global")
        .withEntryType(type0)
        .withEntryKind(EntryKind.DEFINE)
        .bindScopeOrReuse();
""",
      symbolTableEntryFactory, compilationUnit, compilationHelper, symbolTableEntry);

  public static String typeCustomNameDefinition = MessageFormat.format(
      """
var helper = $ctx.root().<{2}>getValue("${2}");
var unit = $ctx.root().<{1}>getValue("${1}");
var type0 = new TypeModule();
{3} scope0 = new {0}()
        .withCompilationHelper(helper)
        .withCompilationUnit(unit)
        .withIdentifier("customModule")
        .withEntryType(type0)
        .withEntryKind(EntryKind.DEFINE)
        .bindScopeOrReuse();
""",
      symbolTableEntryFactory, compilationUnit, compilationHelper, symbolTableEntry);

  public static String taskDefinition = MessageFormat.format(
      """
helper.getTaskBuilder()
    .withContext($ctx)
    .insideScope(scope0)
    .withPriority(Priority.MODULE)
    .withAstNodes($ctx.nt(1),$ctx.nt(2))
    .createAndRegisterTask();
""",
      compilationHelper);

  public static String typeDefinitionFromChild = MessageFormat.format(
      """
var helper = $ctx.root().<{2}>getValue("${2}");
var unit = $ctx.root().<{1}>getValue("${1}");
var type0 = $ctx.nt(1).<%s>getValue($ctx.attr(1, "%s"));
new {0}()
    .withCompilationHelper(helper)
    .withCompilationUnit(unit)
    .withToken($ctx.nt(2).getValue($ctx.attr(2, "%s")))
    .withEntryType(type0)
    .withEntryKind(EntryKind.DEFINE)
    .bind();
""",
      symbolTableEntryFactory, compilationUnit, compilationHelper);

  public static String validationDefinition = """
%s.%s(%s, helper);
    """;

  public static String taskRootDefinition =
      """
helper.getTaskBuilder()
    .withContext($ctx)
    .insideScope(scope0)
    .withParentCompilationUnit(helper.getRootCompilationUnit())
    .withPriority(Priority.MODULE)
    .withAstNodes($ctx.nt(1),$ctx.nt(2))
    .createAndRegisterTask();
""";

  public static String typeFunctionDefinition = MessageFormat.format(
      """
var helper = $ctx.root().<{2}>getValue("${2}");
var unit = $ctx.root().<{1}>getValue("${1}");
var type0 = new TypeFunction();
new {0}()
        .withCompilationHelper(helper)
        .withCompilationUnit(unit)
        .withToken($ctx.nt(1).getValue($ctx.attr(1, "token")))
        .withEntryType(type0)
        .withFoldingRange(Range.foldingRangeFrom($n,0,1))
        .withEntryKind(EntryKind.DEFINE)
        .bind();
""",
      symbolTableEntryFactory, compilationUnit, compilationHelper);

  public static String catchEvalString = String.format(
      "try {\n$ctx.eval($ctx.nt(1));\n} catch (%s e) {\ne.submit($ctx.root().<%s>getValue(\"$%s\"));\n}",
      NeverlangTypesystemException.class.getCanonicalName(),
      TestCompilationHelper.class.getSimpleName(),
      TestCompilationHelper.class.getSimpleName());

  public static String catchWithOrBranchString = String.format(
      "try {\n$ctx.eval($ctx.nt(1));\n} catch (%s e) {\n$ctx.eval($ctx.nt(2));\n}",
      InferenceException.class.getSimpleName());

  public static String checkString = MessageFormat.format(
      """
if(!$ctx.nt(1).<{0}>getValue($ctx.attr(1, "type")).isAssignableFrom($ctx.nt(2).<{0}>getValue($ctx.attr(2, "type")),Variance.INVARIANT)){2}
    throw new {1}($ctx.nt(2).<{0}>getValue($ctx.attr(2, "type")),$ctx.nt(1).<{0}>getValue($ctx.attr(1, "type")),%s);
{3}""",
      symbolTableEntry, TypeMismatchException.class.getCanonicalName(), "{", "}");

  public static String resolveString = MessageFormat.format(
      """
    $ctx.nt(1).<{0}>getValue($ctx.attr(1, "type")).refType().set($ctx.nt(2).<{0}>getValue($ctx.attr(2, "type")).type());
    """,
      symbolTableEntry);

  public static String currentScopeTaskDefinition = MessageFormat.format(
      """
var helper = $ctx.root().<{0}>getValue("${0}");
helper.getTaskBuilder()
    .withContext($ctx)
    .withId(%s)
    .withPriority(Priority.MODULE)
    .withAstNodes($ctx.nt(1),$ctx.nt(2))
    .createAndRegisterTask();
""",
      compilationHelper);
}
