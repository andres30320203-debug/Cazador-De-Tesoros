package view;

import javax.swing.*;
import java.awt.*;
import controller.GameController;
import controller.MenuController;
import controller.ScoreController;

public class GameWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private ScorePanel scorePanel;

    private GameController gameController;
    private MenuController menuController;
    private ScoreController scoreController;

    public GameWindow() {
        setTitle("Cazador de Tesoros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        initializeControllers();
        initializePanels();
        setupCardLayout();

        showMenu();
    }

    private void initializeControllers() {
        menuController = new MenuController(this);
        gameController = new GameController(this);
        scoreController = new ScoreController(this);
    }

    private void initializePanels() {
        menuPanel = new MenuPanel(menuController);
        gamePanel = new GamePanel(gameController);
        scorePanel = new ScorePanel(scoreController);
    }

    private void setupCardLayout() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(scorePanel, "SCORES");

        add(mainPanel);
    }

    public void showMenu() {
        cardLayout.show(mainPanel, "MENU");
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    public void showGame(int difficulty) {
        gameController.startNewGame(difficulty);
        cardLayout.show(mainPanel, "GAME");

        int size = getBoardSize(difficulty);
        gamePanel.initializeBoard(size);

        setSize(Math.max(600, size * 50 + 100), Math.max(600, size * 50 + 200));
        setLocationRelativeTo(null);
    }

    public void showScores() {
        scoreController.loadScores();
        cardLayout.show(mainPanel, "SCORES");
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private int getBoardSize(int difficulty) {
        switch(difficulty) {
            case 1: return 8;
            case 2: return 10;
            case 3: return 12
            default: return 8;
        }
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public ScorePanel getScorePanel() {
        return scorePanel;
    }
}