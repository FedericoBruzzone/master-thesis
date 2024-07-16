package neverlang.core.lsp.capabilities;

import java.util.List;
import org.eclipse.lsp4j.*;

public interface CallHierarchyCapability extends Capability {
  List<CallHierarchyIncomingCall> callHierarchyIncomingCalls(
      CallHierarchyIncomingCallsParams params);

  List<CallHierarchyOutgoingCall> callHierarchyOutgoingCalls(
      CallHierarchyOutgoingCallsParams params);

  List<CallHierarchyItem> prepareCallHierarchy(CallHierarchyPrepareParams params);
}
