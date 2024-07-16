package neverlang.core.lsp.defaults.priorities;

import neverlang.core.typelang.annotations.PriorityAnnotation;
import neverlang.core.typelang.annotations.PriorityEnum;
import neverlang.core.typelang.annotations.TypeLangAnnotation;
import neverlang.core.typelang.annotations.TypeSystemKind;
import neverlang.core.typesystem.Priority;

@PriorityAnnotation(PriorityEnum.LANGUAGE)
@TypeLangAnnotation(kind = TypeSystemKind.PRIORITY)
public class Language extends Priority {}
