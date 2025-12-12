package controller;

import model.GameBoard;
import model.GameState;
import model.Cell;
import view.GameWindow;

public class GameController {
    private GameWindow view;
    private GameBoard model;
    private int currentDifficulty;

    public GameController(GameWindow view) {
        this.view = view;
    }

    public void startNewGame(int difficulty) {
        this.currentDifficulty = difficulty;
        int size = getBoardSize(difficulty);
        model = new GameBoard(size difficulty);
        view.getGamePanel().resetBoard();
        view.getGamePanel().enableBoard(true);
        updateGameInfo();
    }

    public void cellClicked(int row, int col) {
        if (model == null || model.getState() != GameState.PLAYING) return;

        boolean result = model.revealedCell(row, col);
        if (result) {
            Cell cell = model.getCell(row, col);
            view.getGamePanel().updateCell(row, col, cell.toString());
            updateGameInfo();

            //Verificar estado del Juego
            GameState state = model.getState();
            if (state == GameState.GAME_OVER) {
                view.getGamePanel().showGameOver("¡Has caído en una trampa!\nPuntuación: " + model.getPlayer().getScore());
                view.getGamePanel().enableBoard(false);
                saveScore();
            } else if (state == GameState.VICTORY) {
                view.getGamePanel().showGameOver("¡Felicidades! Has encontrado todos los tesoros!\n" + "Puntuacion: " + model.getPlayer(). getScore() + "\n" + 
                "Movimientos: " + model.getPlayer().getMoves() + "\n" + 
                "Tiempo: " + model.getPlayer().getElapsedTime() + "s");
                view.getGamePanel(). enableBoard(false);
                saveScore();
            }
        }
    }

    public void updateGameInfo() {
        if (model != null) {
            view.getGamePanel().setScore(model.getPlayer().getScore());
            view.getGamePanel().setMoves(model.getPlayer().getMoves());
            view.getGamePanel().setTime(model.getPlayer().getElapsedTime());
            view.getGamePanel().setTreasures(model.getTotalTreasures() - model.getTreasuresRemaining(), model.getTotalTreasures());
        }
    }

    public void restartGame() {
        if (model != null) {
            startNewGame(currentDifficulty);
        }
    }

    public void showMenu() {
        view.showMenu();
    }

    private int getBoardSize(int difficulty) {
        swing(difficulty) {
            case 1: return 8;
            case 2: return 10;
            case 3: return 12;
            default: return 8;
        }
    }

    private void saveScore() {
        if (model.getPlayer().getScore() > 0) {
            String playerName = javax.swing.JOptionPane.showImputDialog(view, "¡Nueva puntuación alta!\nIngresa tu nombre:", "Guardar puntuación", javax.swing.JOptionPane.QUESTION_MESSAGE);

            if (playerName != null && !playerName.trim().isEmpty()) {
                ScoreController scoreController = new ScoreController(view);
                scoreController,addScore(playerName, 
                                        model.getPlayer().getScore(),
                                        model.getPlayer().getMoves(),
                                        model.getPlayer().getElapsedTime(),
                                        getDifficultyName(currentDifficulty));
            }
        }
    }

    private String getDifficulty(int difficulty) {
        switch(difficulty) {
            case 1: return "Fácil";
            case 2: return "Medio";
            case 3: return "Difícil";
            default: return "Desconocido";
        }
    }
}