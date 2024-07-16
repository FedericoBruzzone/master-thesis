package neverlang.compiler.lsp.typesystem.priorities;

import neverlang.core.typelang.annotations.*;
import neverlang.core.typesystem.Priority;

@NeverlangPriorityAnnotation(NeverlangPriorityEnum.BUNDLE)
@TypeLangAnnotation(kind = TypeSystemKind.PRIORITY)
public class Bundle extends Priority {}
