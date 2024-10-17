import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class MiniEditeurGUI extends JFrame {
    private Buffer buffer = new Buffer();
    private ClipboardManager clipboardManager = new ClipboardManager();
    private Selection selection = new Selection();
    private Commands commands;

    private JTextArea textArea;
    private JTextArea logArea; // Zone de log
    private JTextField inputField;
    private JLabel actionLabel; // Label pour les actions effectuées

    // Ensemble contenant les commandes
    private Set<String> commandes;

    public MiniEditeurGUI() {
        commands = new Commands(buffer, clipboardManager, selection);

        // Initialisation des commandes valides
        commandes = new HashSet<>();
        commandes.add("!select");
        commandes.add("!copy");
        commandes.add("!cut");
        commandes.add("!paste");
        commandes.add("!delete");
        commandes.add("!exit");

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

                    if (!input.isEmpty()) {
                        if (isCommand(input)) {

                            if (input.equals("!exit")) {// fermer l'editeur
                                logArea.append("Fermeture de l'éditeur.\n");
                                System.exit(0);
                            }

                            commands.executeCommand(input, logArea);
                            actionLabel.setText("Commande exécutée : \"" + input + "\"");
                        } else {
                            buffer.append(input); // Ajouter du texte au buffer
                            logArea.append("Texte ajouté : " + input + "\n");
                            actionLabel.setText("Texte ajouté : \"" + input + "\"");
                        }
                    }

                    // Mettre à jour la zone de texte avec le contenu du buffer
                    textArea.setText(buffer.getText());
                }
            }
        });
    }

    // Méthode pour vérifier si l'input est une commande
    private boolean isCommand(String input) {
        if (input.startsWith("!select ")) {
            return true;
        }
        return commandes.contains(input);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MiniEditeurGUI editeur = new MiniEditeurGUI();
            editeur.setVisible(true);
        });
    }
}
