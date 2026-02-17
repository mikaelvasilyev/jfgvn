import java.awt.*;

public class GameSettings {
    Color textColor;
    int fontSize;
    int buttonBackgroundOpacity = 200;
    int textBackgroundOpacity = 220;

    public GameSettings(Color textColor, int fontSize) {
        this.textColor = textColor;
        this.fontSize = fontSize;
    }

    public GameSettings() {
        textColor = new Color(210, 210, 220);
        fontSize = 26;
    }
}