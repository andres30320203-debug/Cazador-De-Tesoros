package model;

import java.util.Random;

public class GameBoard {
    private Cell[][] board;
    private int size;
    private int totalTreasures;
    private int totalTraps;
    private int treasuresRemaining;
    private int trapsRemaining;
    private GameState state;
    private Player player;
    private Random random;

    public GameBoard(int size, int difficulty) {
        this.size = size;
        this.random = new Random();
        this.player = new Player();
        this.state = GameState.NOT_STARTED;
        initializeBoard(difficulty);
    }

    private void initializeBoard(int difficulty) {
        board = new Cell[size][size];
        // Inicializar celdas
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                board[i][j] = new Cell(i, j);
            }
        }

        // Configuración según dificultad
        switch(difficulty) {
            case 1: // Fácil
                totalTreasures = 5; 
                totalTraps = 3; 
                break;
            case 2: // Medio
                totalTreasures = 8; 
                totalTraps = 6; 
                break;
            case 3: // Difícil
                totalTreasures = 12; 
                totalTraps = 10; 
                break;
            default:
                totalTreasures = 8; 
                totalTraps = 6; 
        }

        treasuresRemaining = totalTreasures;
        trapsRemaining = totalTraps;

        placeItems(Cell.CellType.TREASUURE, totalTreasures);

        placeItems(Cell.CellType.TRAP, totalTraps);

        calculateAdjacentTreasures();

        state = GameState.PLAYING;
        player.startTimer();
    }

    private void placeItems(Cell.CellType type, int count) {
        int placed = 0;
        while (placed < count) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            if (board[row][col].isEmpty()) {
                board[row][col].setType(type);
                placed++;
            }
        }
    }

    private void calculateAdjacentTreasures() {
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                if (board[i][j].isEmpty()) {
                    int count = countAdjacentTreasures(i, j);
                    board[i][j].setAdjacentTreasures(count);
                }
            }
        }
    }

    private int countAdjacentTreasures(int row, int col) {
        int count = 0;
        for (int i=Math.max(0, row-1); i<=Math.min(size-1, row+1); i++) {
            for (int j=Math.max(0, col-1); j<=Math.min(size-1, col+1); j++) {
                if (board[i][j].isTreasure()) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean revealCell(int row, int col) {
        if (row<0 || row>=size || col<0 || col>=size) return false;

        Cell cell = board[row][col];
        if (cell.isRevealed()) return false;

        player.addMove();
        cell.setRevealed(true);

        if(cell.isTreasure()) {
            player.addTreasure(100);
            treasuresRemaining--;
            checkVictory();
            return true;
        } else if (cell.isTrap()) {
            player.triggeredTrap();
            state = GameState.GAME_OVER;
            player.stopTimer();
            return true;
        } else {
            // Si es una celda vacía, revelar celdas adyacentes si no hay tesoros cerca
            if (cell.getAdjacentTreasures() == 0) {
                revealAdjacentCells(row, col);
            }
            return true;
        }
    }

    private void revealedAdjacentCells(int row, int col) {
        for (int i=Math.max(0, row-1); i<=Math.min(size-1, row+1); i++) {
            for (int j=Math.max(0, col-1); j<=Math.min(size-1, col+1); j++) {
                if (i==row && j==col) continue;
                if (!board[i][j].isRevealed() && board[i][j].isEmpty()) {
                    board[i][j].setRevealed(true);
                    if (board[i][j].getAdjacentTreasures() == 0) {
                        revealAdjacentCells(i, j);
                    }
                }
            }
        }
    }

    private void checkVictory() {
        if (treasuresRemaining == 0) {
            state = GameState.VICTORY;
            player.stopTimer();
        }
    }

    public Cell getCell(int row, int col) {
        return board[row][col];
    }

    public int getSize() {
        return size;
    }
    public GameState getState() {
        return state;
    }

    public Player getPlayer() {
        return player;
    }

    public int getTreasuresRemaining() {
        return treasuresRemaining;
    }

    public int getTotalTreasures() {
        return totalTreasures;
    }

    public void reset(int difficulty) {
        initializeBoard(difficulty);
        player.reset();
    }
}