package view;

import javax.swing.*;
import java.awt.*;
import controller.ScoreController;

public class ScorePanel extends JPanel {
    private ScoreController controller;
    private JTextArea scoresArea;
    private JButton backButton;

    public ScorePanel(ScoreController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        //Titulo
        JLabel titleLabel = new JLabel("MEJORES PUNTUACIONES", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        add(titleLabel, BorderLayout.NORTH);

        //Area de puntuaciones
        scoresArea = new JTextArea();
        scoresArea.setEditable(false);
        scoresArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        scoresArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(scoresArea);
        scrollPlane.setBorder(BorderFactory.createTitleBorder("Puntuaciones"));
        
        add(scrollPane, BorderLayout.CENTER);

        //Panel de controles
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        backButton = new JButton("Volver al Menú");
        backButton.addActionListener(e -> controller.goBackToMenu());

        JButton clearButton = new JButton("Limpiar Puntuaciones");
        clearButton.addActionListener(e -> controller.clearScores());

        controlPanel.add(backButton);
        controlPanel.add(clearButton);

        add(controlPanel, BorderLayout.SOUTH);
    }

    public void setScoresText(String text) {
        scoresArea.setText(text);
    }
}