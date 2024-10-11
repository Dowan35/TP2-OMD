public class ClipboardManager {
    private String clipboard = "";

    public void copy(String text) {
        clipboard = text;
    }

    public String paste() {
        return clipboard;
    }
}
