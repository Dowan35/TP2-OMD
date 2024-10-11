import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MiniEditeurGUI extends JFrame {
    private Buffer buffer = new Buffer();
    private ClipboardManager clipboardManager = new ClipboardManager();
    private Selection selection = new Selection();
    private Commands commands;

    private JTextArea textArea;
    private JTextArea logArea; // Zone de log
    private JTextField inputField;
    private JLabel actionLabel; // Label pour les actions effectuées

    public MiniEditeurGUI() {
        commands = new Commands(buffer, clipboardManager, selection);
        
        // Configuration de la fenêtre
        setTitle("Esir Editor");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Zone de texte pour afficher le contenu
        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Zone de log pour afficher les actions
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(Color.LIGHT_GRAY);
        add(new JScrollPane(logArea), BorderLayout.EAST);

        // Champ de saisie pour les commandes et le texte
        inputField = new JTextField();
        add(inputField, BorderLayout.SOUTH);

        // Label pour afficher les actions effectuées
        actionLabel = new JLabel(" "); // Initialiser avec un espace
        add(actionLabel, BorderLayout.NORTH); // Ajouter au-dessus du champ de saisie

        // Écouteur pour détecter la touche Entrée
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String input = inputField.getText().trim(); // Effacer les espaces superflus
                    inputField.setText(""); // Effacer le champ de saisie

                    // Traiter uniquement le texte saisi
                    if (!input.isEmpty()) {
                        if (!input.startsWith("!select ") && !input.equals("!copy") && 
                            !input.equals("!cut") && !input.equals("!paste") && 
                            !input.equals("!exit")) {
                            buffer.append(input); // Ajouter le texte au buffer
                            logArea.append("Texte ajouté : " + input + "\n"); // Ajouter log
                            actionLabel.setText("Texte ajouté : \"" + input + "\""); // Mettre à jour le label
                        } else {
                            commands.executeCommand(input); // Exécuter la commande
                            logArea.append("Commande exécutée : " + input + "\n"); // Ajouter log
                            actionLabel.setText("Commande exécutée : \"" + input + "\""); // Mettre à jour le label
                        }
                    }

                    // Quitter l'application si l'utilisateur entre "exit"
                    if (input.equals("!exit")) {
                        logArea.append("Fermeture de l'éditeur.\n");
                        System.exit(0);
                    }

                    // Mettre à jour la zone de texte avec le contenu du buffer
                    textArea.setText(buffer.getText());
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MiniEditeurGUI editeur = new MiniEditeurGUI();
            editeur.setVisible(true);
        });
    }
}
