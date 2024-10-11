public class Commands {
    private Buffer buffer;
    private ClipboardManager clipboardManager;
    private Selection selection;

    public Commands(Buffer buffer, ClipboardManager clipboardManager, Selection selection) {
        this.buffer = buffer;
        this.clipboardManager = clipboardManager;
        this.selection = selection;
    }

    public void executeCommand(String input) {
        //2 : selection de texte
        if (input.startsWith("!select ")) { // commande select 0 5 avec 0 debut et 5 la fin
            String[] parts = input.split(" ");
            int start = Integer.parseInt(parts[1]);
            int end = Integer.parseInt(parts[2]);
            selection.setSelection(start, end);

        } else if (input.equals("!copy")) { // 3: copie dans presse papier
            String selectedText = selection.getSelectedText(buffer.getText());
            clipboardManager.copy(selectedText);
            System.out.println("Texte copié : " + selectedText);

        } else if (input.equals("!cut")) {// 4: copie dans presse papier puis suppression
            String selectedText = selection.getSelectedText(buffer.getText());
            clipboardManager.copy(selectedText);
            buffer.delete(selection.getStart(), selection.getEnd());
            System.out.println("Texte coupé : " + selectedText);

        } else if (input.equals("!paste")) { //5 : remplacement
            String textToPaste = clipboardManager.paste();
            buffer.append(textToPaste);
            System.out.println("Texte collé : " + textToPaste);

        } else if (input.equals("!exit")) {// quitter l'editeur
            System.out.println("Fermeture de l'éditeur.");
            System.exit(0);

        } else {
            buffer.append(input);
        }
    }
}
