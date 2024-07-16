// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import * as vscode from "vscode";
import * as net from "net";
import {
  LanguageClient,
  LanguageClientOptions,
  StreamInfo,
  ServerOptions,
} from "vscode-languageclient/node";
import { workspace } from "vscode";
import { registerCommands } from "./disposable/command";
import { ASTVisualizerPanel } from "./panels/astVisualizerPanel";

let client: LanguageClient;
let outputChannelName = "Neverlang Language Server";
let outputChannel = vscode.window.createOutputChannel(outputChannelName);

// This method is called when your extension is activated
// Your extension is activated the very first time the command is executed
export function activate(context: vscode.ExtensionContext) {
  // Use the console to output diagnostic information (console.log) and errors (console.error)
  // This line of code will only be executed once when your extension is activated

  console.log(
    'Congratulations, your extension "neverlang-vscode-client" is now active!!'
  );

  client = new LanguageClient(
    "neverlang",
    getServerOptions(),
    getClientOptions()
  );

  registerCommands(context);

  client.start();
  // ASTVisualizerPanel.render(context.extensionUri);
  vscode.window.showInformationMessage(
    "Language server for Neverlang started successfully"
  );
}

function getServerOptions(): ServerOptions {
  const { NODE_ENV } = process.env;
  if (NODE_ENV === "production") {
    let serverOptions: ServerOptions = {
      command: "neverlang-language-server",
      args: [],
      options: {},
    };
    return serverOptions;
  } else {
    // outputChannel.appendLine("Start with Socket");
    return async () => {
      let socket = net.connect({
        port: 5123,
      });
      let result: StreamInfo = {
        writer: socket,
        reader: socket,
      };
      socket.on("error", () =>
        vscode.window.showErrorMessage("Language server unreachable")
      );
      return result;
    };
  }
}

function getClientOptions(): LanguageClientOptions {
  return {
    outputChannel,
    synchronize: {
      fileEvents: [workspace.createFileSystemWatcher("**/*.nl")],
    },
    documentSelector: [{ scheme: "file", language: "neverlang" }],
  };
}

// this method is called when your extension is deactivated
export function deactivate() {
  client?.stop();
}
