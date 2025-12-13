package View;

import javax.swing.*;
import java.awt.*;
import Controller.MenuController;

public class MenuPanel extends JPanel {
    private MenuController controller;

    public MenuPanel(MenuController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        // Panel principal con imagen de fondo
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(70, 130, 180));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 50);

        //Titulo
        JLabel titleLabel = new JLabel("CAZADOR DE TESOROS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.YELLOW);
        gbc.insets = new Insets(20, 50, 30, 50);
        mainPanel.add(titleLabel, gbc);

        //Subtitulo
        JLabel subtitleLabel = new JLabel("¡Encuentra los tesoros ocultos!", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(0, 50, 30, 50);
        mainPanel.add(subtitleLabel, gbc);

        //Botones de dificultad
        JButton easyButton = createMenuButton("Fácil (8x8)");
        JButton mediumButton = createMenuButton("Medio (10x10)");
        JButton hardButton = createMenuButton("Difícil (12x12)");
        JButton scoreButton = createMenuButton("Puntuaciones");
        JButton exitButton = createMenuButton("Salir");

        easyButton.addActionListener(e -> controller.startGame(1));
        mediumButton.addActionListener(e -> controller.startGame(2));
        hardButton.addActionListener(e -> controller.startGame(3));
        scoreButton.addActionListener(e -> controller.showScores());
        exitButton.addActionListener(e -> System.exit(0));

        gbc.insets = new Insets(10, 50, 10, 50);
        mainPanel.add(easyButton, gbc);
        mainPanel.add(mediumButton, gbc);
        mainPanel.add(hardButton, gbc);
        mainPanel.add(scoreButton, gbc);
        mainPanel.add(exitButton, gbc);

        //Instrucciones
        JTextArea instructions = new JTextArea(
            "INSTRUCCIONES:\n" +
            "1. Haz clic en las celdas para revelarlas\n" +
            "2. Los números indican cuántos tesoros hay cerca\n" +
            "3. Encuentra todos los tesoros para ganar\n" +
            "4. ¡Cuidado con las trampas!"
        );
        instructions.setEditable(false);
        instructions.setBackground(new Color(70, 130, 180));
        instructions.setForeground(Color.WHITE);
        instructions.setFont(new Font("Arial", Font.PLAIN, 14));
        instructions.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        gbc.insets = new Insets(20, 50, 10, 50);
        mainPanel.add(instructions, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(30, 144, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(200, 40));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(65, 105, 225));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 144, 255));
            }
        });
        
        return button;
    }
}