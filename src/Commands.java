import javax.swing.JTextArea;

public class Commands {
    private Buffer buffer;
    private ClipboardManager clipboardManager;
    private Selection selection;

    public Commands(Buffer buffer, ClipboardManager clipboardManager, Selection selection) {
        this.buffer = buffer;
        this.clipboardManager = clipboardManager;
        this.selection = selection;
    }

    public void executeCommand(String input, JTextArea logArea) {
        logArea.append("Commande exécutée : " + input + "\n"); // Ajouter log

        // 2 : selection de texte
        if (input.startsWith("!select")) { // commande select 0 5 avec 0 début et 5 la fin

            String[] parts = input.split(" ");
            if (parts.length != 3) {
                selection.selectAllText(buffer.getText()); // si mal écrit, on sélectionne tout.
            } else {
                int start = Integer.parseInt(parts[1]);
                int end = Integer.parseInt(parts[2]);
                selection.setSelection(start, end);
            }

            // Afficher le texte sélectionné dans la zone de log
            String selectedText = selection.getSelectedText(buffer.getText());
            logArea.append("Texte sélectionné : \"" + selectedText + "\"\n");

        } else if (input.equals("!copy")) { // 3: copie dans presse papier
            String selectedText = selection.getSelectedText(buffer.getText());
            clipboardManager.copy(selectedText);
            System.out.println("Texte copié : " + selectedText);
            selection.setSelection(-1, -1); // reset le select

        } else if (input.equals("!cut")) { // 4: copie dans presse papier puis suppression
            String selectedText = selection.getSelectedText(buffer.getText());

            if (selectedText != "") { // si du texte a bien été selectionné
                clipboardManager.copy(selectedText);
                buffer.delete(selection.getStart(), selection.getEnd());
                System.out.println("Texte coupé : " + selectedText);
            } else {
                System.out.println("Rien n'a été sélectionné.");
            }

            selection.setSelection(-1, -1); // reset le select

        } else if (input.equals("!paste")) { // 5 : remplacer la sélection
            String textToPaste = clipboardManager.paste();

            String selectedText = selection.getSelectedText(buffer.getText());

            // si rien sélectionné, on ajoute a la suite du texte existant.
            if (selectedText != "") { // si du texte a bien été selectionné
                buffer.delete(selection.getStart(), selection.getEnd()); // Supprimer le texte sélectionné
                buffer.appendAt(selection.getStart(), textToPaste); // Coller à l'endroit sélectionné

            } else {
                System.out.println(buffer.getText().length() - 1);
                buffer.appendAt(-1, textToPaste); // ajoute a la suite
            }
            System.out.println("Texte collé : " + textToPaste);
            selection.setSelection(-1, -1); // reset le select

        } else if (input.equals("!delete")) { // supprimer la sélection
            String selectedText = selection.getSelectedText(buffer.getText());

            if (selectedText != "") { // si du texte a bien été selectionné
                buffer.delete(selection.getStart(), selection.getEnd());
                System.out.println("Texte supprimé : " + selectedText);
            } else {
                System.out.println("Rien n'a été sélectionné.");
            }

            selection.setSelection(-1, -1); // reset le select

        } else if (input.equals("!exit")) { // quitter l'éditeur
            System.out.println("Fermeture de l'éditeur.");
            System.exit(0);

        } else {
            buffer.append(input);
        }
    }
}
