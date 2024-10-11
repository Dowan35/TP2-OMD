public class Buffer {
    // 1: texte contenu dans buffer
    private StringBuilder text = new StringBuilder();

    public void append(String str) {
        text.append(str).append("\n");
    }

    public void delete(int start, int end) {
        text.delete(start, end);
    }

    public String getText() {
        return text.toString();
    }
}
