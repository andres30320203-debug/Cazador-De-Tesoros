package model;

public class Treasure {
    private int value;
    private booleam found;

    public Treasure(int value) {
        this.value = value;
        this.found = false;
    }

    public int getValue() {
        return value;
    }
    public boolean idFound() {
        return found;
    }
    public void setFound(boolean found) {
        this.found = found;
    }

    public int collect() {
        if (!found) {
            found = true;
            return value;
        }
        return 0;
    }
}