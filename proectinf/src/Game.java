import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Game extends JFrame {
    JLabel backgroundLabel = new JLabel();
    JTextArea textArea = new JTextArea();

    JPanel mainPanel = new JPanel(new CardLayout());

    JPanel menuPanel = new JPanel(new GridBagLayout());
    JPanel buttonPanel = new JPanel();
    JPanel gamePanel = new JPanel(new BorderLayout());

    GameSettings gameSettings = new GameSettings();

    public Game() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        createMainMenu();
        setupGamePanel();

        mainPanel.add(gamePanel, "game");

        setContentPane(mainPanel);

        showScene(GameScenes.getGameScenes());
    }

    private void showCard(String cardName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, cardName);
    }

    private void createMainMenu() {
        menuPanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("НОВЕЛЛА");
        title.setFont(new Font("Serif", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0);
        menuPanel.add(title, gbc);

        JButton startButton = createMenuButton("НАЧАТЬ ИГРУ");
        startButton.addActionListener(e -> showCard("game"));
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 0);
        menuPanel.add(startButton, gbc);

        JButton settingsButton = createMenuButton("НАСТРОЙКИ");
        settingsButton.addActionListener(e -> showSettingsDialog());
        gbc.gridy = 2;
        menuPanel.add(settingsButton, gbc);

        JButton exitButton = createMenuButton("ВЫХОД");
        exitButton.addActionListener(e -> System.exit(0));
        gbc.gridy = 3;
        menuPanel.add(exitButton, gbc);

        mainPanel.add(menuPanel, "menu");
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 70, 150));
        button.setPreferredSize(new Dimension(300, 60));
        button.setFocusPainted(false);
        return button;
    }

    private void setupGamePanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        backgroundLabel.setBounds(0, 0, 800, 600);
        gamePanel.setLayout(new BorderLayout());

        contentPanel.setBackground(new Color(0, 0, 0, 200));
        contentPanel.setOpaque(false);

        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setForeground(gameSettings.textColor);
        textArea.setFont(new Font("Serif", Font.BOLD, gameSettings.fontSize));
        textArea.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);

        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JPanel menuButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        menuButtonPanel.setOpaque(false);
        JButton menuButton = new JButton("В меню");
        menuButton.setFont(new Font("Arial", Font.PLAIN, 16));
        menuButton.setForeground(Color.WHITE);
        menuButton.setBackground(new Color(100, 100, 200, 180));
        menuButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        menuButton.setFocusPainted(false);
        menuButton.addActionListener(e -> showCard("menu"));
        menuButtonPanel.add(menuButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(menuButtonPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
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
        settingsDialog.setSize(350, 250);
        settingsDialog.setLayout(new GridLayout(4, 1, 10, 10));

        JPanel colorPanel = new JPanel(new FlowLayout());
        colorPanel.add(new JLabel("Цвет текста:"));
        JButton colorButton = new JButton("Выбрать");
        colorButton.setBackground(gameSettings.textColor);
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(settingsDialog, "Выберите цвет", gameSettings.textColor);
            if (newColor != null) {
                gameSettings.textColor = newColor;
                colorButton.setBackground(newColor);
                textArea.setForeground(newColor);
            }
        });
        colorPanel.add(colorButton);
        settingsDialog.add(colorPanel);

        JPanel fontSizePanel = new JPanel(new FlowLayout());
        fontSizePanel.add(new JLabel("Размер шрифта:"));
        JComboBox<Integer> fontSizeBox = new JComboBox<>(new Integer[]{20, 24, 26, 28, 32});
        fontSizeBox.setSelectedItem(gameSettings.fontSize);
        fontSizeBox.addActionListener(e -> {
            gameSettings.fontSize = (Integer) fontSizeBox.getSelectedItem();
            textArea.setFont(new Font("Serif", Font.BOLD, gameSettings.fontSize));
        });
        fontSizePanel.add(fontSizeBox);
        settingsDialog.add(fontSizePanel);

        JButton applyButton = new JButton("Применить");
        applyButton.addActionListener(e -> settingsDialog.dispose());
        settingsDialog.add(applyButton);

        JButton cancelButton = new JButton("Отмена");
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

        textArea.setText("");
        for (String replica : scene.replicas) {
            textArea.append(replica + "\n\n");
        }
        textArea.setCaretPosition(0);

        buttonPanel.removeAll();
        createButtons(scene);

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void createButtons(Scene scene) {
        if (scene.choices != null && scene.choices.length > 0) {
            for (int i = 0; i < scene.choices.length; i++) {
                final int choiceIndex = i;
                JButton button = createButton(scene.choices[i], e -> {
                    if (scene.scenesChoices != null && choiceIndex < scene.scenesChoices.length) {
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

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLUE);
        button.setMaximumSize(new Dimension(500, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(listener);
        return button;
    }
}