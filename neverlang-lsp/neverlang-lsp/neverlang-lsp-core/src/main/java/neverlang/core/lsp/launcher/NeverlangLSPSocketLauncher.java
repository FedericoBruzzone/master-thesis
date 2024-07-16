package neverlang.core.lsp.launcher;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;

public class NeverlangLSPSocketLauncher extends NeverlangLSPLauncher implements Runnable {

  private final int port;

  public NeverlangLSPSocketLauncher(NeverlangLSPProvider provider) {
    super(provider);
    this.port = 0;
  }

  public NeverlangLSPSocketLauncher(NeverlangLSPProvider provider, int port) {
    super(provider);
    this.port = port;
  }

  @Override
  public void run() {
    while (true) {
      try (var serverSocket = new ServerSocket(port)) {
        System.out.printf("Server started at port %d\n", port);
        var client = serverSocket.accept();
        System.out.println(client);
        startServer(client.getInputStream(), client.getOutputStream());
      } catch (IOException | ExecutionException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void run_pipe() {
    while (true)
      try {
        startServer(System.in, System.out);
      } catch (ExecutionException | InterruptedException e) {
        throw new RuntimeException(e);
      }
  }
}
