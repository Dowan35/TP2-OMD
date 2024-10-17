import java.util.Scanner;

public class MiniEditeur {
    private Buffer buffer = new Buffer();
    private ClipboardManager clipboardManager = new ClipboardManager();
    private Selection selection = new Selection();
    private Commands commands;

    public static void main(String[] args) {
        MiniEditeur editeur = new MiniEditeur();
        editeur.run();
    }

    public MiniEditeur() {
        commands = new Commands(buffer, clipboardManager, selection);
    }

    public void run() {
        // Fermeture auto du scanner
        try (Scanner scanner = new Scanner(System.in)) { // Utiliser try-with-resources
            System.out.println(
                    "##############################################\nBienvenue dans Esir Editor.\n##############################################");
            while (true) {
                System.out.println(buffer.getText());
                System.out.println("Entrez une commande (ou du texte) :");
                String input = scanner.nextLine();

                // Si l'utilisateur saisit du texte sans commande
                if (!input.startsWith("!select ") && !input.equals("!copy") &&
                        !input.equals("!cut") && !input.equals("!paste") &&
                        !input.equals("!exit")) {
                    buffer.append(input); // Ajouter le texte au buffer
                } else {
                    commands.executeCommand(input, logArea); // Exécuter la commande
                }
            }
        }
    }
}

// javac MiniEditeur.java Buffer.java Selection.java ClipboardManager.java
// Commands.java
// java -cp . MiniEditeur