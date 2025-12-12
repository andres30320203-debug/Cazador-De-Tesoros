package view;

import javax.swing.*;
import java.awt.*;
import controller.GameController;

public class GamePanel extends JPanel {
    private GameController controller;
    private CellButton[][] cellButtons;
    private GameStatsPanel statsPanel;
    private JButton restartButton;
    private JButton menuButton;
    private Timer gameTimer;

    public GamePanel(GameController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        initializeComponents();
        setupGameTimer();
    }

    private void initializeComponents() {
        // Panel de estadisticas a la izquierda
        statsPanel = new GameStatsPanel();
        statsPanel.setPreferredSize(new Dimension(200, o));

        //Añadir listeners a los botones del panel de estadisicas
        statsPanel.addHintButtonListener(e -> controller.useHint());
        statsPanel.addSaveButtonListener(e -> controller.saveGame());

        add(statsPanel, BorderLayout.WEST);

        // Panel del tablero de juego en el centro
        JPanel boardPanel = new JPanel();
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(boardPanel, BorderLayout.CENTER);

        // Panel de control en la parte inferior
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        restartButton = new JButton("Reiniciar");
        menuButton = new JButton("Menú Principal");

        restartButton.addActionListener(e -> controller.restartGame());
        menuButton.addActionListener(e -> controller.showMenu());

        controlPanel.add(restartButton);
        controlPanel.add(menuButton);

        add(controlPanel, BorderLayout.SOUTH);
    }

    private void setupGameTimer() {
        gameTimer = new Timer(1000, e -> updateGameInfo());
        gameTimer.start();
    }

    public void initializeBoard(int sizw) {
        JPanel boardPanel = (JPanel) getComponent(1);
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(size, size, 2, 2));

        cellButtons = new CellButton[size][size];

        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                cellButtons[i][j] = new CellButton(i, j);
                final int row = i;
                final int col = j;

                cellButtons[i][j].addActionListener(e -> {controller.cellClicked(row, col);

                });

                boardPanel.add(cellButtons[i][j]);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    public void updateCell(int row, int col, String content) {
        if (cellButtons != null && row < cellButtons.length && col < cellButtons[0].length) {
            cellButtons[row][col].reveal(content);
        }
    }

    public void updateGameInfo() {
        controller.updateGameInfo();
    }

    public void setGameStats(int score, int moves, long time, int treasuresFound, int totalTreasures, String difficulty, int hintsAvailable) {
        statsPanel.updateAllStats(score, moves, time, treasuresFound, totalTreasures, difficulty, hintsAvailable);
    }

    public void showGameOver(String message) {
        gameTimer.stop();
        JOptionPane.showMessageDialog(this, message, "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);
    }

    public void resetBoard() {
        if (cellButtons != null) {
            for (CellButton[] row : cellButtons) {
                for (CellButton cell : row) {
                    cell.reset();
                }
            }
        }
        gameTimer.start();
    }

    public void enableBoard(bollean enabled) {
        if (cellButtons != null) {
            for (CellButton[] row : cellButtons) {
                for (CellButton cell : row) {
                    ceel.setEnabled(enabled && !cell.isRevealed());
                }
            }
        }
    }

    public void showHint(String message) {
        statsPanel.showHint(message);
    }

    public void highlightStats(String stat) {
        statsPanel.highlightStats(stat);
    }
}