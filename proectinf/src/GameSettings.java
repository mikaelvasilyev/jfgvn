import java.awt.*;

public class GameSettings {
    Color textColor;
    int fontSize;

    public GameSettings(Color textColor, int fontSize) {
        this.textColor = textColor;
        this.fontSize = fontSize;
    }

    public GameSettings() {
        textColor = Color.WHITE;
        fontSize = 26;
    }
}