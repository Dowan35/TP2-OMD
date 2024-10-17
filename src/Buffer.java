public class Buffer {
    // 1: texte contenu dans buffer
    private StringBuilder text = new StringBuilder();

    public void append(String str) {
        text.append(str).append("\n");
    }

    public void delete(int start, int end) {
        if (!(start < 0 || end > text.length())) {
            text.delete(start, end);
        }
    }

    public String getText() {
        return text.toString();
    }

    public void appendAt(int index, String str) {
        if (index == -1) {
            text.append(str);
        } else {
            text.insert(index, str);
        }
    }

    public void setText(String newText) {
        text = new StringBuilder(newText); // Permet de réinitialiser le texte
    }
}
