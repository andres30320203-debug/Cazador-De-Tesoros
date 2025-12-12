package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameStatsPanel extends JPanel {
    private JLabel scoreValueLabel;
    private JLabel movesValueLabel;
    private JLabel timeValueLabel;
    private JLabel treasuresValueLabel;
    private JLabel difficultyValueLabel;
    private JButton hintButton;
    private JButton saveButton;

    public GameStatsPanel() {
        setLayout(new GridLayout(6, 2, 10, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10,));
        setBackground(new Color(240, 240, 245));

        initializeComponents();
    }

    private void initializeComponents() {
        // Etiquetas
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font valueFont = new Font("Arial", Font.PLAIN, 14);

        //Puntuación
        JLabel scoreLabel = new JLabel("Puntuación:");
        scoreLabel.setFont(labelFont);
        scoreValueLabel = new JLabel("0");
        scoreValueLabel.setFont(valueFont);
        scoreValueLabel.setForeground(new Color(0, 100, 0));

        //Movimientos
        JLabel movesLabel = new JLabel("Movimientos:");
        movesLabel.setFont(labelFont);
        movesValueLabel = new JLabel("0");
        movesValueLabel.setFont(valueFont);

        //Tiempo
        JLabel timeLabel = new JLabel("Tiempo Transcurrido:");
        timeLabel.setFont(labelFont);
        timeValueLabel = new JLabel("00:00");
        timeValueLabel.setFont(valueFont);
        timeValueLabel.setForeground(new Color(0, 0, 150));

        //Tesoros 
        JLabel treasuresLabel = new JLabel("Tesoros Encontrados:");
        treasuresLabel.setFont(labelFont);
        treasuresValueLabel = new JLabel("0/0");
        treasuresValueLabel.setFont(valueFont);
        treasuresValueLabel.setForeground(new Color(255, 140, 0));

        //Dificultad
        JLabel difficultyLabel = new JLabel("Dificultad:");
        difficultyLabel.setFont(labelFont);
        difficultyValueLabel = new JLabel("Fácil");
        difficultyValueLabel.setFont(valueFont);

        //Botones
        hintButton = new JButton("Pista (3)");
        hintButton.setFont(new Font("Arial", Font.PLAIN, 12));
        hintButton.setBackground(new Color(100, 200, 100));
        hintButton.setForeground(Color.WHITE);
        hintButton.setEbavled(false);

        saveButton = new JButton("Guardar Juego");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 12));
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);

        //Añadir componentes
        add(scoreLabel);
        add(scoreValueLabel);

        add(movesLabel);
        add(movesValueLabel);

        add(timeLabel);
        add(timeValueLabel);

        add(treasuresLabel);
        add(treasuresValueLabel);

        add(difficultyLabel);
        add(difficultyValueLabel);

        add(hintButton);
        add(saveButton);
    }

    // Métodos para actualización de estadísticas
    public void updateScore(int score) {
        scoreValueLabel.setText(String.valueOf(score));
        scoreValueLabel.setForeground(score > 0 ? new Color(0, 100, 0) : Color.BLACK);
    }

    public void updateMoves(int moves) {
        movesValueLabel.setText(String.valueOf(moves));
    }

    public void updateTime(long seconds) {
        timeValueLabel.setText(seconds + "s");
    }

    public void updateTreasures(int found, int total) {
        treasuresValueLabel.setText(found + "/" + total);

        if(found == 0) {
            treasuresValueLabel.setForeground(Color.BLACK);
        } else if (found == total) {
            treasuresValueLabel.setForeground(new Color(0, 100, 0));
        } else {
            trasuresValueLabel.setForeground(new Color(255, 140, 0));
        }
    }

    public void updateDifficulty(String difficulty) {
        difficultyValueLabel.setText(difficulty);

        // Color por dificultad
        switch (difficulty.toLowerCase()) {
            case "facil":
                difficultyValueLabel.setForeground(new Color(0, 150, 0));
                break;
            case "medio":
                difficultyValueLabel.setForeground(new Color(255, 140, 0));
                break;
            case "dificil":
                difficultyValueLabel.setForeground(Color.RED);
                break;
            default:
                difficultyValueLabel.setForeground(Color.BLACK);
        }
    }

    public void updateHints(int hintsAvailable) {
        if (hintsAvailable > 0) {
        hintButton.setText("Pista (" + hintsAvailable + ")");
        hintButton.setEnabled(true);
        hintButton.setBackground(new Color(100,200,100));
        } else {
        hintButton.setText("Sin pistas");
        hintButton.setEnabled(false);
        hintButton.setBackground(Color.LIGHT_GRAY);
        }
    }

    //Métodos para añadir Listeners a los botones
    public void addHintButtonListener(ActionListener listener) {
        hintButton.addActionListener(listener);
    }

    public void addSaveButtonListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    //Método para habilitar/deshabilitar botones
    public void setHintButtonsEnabled(boolean enabled) {
        hintButton.setEnabled(enabled);
    }

    public void setSaveButtonEnabled(boolean enabled) {
        saveButton.setEnabled(enabled);
    }

    //Metodo para mostrar info de la pista
    public void showHint(String message) {
        JOptionPane.showMessageDialog(this, message, "Pista", JOptionPane.INFORMATION_MESSAGE);
    }

    //Metodo para actualizar todo junto
    public void updateAllStats(int score, int moves, long time, int treasuresFound, int totalTreasures, String difficulty, int hintsAvailable) {
        updateScore(score);
        updateMoves(moves);
        updateTime(time);
        updateTreasures(treasuresFound, totalTreasures);
        updateDifficulty(difficulty);
        updateHints(hintsAvailable);
    }

    //Metodo para resaltar estadisticas (pequeño efecto visual :D)
    public void highlightStats() {
        Timer highlightTimer = new Timer(500, e -> {
            switch(stat.toLowerCase()) {
                case "score":
                    scoreValueLabel.setForeground(Color.RED);
                    break;
                case "moves":
                    movesValueLabel.setForeground(Color.RED);
                    break;
                case "treasures":
                    treasuresValueLabel.setForeground(Color.RED);
                    break;
            }
        });

        highlightTimer.setRepeats(false);
        highlightTimer.start();

        //Restaurar color
        Timer restoreTimer = new Timer(1000, e -> {
            switch(stat.toLowerCase()) {
                case "score":
                    scoreValueLabel.setForeground(new Color(0, 100, 0));
                    break;
                case "moves":
                    movesValueLabel.setForeground(Color.BLACK);
                    break;
                case "treasures":
                    treasuresValueLabel.setForeground(new Color(255, 140, 0));
                    break;
            }
        });

        restoreTimer.setRepeats(false);
        restoreTimer.start();
    }
}