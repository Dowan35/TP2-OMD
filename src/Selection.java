public class Selection {
    private int start = -1;
    private int end = -1;

    public void setSelection(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public String getSelectedText(String text) {

        if (start >= 0 && start < end) {
            if (end > text.length()) {
                end = text.length();
            }
            return text.substring(start, end);
        }

        return ""; // Retourne une chaîne vide si la sélection est invalide
    }

    public void selectAllText(String text) {
        this.start = 0;
        this.end = text.length();
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
