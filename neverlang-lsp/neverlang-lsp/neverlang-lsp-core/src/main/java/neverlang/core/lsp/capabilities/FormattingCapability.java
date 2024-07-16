package neverlang.core.lsp.capabilities;

import java.util.List;
import org.eclipse.lsp4j.DocumentFormattingParams;
import org.eclipse.lsp4j.DocumentOnTypeFormattingParams;
import org.eclipse.lsp4j.DocumentRangeFormattingParams;
import org.eclipse.lsp4j.TextEdit;

public interface FormattingCapability extends Capability {
  List<? extends TextEdit> formatting(DocumentFormattingParams params);

  List<? extends TextEdit> onTypeFormatting(DocumentOnTypeFormattingParams params);

  List<? extends TextEdit> rangeFormatting(DocumentRangeFormattingParams params);
}
