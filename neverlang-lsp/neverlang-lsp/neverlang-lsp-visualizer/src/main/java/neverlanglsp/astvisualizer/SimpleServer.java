package neverlanglsp.astvisualizer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class SimpleServer extends WebSocketServer implements Runnable {

  public SimpleServer(String host, int port) {
    super(new InetSocketAddress(host, port));
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
    System.out.println("new connection to " + conn.getRemoteSocketAddress());
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    System.out.println("closed "
        + conn.getRemoteSocketAddress()
        + " with exit code "
        + code
        + " additional info: "
        + reason);
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    System.out.println("received message from " + conn.getRemoteSocketAddress() + ": " + message);
  }

  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
    System.out.println("received ByteBuffer from " + conn.getRemoteSocketAddress());
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    System.err.println(
        "an error occurred on connection " + conn.getRemoteSocketAddress() + ":" + ex);
  }

  @Override
  public void onStart() {
    System.out.println("Web socket server started successfully at port " + getPort());
  }
}
