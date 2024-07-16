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
let outputChannelName = "simplelang.SimpleLang-server";
let outputChannel = vscode.window.createOutputChannel(outputChannelName);

// This method is called when your extension is activated
// Your extension is activated the very first time the command is executed
export function activate() {
  // Use the console to output diagnostic information (console.log) and errors (console.error)
  // This line of code will only be executed once when your extension is activated

  console.log(
    "Congratulations, your extension \"simplelang.SimpleLang-vscode-client\" is now active!!"
  );

  client = new LanguageClient(
    "simplelang.SimpleLang-client",
    outputChannelName,
    getServerOptions(),
    getClientOptions()
  );

  client.start();
  vscode.window.showInformationMessage(
    "Language server for simplelang.SimpleLang started successfully"
  );
}

function getServerOptions() {
  const DIR = __dirname; // + "/../shadow.jar"

  const serverCommand = "java";
  const serverArgs = [
    "-jar",
    "/Users/federicobruzzone/Documents/ADAPT-LAB/tesi-dagostino/trunk/examples/simplelanguage/build/libs/simplelang.SimpleLang-client.jar",
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
      fileEvents: [workspace.createFileSystemWatcher("**/*.sl")],
    },
    documentSelector: [{ scheme: "file", language: "simplelang.SimpleLang" }],
  };
}

// this method is called when your extension is deactivated
export function deactivate() {
  client?.stop();
}
