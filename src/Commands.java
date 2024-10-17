import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JTextArea;

public class Commands {
    private Buffer buffer;
    private ClipboardManager clipboardManager;
    private Selection selection;

    // Piles pour gérer l'annulation et le rétablissement
    private Stack<String> undoStack = new Stack<>();
    private Stack<String> redoStack = new Stack<>();

    // Liste pour enregistrer les commandes entre !start et !stop
    private List<String> recordedCommands = new ArrayList<>();
    private boolean isRecording = false;

    public Commands(Buffer buffer, ClipboardManager clipboardManager, Selection selection) {
        this.buffer = buffer;
        this.clipboardManager = clipboardManager;
        this.selection = selection;
    }

    public void executeCommand(String input, JTextArea logArea) {
        // Enregistrement des commandes si on est en mode enregistrement
        if (isRecording && !input.equals("!stop")) {
            recordedCommands.add(input);
        }

        String[] parts = input.split(" ");
        String command = parts[0];

        if (input.startsWith("!select")) { // commande select 0 5 avec 0 début et 5 la fin

            logArea.append("Commande exécutée : " + input + "\n");
            if (parts.length == 3) {
                int start = Integer.parseInt(parts[1]); // (❁´◡`❁)
                int end = Integer.parseInt(parts[2]);
                selection.setSelection(start, end);
            }

            // Afficher le texte sélectionné dans la zone de log
            String selectedText = selection.getSelectedText(buffer.getText());
            logArea.append("Texte sélectionné : \n\"" + selectedText + "\"\n");

        }

        switch (command) {
            case "!start":
                recordedCommands = new ArrayList<>(); // reset des enregistrements
                isRecording = true;
                logArea.append("Enregistrement des commandes commencé...\n");
                break;
            case "!stop":
                isRecording = false;
                logArea.append("Enregistrement des commandes terminé.\n");
                break;
            case "!repeat":
                logArea.append("Répétition des commandes enregistrées :\n");
                for (String cmd : recordedCommands) {
                    logArea.append("> " + cmd + "\n");
                    executeCommand(cmd, logArea); // Exécute chaque commande enregistrée
                }
                break;
            case "!select":
                logArea.append("Commande exécutée : " + input + "\n");
                handleSelect(parts, logArea);
                break;
            case "!copy":
                logArea.append("Commande exécutée : " + input + "\n");
                handleCopy(logArea);
                break;
            case "!cut":
                logArea.append("Commande exécutée : " + input + "\n");
                handleCut(logArea);
                break;
            case "!paste":
                logArea.append("Commande exécutée : " + input + "\n");
                handlePaste(logArea);
                break;
            case "!delete":
                logArea.append("Commande exécutée : " + input + "\n");
                handleDelete(logArea);
                break;
            case "!undo":
                logArea.append("Commande exécutée : " + input + "\n");
                undo(logArea);
                System.out.println("undo : " + buffer.getText());
                break;
            case "!redo":
                logArea.append("Commande exécutée : " + input + "\n");
                redo(logArea);
                break;
            case "!exit":
                handleExit(logArea);
                break;
            default:
                // sauvegarder l'état courant
                saveStateForUndo();
                logArea.append("Texte ajouté :\n" + input + "\n");
                buffer.append(input);
                break;
        }
    }

    // Méthode pour gérer la sélection
    private void handleSelect(String[] parts, JTextArea logArea) {
        if (parts.length != 3) {
            selection.selectAllText(buffer.getText()); // si mal écrit, on sélectionne tout.
        } else {
            int start = Integer.parseInt(parts[1]);
            int end = Integer.parseInt(parts[2]);
            selection.setSelection(start, end);
        }

        String selectedText = selection.getSelectedText(buffer.getText());
        logArea.append("Texte sélectionné : \n\"" + selectedText + "\"\n");
    }

    // Méthode pour gérer la copie
    private void handleCopy(JTextArea logArea) {
        String selectedText = selection.getSelectedText(buffer.getText());
        clipboardManager.copy(selectedText);
        logArea.append("Texte copié : \n\"" + selectedText + "\"\n");
        selection.setSelection(-1, -1); // reset le select
    }

    // Méthode pour gérer le couper
    private void handleCut(JTextArea logArea) {
        // sauvegarder l'état courant
        saveStateForUndo();
        String selectedText = selection.getSelectedText(buffer.getText());
        if (!selectedText.isEmpty()) {
            clipboardManager.copy(selectedText);
            buffer.delete(selection.getStart(), selection.getEnd());
            logArea.append("Texte coupé : \n\"" + selectedText + "\"\n");
        } else {
            logArea.append("Rien n'a été sélectionné.\n");
        }
        selection.setSelection(-1, -1); // reset le select
    }

    // Méthode pour gérer le coller
    private void handlePaste(JTextArea logArea) {
        // sauvegarder l'état courant
        saveStateForUndo();

        String textToPaste = clipboardManager.paste();
        String selectedText = selection.getSelectedText(buffer.getText());

        if (!selectedText.isEmpty()) {
            buffer.delete(selection.getStart(), selection.getEnd()); // Supprimer le texte sélectionné
            buffer.appendAt(selection.getStart(), textToPaste); // Coller à l'endroit sélectionné
        } else {
            buffer.appendAt(-1, textToPaste); // ajoute à la fin si rien sélectionné
        }
        logArea.append("Texte collé : \n\"" + textToPaste + "\"\n");
        selection.setSelection(-1, -1); // reset le select
    }

    // Méthode pour gérer la suppression
    private void handleDelete(JTextArea logArea) {
        // sauvegarder l'état courant
        saveStateForUndo();
        String selectedText = selection.getSelectedText(buffer.getText());
        if (!selectedText.isEmpty()) {
            buffer.delete(selection.getStart(), selection.getEnd());
            logArea.append("Texte supprimé : \n\"" + selectedText + "\"\n");
        } else {
            logArea.append("Rien n'a été sélectionné.\n");
        }
        selection.setSelection(-1, -1); // reset le select
    }

    // Méthode pour gérer la sortie
    private void handleExit(JTextArea logArea) {
        logArea.append("Fermeture de l'éditeur.\n");
        System.exit(0);
    }

    // Annuler la dernière action
    private void undo(JTextArea logArea) {
        if (!undoStack.isEmpty()) {
            redoStack.push(buffer.getText()); // Sauvegarder l'état courant dans redo
            String previousState = undoStack.pop();
            buffer.setText(previousState); // Restaurer l'état précédent
            logArea.append("Action annulée : " + previousState + "\n");
        } else {
            logArea.append("Aucune action à annuler.\n");
        }
    }

    // Rétablir la dernière action annulée
    private void redo(JTextArea logArea) {
        if (!redoStack.isEmpty()) {
            undoStack.push(buffer.getText()); // Sauvegarder l'état courant dans undo
            String nextState = redoStack.pop();
            buffer.setText(nextState); // Restaurer l'état rétabli
            logArea.append("Action rétablie : " + nextState + "\n");
        } else {
            logArea.append("Aucune action à rétablir.\n");
        }
    }

    // Sauvegarde l'état actuel pour pouvoir annuler
    private void saveStateForUndo() {
        undoStack.push(buffer.getText());
        System.out.println("save" + undoStack);
        redoStack.clear(); // On vide la pile redo car un nouvel état est créé
    }

}
