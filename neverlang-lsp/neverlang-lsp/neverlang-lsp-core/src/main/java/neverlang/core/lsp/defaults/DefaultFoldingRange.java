package neverlang.core.lsp.defaults;

import java.util.List;
import java.util.Objects;
import neverlang.core.lsp.capabilities.BaseCapability;
import neverlang.core.lsp.capabilities.FoldingRangeCapability;
import neverlang.core.lsp.utils.Conversions;
import neverlang.core.typesystem.graph.IndexNode;
import neverlang.core.typesystem.graph.Indexable;
import org.eclipse.lsp4j.FoldingRange;
import org.eclipse.lsp4j.FoldingRangeRequestParams;
import org.eclipse.lsp4j.ServerCapabilities;

public class DefaultFoldingRange extends BaseCapability implements FoldingRangeCapability {

  @Override
  public void setCapability(ServerCapabilities serverCapabilities) {
    serverCapabilities.setFoldingRangeProvider(true);
  }

  @Override
  public List<FoldingRange> foldingRange(FoldingRangeRequestParams params) {
    return getWorkspaceHandler().getIndexTree(Conversions.extractPath(params).get()).stream()
        .flatMap(IndexNode::streamAll)
        .map(IndexNode::index)
        .filter(Objects::nonNull)
        .map(Indexable::foldingRange)
        .filter(Objects::nonNull)
        .map(Conversions::toFoldingRange)
        .toList();
  }
}
