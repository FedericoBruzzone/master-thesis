package neverlang.core.lsp.capabilities;

import java.util.List;
import org.eclipse.lsp4j.FoldingRange;
import org.eclipse.lsp4j.FoldingRangeRequestParams;

public interface FoldingRangeCapability extends Capability {
  List<FoldingRange> foldingRange(FoldingRangeRequestParams params);
}
