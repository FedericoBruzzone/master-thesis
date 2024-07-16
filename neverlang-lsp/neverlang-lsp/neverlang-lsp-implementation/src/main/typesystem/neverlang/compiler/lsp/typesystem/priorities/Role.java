package neverlang.compiler.lsp.typesystem.priorities;

import neverlang.core.typelang.annotations.TypeLangAnnotation;
import neverlang.core.typelang.annotations.TypeSystemKind;
import neverlang.core.typesystem.Priority;

@NeverlangPriorityAnnotation(NeverlangPriorityEnum.ROLE)
@TypeLangAnnotation(kind = TypeSystemKind.PRIORITY)
public class Role extends Priority {}
