import { VSCodeButton } from "@vscode/webview-ui-toolkit/react";
import { ElementsDefinition } from "cytoscape";
import { useCallback, useState } from "react";
import { ASTVisualizer } from "./ASTVisualizer";

function App() {
  const [socket, setSocket] = useState<WebSocket | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [elements, setElements] = useState<ElementsDefinition | null>(null);

  const startWebSocket = useCallback(() => {
    if (socket === null) {
      setIsLoading(true);
      const sck = new WebSocket("ws://localhost:8888");

      sck.onmessage = function (event) {
        try {
          const parsed = JSON.parse(event.data);
          setElements(parsed);
        } catch (error) {}
      };

      sck.onopen = () => {
        setSocket(sck);
        setIsLoading(false);
      };

      sck.onclose = () => {
        console.log("Closing websocket");
        setSocket(null);
        setElements(null);
        setIsLoading(false);
      };

      sck.onerror = () => {
        alert("Error connecting to websocket");
        setSocket(null);
        setIsLoading(false);
      };
    } else {
      socket?.close();
    }
  }, [socket, setSocket, setIsLoading, setElements]);

  return (
    <main>
      <VSCodeButton onClick={startWebSocket} disabled={isLoading}>
        {socket === null ? "START WEBSOCKET" : "STOP WEBSOCKET"}
      </VSCodeButton>
      <div
        style={{ height: "90vh", backgroundColor: "white", marginTop: "1rem" }}
      >
        {elements !== null && <ASTVisualizer elements={elements} />}
        {isLoading && <p>Loading...</p>}
        {socket !== null && elements === null && <p>Waiting for data...</p>}
      </div>
    </main>
  );
}

export default App;
