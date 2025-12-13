package Controller;

import Model.GameBoard;
import Model.GameState;
import Model.Cell;
import View.GameWindow;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController {
    private GameWindow view;
    private GameBoard Model;
    private int currentDifficulty;
    private int hintsAvailable;
    private Timer hintTimer;
    
    public GameController(GameWindow view) {
        this.view = view;
        this.hintsAvailable = 3;
    }
    
    public void startNewGame(int difficulty) {
        this.currentDifficulty = difficulty;
        this.hintsAvailable = 3;
        int size = getBoardSize(difficulty);
        Model = new GameBoard(size, difficulty);
        view.getGamePanel().resetBoard();
        view.getGamePanel().enableBoard(true);
        updateGameInfo();
        
        // Inicializar el hint timer
        setupHintTimer();
    }
    
    private void setupHintTimer() {
        if (hintTimer != null) {
            hintTimer.stop();
        }
        
        hintTimer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hintsAvailable < 5 && Model != null && Model.getState() == GameState.PLAYING) {
                    hintsAvailable++;
                    updateGameInfo();
                    view.getGamePanel().showHint("¡Has ganado una pista extra! Total: " + hintsAvailable);
                }
            }
        });
        
        hintTimer.start();
    }
    
    public void cellClicked(int row, int col) {
        if (Model == null || Model.getState() != GameState.PLAYING) return;
        
        boolean result = Model.revealCell(row, col);
        if (result) {
            Cell cell = Model.getCell(row, col);
            view.getGamePanel().updateCell(row, col, cell.toString());
            updateGameInfo();
            
            // Resaltar estadística si se encuentra tesoro
            if (cell.isTreasure()) {
                view.getGamePanel().highlightStats("treasures");
                view.getGamePanel().highlightStats("score");
            }
            
            // Verificar estado del juego
            GameState state = Model.getState();
            if (state == GameState.GAME_OVER) {
                hintTimer.stop();
                view.getGamePanel().showGameOver("¡Has caído en una trampa!\n\n" +
                    "Resumen de la partida:\n" +
                    "Puntuación: " + Model.getPlayer().getScore() + "\n" +
                    "Movimientos: " + Model.getPlayer().getMoves() + "\n" +
                    "Tiempo: " + Model.getPlayer().getElapsedTime() + "s\n" +
                    "Tesoros encontrados: " + (Model.getTotalTreasures() - Model.getTreasuresRemaining()) + "/" + Model.getTotalTreasures());
                view.getGamePanel().enableBoard(false);
                saveScore();
            } else if (state == GameState.VICTORY) {
                hintTimer.stop();
                view.getGamePanel().showGameOver("¡Felicidades! ¡Has encontrado todos los tesoros!\n\n" +
                    "Resumen de la partida:\n" +
                    "Puntuación: " + Model.getPlayer().getScore() + "\n" +
                    "Movimientos: " + Model.getPlayer().getMoves() + "\n" +
                    "Tiempo: " + Model.getPlayer().getElapsedTime() + "s\n" +
                    "Eficiencia: " + String.format("%.2f", (Model.getPlayer().getScore() / (double)Model.getPlayer().getMoves())) + " pts/mov");
                view.getGamePanel().enableBoard(false);
                saveScore();
            }
        }
    }
    
    public void updateGameInfo() {
        if (Model != null) {
            // Actualizar todas las estadísticas en el GameStatsPanel
            view.getGamePanel().setGameStats(
                Model.getPlayer().getScore(),
                Model.getPlayer().getMoves(),
                Model.getPlayer().getElapsedTime(),
                Model.getTotalTreasures() - Model.getTreasuresRemaining(),
                Model.getTotalTreasures(),
                getDifficultyName(currentDifficulty),
                hintsAvailable
            );
        }
    }
    
    public void useHint() {
        if (Model == null || Model.getState() != GameState.PLAYING) {
            view.getGamePanel().showHint("No hay una partida en curso.");
            return;
        }
        
        if (hintsAvailable <= 0) {
            view.getGamePanel().showHint("¡No te quedan pistas! Espera 30 segundos para obtener otra.");
            return;
        }
        
        // Buscar un tesoro no encontrado para dar pista
        boolean hintGiven = false;
        int boardSize = Model.getSize();
        
        for (int i = 0; i < boardSize && !hintGiven; i++) {
            for (int j = 0; j < boardSize && !hintGiven; j++) {
                Cell cell = Model.getCell(i, j);
                if (cell.isTreasure() && !cell.isRevealed()) {
                    // Dar pista sobre celdas adyacentes
                    for (int x = Math.max(0, i - 1); x <= Math.min(boardSize - 1, i + 1) && !hintGiven; x++) {
                        for (int y = Math.max(0, j - 1); y <= Math.min(boardSize - 1, j + 1) && !hintGiven; y++) {
                            if (x == i && y == j) continue;
                            
                            Cell adjacent = Model.getCell(x, y);
                            if (adjacent.isEmpty() && adjacent.isRevealed()) {
                                // Dar pista basada en esta celda revelada
                                hintsAvailable--;
                                updateGameInfo();
                                
                                String direction = getDirection(x, y, i, j);
                                view.getGamePanel().showHint("¡Pista utilizada! Hay un tesoro " + direction + 
                                    " de la celda [" + (x+1) + "," + (y+1) + "]\n" +
                                    "Pistas restantes: " + hintsAvailable);
                                hintGiven = true;
                                view.getGamePanel().highlightStats("score");
                            }
                        }
                    }
                }
            }
        }
        
        if (!hintGiven) {
            // Si no se pudo dar pista específica, dar información general
            hintsAvailable--;
            updateGameInfo();
            
            int unrevealedTreasures = Model.getTreasuresRemaining();
            view.getGamePanel().showHint("¡Pista utilizada!\n" +
                "Tesoros restantes: " + unrevealedTreasures + "\n" +
                "Busca en áreas con más números altos alrededor\n" +
                "Pistas restantes: " + hintsAvailable);
        }
    }
    
    private String getDirection(int fromRow, int fromCol, int toRow, int toCol) {
        if (toRow < fromRow) {
            if (toCol < fromCol) return "arriba-izquierda";
            if (toCol > fromCol) return "arriba-derecha";
            return "arriba";
        } else if (toRow > fromRow) {
            if (toCol < fromCol) return "abajo-izquierda";
            if (toCol > fromCol) return "abajo-derecha";
            return "abajo";
        } else {
            if (toCol < fromCol) return "izquierda";
            return "derecha";
        }
    }
    
    public void saveGame() {
        if (Model == null) {
            view.getGamePanel().showHint("No hay partida para guardar.");
            return;
        }
        
        int choice = javax.swing.JOptionPane.showConfirmDialog(
            view,
            "¿Deseas guardar la partida actual?\n" +
            "Puntuación: " + Model.getPlayer().getScore() + "\n" +
            "Tesoros: " + (Model.getTotalTreasures() - Model.getTreasuresRemaining()) + "/" + Model.getTotalTreasures() + "\n" +
            "Tiempo: " + Model.getPlayer().getElapsedTime() + "s",
            "Guardar Partida",
            javax.swing.JOptionPane.YES_NO_OPTION
        );
        
        if (choice == javax.swing.JOptionPane.YES_OPTION) {
            // En una implementación real, aquí guardarías el estado del juego
            String playerName = javax.swing.JOptionPane.showInputDialog(
                view,
                "Ingresa un nombre para la partida guardada:",
                "Nombre de partida",
                javax.swing.JOptionPane.QUESTION_MESSAGE
            );
            
            if (playerName != null && !playerName.trim().isEmpty()) {
                // Simulación de guardado
                view.getGamePanel().showHint("Partida '" + playerName + "' guardada exitosamente.\n" +
                    "Puedes continuar jugando.");
                
                // En una implementación real, guardarías:
                // - Estado del tablero
                // - Puntuación del jugador
                // - Tiempo transcurrido
                // - Celdas reveladas
                // - Pistas restantes
            }
        }
    }
    
    public void restartGame() {
        if (Model != null) {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                view,
                "¿Estás seguro de que quieres reiniciar la partida?\n" +
                "Se perderá el progreso actual.",
                "Reiniciar Partida",
                javax.swing.JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                startNewGame(currentDifficulty);
            }
        }
    }
    
    public void showMenu() {
        if (Model != null && Model.getState() == GameState.PLAYING) {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                view,
                "¿Estás seguro de que quieres volver al menú?\n" +
                "Se perderá el progreso de la partida actual.",
                "Volver al Menú",
                javax.swing.JOptionPane.YES_NO_OPTION
            );
            
            if (confirm != javax.swing.JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        if (hintTimer != null) {
            hintTimer.stop();
        }
        
        view.showMenu();
    }
    
    private int getBoardSize(int difficulty) {
        switch(difficulty) {
            case 1: return 8;
            case 2: return 10;
            case 3: return 12;
            default: return 8;
        }
    }
    
    private String getDifficultyName(int difficulty) {
        switch(difficulty) {
            case 1: return "Fácil";
            case 2: return "Medio";
            case 3: return "Difícil";
            default: return "Desconocido";
        }
    }
    
    private void saveScore() {
        if (Model.getPlayer().getScore() > 0) {
            String playerName = javax.swing.JOptionPane.showInputDialog(
                view, 
                "¡Nueva puntuación alta!\nIngresa tu nombre para guardarla:",
                "Guardar Puntuación",
                javax.swing.JOptionPane.QUESTION_MESSAGE
            );
            
            if (playerName != null && !playerName.trim().isEmpty()) {
                ScoreController scoreController = new ScoreController(view);
                scoreController.addScore(
                    playerName,
                    Model.getPlayer().getScore(),
                    Model.getPlayer().getMoves(),
                    Model.getPlayer().getElapsedTime(),
                    getDifficultyName(currentDifficulty)
                );
                
                view.getGamePanel().showHint("¡Puntuación de " + playerName + " guardada exitosamente!");
            }
        }
    }
    
    // Método para limpiar recursos
    public void cleanup() {
        if (hintTimer != null) {
            hintTimer.stop();
        }
    }
    
    // Getters para información del juego
    public int getCurrentScore() {
        return Model != null ? Model.getPlayer().getScore() : 0;
    }
    
    public int getRemainingTreasures() {
        return Model != null ? Model.getTreasuresRemaining() : 0;
    }
    
    public GameState getGameState() {
        return Model != null ? Model.getState() : GameState.NOT_STARTED;
    }
}