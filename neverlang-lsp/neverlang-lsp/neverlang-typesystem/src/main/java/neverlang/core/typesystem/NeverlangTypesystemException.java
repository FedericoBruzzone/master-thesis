package neverlang.core.typesystem;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import neverlang.core.typesystem.symbols.Location;

public abstract class NeverlangTypesystemException extends RuntimeException {
  public NeverlangTypesystemException(String message) {
    super(message);
  }

  public void submit(AbstractCompilationHelper compilationHelper) {
    var logRecord = new LogRecord(Level.SEVERE, this.getMessage());
    logRecord.setThrown(this);
    compilationHelper.submit(logRecord);
  }

  public abstract Location location();
}
