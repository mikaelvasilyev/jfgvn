import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Game extends JFrame {

    private class TranslucentButton extends JButton {
        private final Color fillColor;

        TranslucentButton(String text, Color baseColor, int initialOpacity) {
            super(text);
            this.fillColor = baseColor;
            setContentAreaFilled(false);
            setOpaque(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            int opacity = Math.max(0, Math.min(255, gameSettings.buttonBackgroundOpacity));
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setColor(new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), opacity));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }
    JLabel backgroundLabel = new JLabel();
    JTextArea textArea = new JTextArea();

    JPanel mainPanel = new JPanel(new CardLayout());

    JPanel menuPanel = new JPanel(new GridBagLayout());
    JPanel buttonPanel = new JPanel();
    JPanel gamePanel = new JPanel(new BorderLayout());

    GameSettings gameSettings = new GameSettings();

    int[] pathChoices = new int[4];

    public Game() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        getContentPane().setBackground(Color.BLACK);
        setBackground(Color.BLACK);

        createMainMenu();
        setupGamePanel();

        mainPanel.add(gamePanel, "game");
        mainPanel.setBackground(Color.BLACK);
        gamePanel.setBackground(Color.BLACK);

        setContentPane(mainPanel);

        showScene(GameScenes.getGameScenes());
    }

    private void showCard(String cardName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, cardName);
    }

    private void createMainMenu() {
        menuPanel.setBackground(new Color(14, 14, 32));
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 65), 1),
                BorderFactory.createEmptyBorder(64, 88, 64, 88)
        ));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("НОВЕЛЛА");
        title.setFont(new Font("Serif", Font.BOLD, 54));
        title.setForeground(new Color(248, 248, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 12, 0);
        menuPanel.add(title, gbc);

        JLabel subtitle = new JLabel("Гарри Поттер и Тени Министерства");
        subtitle.setFont(new Font("Serif", Font.PLAIN, 19));
        subtitle.setForeground(new Color(190, 190, 225));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 48, 0);
        menuPanel.add(subtitle, gbc);

        JButton startButton = createMenuButton("НАЧАТЬ ИГРУ");
        startButton.addActionListener(e -> showCard("game"));
        gbc.gridy = 2;
        gbc.insets = new Insets(14, 0, 14, 0);
        menuPanel.add(startButton, gbc);

        JButton settingsButton = createMenuButton("НАСТРОЙКИ");
        settingsButton.addActionListener(e -> showSettingsDialog());
        gbc.gridy = 3;
        menuPanel.add(settingsButton, gbc);

        JButton exitButton = createMenuButton("ВЫХОД");
        exitButton.addActionListener(e -> System.exit(0));
        gbc.gridy = 4;
        menuPanel.add(exitButton, gbc);

        mainPanel.add(menuPanel, "menu");
    }

    private JButton createMenuButton(String text) {
        TranslucentButton button = new TranslucentButton(text, new Color(65, 65, 72), gameSettings.buttonBackgroundOpacity);
        button.setFont(new Font("Serif", Font.BOLD, 22));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(320, 56));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(110, 110, 118), 1),
                BorderFactory.createEmptyBorder(14, 28, 14, 28)
        ));
        return button;
    }

    private void setupGamePanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        backgroundLabel.setBounds(0, 0, 800, 600);
        gamePanel.setLayout(new BorderLayout());

        contentPanel.setOpaque(false);

        JPanel textBackPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int alpha = Math.max(0, Math.min(255, gameSettings.textBackgroundOpacity));
                g2.setColor(new Color(18, 18, 28, alpha));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        textBackPanel.setOpaque(false);

        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setForeground(gameSettings.textColor);
        textArea.setFont(new Font("Serif", Font.BOLD, gameSettings.fontSize));
        textArea.setCaretColor(gameSettings.textColor);
        textArea.setOpaque(false);
        textArea.setMargin(new Insets(14, 18, 14, 18));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        textBackPanel.add(scrollPane, BorderLayout.CENTER);

        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JPanel menuButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        menuButtonPanel.setOpaque(false);
        JButton menuButton = createGamePanelButton("В меню");
        menuButton.addActionListener(e -> showCard("menu"));
        JButton restartButton = createGamePanelButton("Перезапустить игру");
        restartButton.addActionListener(e -> {
            for (int i = 0; i < pathChoices.length; i++) pathChoices[i] = 0;
            showScene(GameScenes.getGameScenes());
            showCard("game");
        });
        menuButtonPanel.add(restartButton);
        menuButtonPanel.add(menuButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(menuButtonPanel, BorderLayout.NORTH);
        centerPanel.add(textBackPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.add(centerPanel, BorderLayout.CENTER);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.add(backgroundLabel, Integer.valueOf(0));
        layeredPane.add(contentPanel, Integer.valueOf(1));
        gamePanel.add(layeredPane, BorderLayout.CENTER);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                updateBackground();
                contentPanel.setBounds(0, getHeight() - 340, getWidth(), 300);
                revalidate();
                repaint();
            }
        });
    }

    private void updateBackground() {
        if (backgroundLabel.getIcon() != null) {
            ImageIcon image = (ImageIcon) backgroundLabel.getIcon();
            Image backgroundImage = image.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            backgroundLabel.setIcon(new ImageIcon(backgroundImage));
            backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    private void showSettingsDialog() {
        JDialog settingsDialog = new JDialog(this, "Настройки", true);
        settingsDialog.setSize(420, 400);
        settingsDialog.getContentPane().setBackground(new Color(22, 22, 38));
        settingsDialog.setLayout(new GridLayout(6, 1, 12, 12));

        Font labelFont = new Font("Serif", Font.PLAIN, 15);
        Color labelColor = new Color(220, 220, 235);

        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        colorPanel.setBackground(new Color(22, 22, 38));
        JLabel colorLabel = new JLabel("Цвет текста:");
        colorLabel.setFont(labelFont);
        colorLabel.setForeground(labelColor);
        colorPanel.add(colorLabel);
        JButton colorButton = new JButton("Выбрать");
        colorButton.setFont(labelFont);
        colorButton.setFocusPainted(false);
        colorButton.setBackground(gameSettings.textColor);
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(settingsDialog, "Выберите цвет", gameSettings.textColor);
            if (newColor != null) {
                gameSettings.textColor = newColor;
                colorButton.setBackground(newColor);
                textArea.setForeground(newColor);
                textArea.setCaretColor(newColor);
            }
        });
        colorPanel.add(colorButton);
        settingsDialog.add(colorPanel);

        JPanel fontSizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        fontSizePanel.setBackground(new Color(22, 22, 38));
        JLabel fontSizeLabel = new JLabel("Размер шрифта:");
        fontSizeLabel.setFont(labelFont);
        fontSizeLabel.setForeground(labelColor);
        fontSizePanel.add(fontSizeLabel);
        JComboBox<Integer> fontSizeBox = new JComboBox<>(new Integer[]{20, 24, 26, 28, 32});
        fontSizeBox.setFont(labelFont);
        fontSizeBox.setSelectedItem(gameSettings.fontSize);
        fontSizeBox.addActionListener(e -> {
            gameSettings.fontSize = (Integer) fontSizeBox.getSelectedItem();
            textArea.setFont(new Font("Serif", Font.BOLD, gameSettings.fontSize));
        });
        fontSizePanel.add(fontSizeBox);
        settingsDialog.add(fontSizePanel);

        JPanel textOpacityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        textOpacityPanel.setBackground(new Color(22, 22, 38));
        JLabel textOpacityLabel = new JLabel("Прозрачность фона текста:");
        textOpacityLabel.setFont(labelFont);
        textOpacityLabel.setForeground(labelColor);
        textOpacityPanel.add(textOpacityLabel);
        JSlider textOpacitySlider = new JSlider(0, 255, gameSettings.textBackgroundOpacity);
        textOpacitySlider.setPreferredSize(new Dimension(160, 28));
        textOpacitySlider.setPaintLabels(false);
        textOpacitySlider.setPaintTicks(true);
        textOpacitySlider.setMajorTickSpacing(85);
        JLabel textOpacityValue = new JLabel(String.valueOf(gameSettings.textBackgroundOpacity));
        textOpacitySlider.addChangeListener(e -> {
            gameSettings.textBackgroundOpacity = textOpacitySlider.getValue();
            textOpacityValue.setText(String.valueOf(gameSettings.textBackgroundOpacity));
        });
        textOpacityValue.setFont(labelFont);
        textOpacityValue.setForeground(labelColor);
        textOpacityPanel.add(textOpacitySlider);
        textOpacityPanel.add(textOpacityValue);
        settingsDialog.add(textOpacityPanel);

        JPanel opacityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        opacityPanel.setBackground(new Color(22, 22, 38));
        JLabel opacityLabel = new JLabel("Прозрачность фона кнопок:");
        opacityLabel.setFont(labelFont);
        opacityLabel.setForeground(labelColor);
        opacityPanel.add(opacityLabel);
        JSlider opacitySlider = new JSlider(0, 255, gameSettings.buttonBackgroundOpacity);
        opacitySlider.setPreferredSize(new Dimension(160, 28));
        opacitySlider.setPaintLabels(false);
        opacitySlider.setPaintTicks(true);
        opacitySlider.setMajorTickSpacing(85);
        JLabel opacityValue = new JLabel(String.valueOf(gameSettings.buttonBackgroundOpacity));
        opacitySlider.addChangeListener(e -> {
            gameSettings.buttonBackgroundOpacity = opacitySlider.getValue();
            opacityValue.setText(String.valueOf(gameSettings.buttonBackgroundOpacity));
        });
        opacityValue.setFont(labelFont);
        opacityValue.setForeground(labelColor);
        opacityPanel.add(opacitySlider);
        opacityPanel.add(opacityValue);
        settingsDialog.add(opacityPanel);

        JButton applyButton = new JButton("Применить");
        applyButton.setFont(new Font("Serif", Font.BOLD, 15));
        applyButton.setBackground(new Color(55, 55, 75));
        applyButton.setForeground(Color.WHITE);
        applyButton.setFocusPainted(false);
        applyButton.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        applyButton.addActionListener(e -> {
            mainPanel.repaint();
            settingsDialog.dispose();
        });
        settingsDialog.add(applyButton);

        JButton cancelButton = new JButton("Отмена");
        cancelButton.setFont(new Font("Serif", Font.PLAIN, 15));
        cancelButton.setBackground(new Color(45, 45, 62));
        cancelButton.setForeground(labelColor);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        cancelButton.addActionListener(e -> settingsDialog.dispose());
        settingsDialog.add(cancelButton);

        settingsDialog.setLocationRelativeTo(this);
        settingsDialog.setVisible(true);
    }

    private void showScene(Scene scene) {
        ImageIcon image = new ImageIcon("./images/" + scene.backgroundImage);
        Image backgroundImage = image.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(backgroundImage));
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());

        StringBuilder sb = new StringBuilder();
        for (String replica : scene.replicas) {
            String line = cleanReplica(replica);
            if (!line.isEmpty()) {
                if (sb.length() > 0) sb.append("\n\n");
                sb.append(line);
            }
        }
        textArea.setText(normalizeDisplayText(sb.toString()));
        textArea.setCaretPosition(0);

        buttonPanel.removeAll();
        createButtons(scene);

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private String cleanReplica(String replica) {
        if (replica == null) return "";
        String trimmed = replica.trim();
        if (trimmed.isEmpty()) return "";

        String[] prefixes = {
                "Фон:", "Персонаж:", "Персонажи:", "Звуки:", "Текст:", "Визуал:", "Заставка:"
        };

        for (String prefix : prefixes) {
            if (trimmed.startsWith(prefix)) {
                String without = trimmed.substring(prefix.length()).trim();
                return without.isEmpty() ? "" : without;
            }
        }

        return trimmed;
    }

    private String normalizeDisplayText(String raw) {
        if (raw == null || raw.isEmpty()) return "";
        return raw.replaceAll("(\\n\\s*){3,}", "\n\n").trim();
    }

    private void createButtons(Scene scene) {
        if (scene.choices != null && scene.choices.length > 0) {
            boolean isFinalChoice = "Выбор: Финал пути".equals(scene.name);
            for (int i = 0; i < scene.choices.length; i++) {
                final int choiceIndex = i;
                JButton button = createButton(scene.choices[i], e -> {
                    if (isFinalChoice) {
                        pathChoices[3] = choiceIndex;
                        Scene end = GameScenes.getEnding(pathChoices[0], pathChoices[1], pathChoices[2], pathChoices[3]);
                        if (end != null) showScene(end);
                    } else if ("Выбор: Риск".equals(scene.name)) {
                        pathChoices[0] = choiceIndex;
                        if (scene.scenesChoices != null && choiceIndex < scene.scenesChoices.length)
                            showScene(scene.scenesChoices[choiceIndex]);
                    } else if ("Выбор: Доверие".equals(scene.name)) {
                        pathChoices[1] = choiceIndex;
                        if (scene.scenesChoices != null && choiceIndex < scene.scenesChoices.length)
                            showScene(scene.scenesChoices[choiceIndex]);
                    } else if ("Выбор: Альянс".equals(scene.name)) {
                        pathChoices[2] = choiceIndex;
                        if (scene.scenesChoices != null && choiceIndex < scene.scenesChoices.length)
                            showScene(scene.scenesChoices[choiceIndex]);
                    } else if (scene.scenesChoices != null && choiceIndex < scene.scenesChoices.length) {
                        showScene(scene.scenesChoices[choiceIndex]);
                    }
                });
                buttonPanel.add(button);
                buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } else if (scene.next != null) {
            JButton button = createButton("Далее >>", e -> showScene(scene.next));
            buttonPanel.add(button);
        } else {
            JButton endButton = createButton("Завершить игру", e -> showCard("menu"));
            buttonPanel.add(endButton);
        }
    }

    private JButton createGamePanelButton(String text) {
        TranslucentButton b = new TranslucentButton(text, new Color(55, 55, 62), gameSettings.buttonBackgroundOpacity);
        b.setFont(new Font("Serif", Font.BOLD, 16));
        b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(180, 42));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(115, 115, 122), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        return b;
    }

    private JButton createButton(String text, ActionListener listener) {
        TranslucentButton button = new TranslucentButton(text, new Color(65, 65, 72), gameSettings.buttonBackgroundOpacity);
        button.setFont(new Font("Serif", Font.BOLD, 17));
        button.setForeground(Color.WHITE);
        button.setMaximumSize(new Dimension(520, 48));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(110, 110, 118), 1),
                BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));
        button.addActionListener(listener);
        return button;
    }
}