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
        //fermeture auto du scanner
        try (Scanner scanner = new Scanner(System.in)) { // Utiliser try-with-resources
            while (true) {
                System.out.println("Texte : " + buffer.getText());
                System.out.println("Entrez une commande :");
                String input = scanner.nextLine();
                commands.executeCommand(input);
            }
        }
    }
}


// javac MiniEditeur.java Buffer.java Selection.java ClipboardManager.java Commands.java
// java MiniEditeur
