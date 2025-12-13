package Model;

public class Player {
    private int score;
    private int moves;
    private int treasuresFound;
    private int trapsTriggered;
    private long startTime;
    private long endTime;

    public Player() {
        this.score = 0;
        this.moves = 0;
        this.treasuresFound = 0;
        this.trapsTriggered = 0;
        this.startTime = System.currentTimeMillis();
    }

    public void addMove() {
        moves++;
    }
    public int getMoves() {
        return moves;
    }

    public void addTreasure(int value) {
        treasuresFound++;
        score += value;
    }
    public int getTreasuresFound() {
        return treasuresFound;
    }

    public void triggeredTrap() {
        trapsTriggered++;
    }
    public int getTrapsTriggered() {
        return trapsTriggered;
    }

    public int getScore() {
        return score;
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }
    public void stopTimer() {
        endTime = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        if (endTime > 0) {
            return (endTime - startTime) / 1000;
        }
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    public void reset() {
        score = 0;
        moves = 0;
        treasuresFound = 0;
        trapsTriggered = 0;
        startTime = System.currentTimeMillis();
        endTime = 0;
    }
}