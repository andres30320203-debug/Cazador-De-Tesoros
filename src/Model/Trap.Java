package model;

public class Trap {
    private boolean triggered;

    public Trap() {
        this.triggered = false;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public boolean trigger() {
        if (!triggered) {
            triggered = true;
            return true;
        }
        return false;
    }
}