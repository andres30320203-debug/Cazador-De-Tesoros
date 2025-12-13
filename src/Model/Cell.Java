package Model;

public class Cell {
    public enum CellType {
        EMPTY, TREASURE, TRAP, PLAYER
    }

    private CellType type;
    private boolean revealed;
    private int adjacentTreasures;
    private int row;
    private int col;

    public Cell(int row, int col) {
        this.row = row;
        this.col =col;
        this.type = CellType.EMPTY;
        this.revealed = false;
        this.adjacentTreasures = 0;
    }

    public CellType getType() {
        return type;
    } 
    public void setType(CellType type) {
        this.type = type;
    }

    public boolean isRevealed() {
        return revealed;
    }
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public int getAdjacentTreasures() {
        return adjacentTreasures;
    }
    public void setAdjacentTreasures(int count) {
        this.adjacentTreasures = count;
    }

    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }

    public boolean isEmpty() {
        return type == CellType.EMPTY;
    }
    public boolean isTreasure() {
        return type == CellType.TREASURE;
    }
    public boolean isTrap() {
        return type == CellType.TRAP;
    }

    @Override
    public String toString() {
        if (!revealed) return "?";
        switch (type) {
            case TREASURE: return "T";
            case TRAP: return "X";
            case EMPTY: return adjacentTreasures > 0 ? String.valueOf(adjacentTreasures) : ".";
            default: return " ";
        }
    }
}