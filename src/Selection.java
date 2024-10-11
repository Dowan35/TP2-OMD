public class Selection {
    private int start;
    private int end;

    public void setSelection(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public String getSelectedText(String text) {
        if (start >= 0 && start < end) {
            if (end > text.length()){
                end = text.length();
            }
            return text.substring(start, end);
        }

        return ""; // Retourne une chaîne vide si la sélection est invalide
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
