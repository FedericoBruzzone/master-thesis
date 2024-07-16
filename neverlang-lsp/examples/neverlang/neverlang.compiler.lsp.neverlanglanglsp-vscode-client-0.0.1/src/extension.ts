// The module vscode contains the VS Code extensibility API
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

const { spawn } = require("child_process");
let client: LanguageClient;
let outputChannelName = "neverlang.compiler.lsp.NeverlangLangLSP-server";
let outputChannel = vscode.window.createOutputChannel(outputChannelName);

// This method is called when your extension is activated
// Your extension is activated the very first time the command is executed
export function activate() {
  // Use the console to output diagnostic information (console.log) and errors (console.error)
  // This line of code will only be executed once when your extension is activated

  console.log(
    "Congratulations, your extension \"neverlang.compiler.lsp.NeverlangLangLSP-vscode-client\" is now active!!"
  );

  client = new LanguageClient(
    "neverlang.compiler.lsp.NeverlangLangLSP-client",
    outputChannelName,
    getServerOptions(),
    getClientOptions()
  );

  client.start();
  vscode.window.showInformationMessage(
    "Language server for neverlang.compiler.lsp.NeverlangLangLSP started successfully"
  );
}

function getServerOptions() {
  const DIR = __dirname; // + "/../shadow.jar"

  const serverCommand = "java";
  const serverArgs = [
    "-jar",
    "/home/fcb/Documents/neverlang-lsp/examples/neverlang/build/libs/neverlang.compiler.lsp.NeverlangLangLSP-client.jar",
  ];
  const serverOptions = {
    command: serverCommand,
    args: serverArgs,
  };
  return serverOptions;
}

function getClientOptions(): LanguageClientOptions {
  return {
    outputChannel,
    synchronize: {
      fileEvents: [workspace.createFileSystemWatcher("**/*.nl")],
    },
    documentSelector: [{ scheme: "file", language: "neverlang.compiler.lsp.NeverlangLangLSP" }],
  };
}

// this method is called when your extension is deactivated
export function deactivate() {
  client?.stop();
}