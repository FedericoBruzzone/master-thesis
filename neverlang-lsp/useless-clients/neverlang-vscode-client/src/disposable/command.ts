import * as vscode from "vscode";
import { ASTVisualizerPanel } from "../panels/astVisualizerPanel";

namespace Commands {
  export const CODE_LENS = "neverlang.codeLens";
  export const AST_VISUALIZER = "neverlang.astVisualizer";
}

export function registerCommands(context: vscode.ExtensionContext) {
  context.subscriptions.push(
    vscode.commands.registerCommand(
      Commands.CODE_LENS,
      async (
        uri: string,
        position: vscode.Position,
        locations: Array<{ uri: string; range: vscode.Range }>,
        multiple: string
      ) => {
        // The code you place here will be executed every time your command is executed
        // Display a message box to the user
        await vscode.commands.executeCommand(
          "editor.action.peekLocations",
          vscode.Uri.parse(uri),
          new vscode.Position(position.line, position.character),
          locations.map(
            (e) =>
              new vscode.Location(
                vscode.Uri.parse(e.uri),
                new vscode.Range(
                  new vscode.Position(
                    e.range.start.line,
                    e.range.start.character
                  ),
                  new vscode.Position(e.range.end.line, e.range.end.character)
                )
              )
          ),
          multiple
        );
      }
    )
  );

  context.subscriptions.push(
    vscode.commands.registerCommand(Commands.AST_VISUALIZER, () => {
      ASTVisualizerPanel.render(context.extensionUri);
    })
  );
}
