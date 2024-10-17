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
        setMinimumSize(new Dimension(400, 300)); // Taille minimum de la fenêtre
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
        // add(new JScrollPane(logArea), BorderLayout.EAST);
        logArea.setLineWrap(true); // Activer le retour à la ligne

        // // Définir une taille préférée pour le logArea
        // logArea.setPreferredSize(new Dimension(200, 0)); // Largeur de 200 pixels

        // // Ajouter la zone de log dans un JScrollPane
        // add(new JScrollPane(logArea), BorderLayout.EAST);
        
        // Ajouter la zone de log dans un JScrollPane
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setPreferredSize(new Dimension(200, 0)); // Largeur de 200 pixels
        logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        logScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(logScrollPane, BorderLayout.EAST);

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
                        // traiter l'input
                        commands.executeCommand(input, logArea);

                        // Mettre à jour la zone de texte avec le contenu du buffer
                        textArea.setText(buffer.getText());
                        System.out.println(buffer.getText());
                    }
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
