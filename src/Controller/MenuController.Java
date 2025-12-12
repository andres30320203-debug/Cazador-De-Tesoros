package controller;

import view.GameWindow;

public class MenuController {
    private GameWindow view;

    public MenuController(GameWindow view) {
        this.view = view;
    }

    public void startGame(int difficulty) {
        view.showGame(difficulty)
    }

    public void showScores() {
        view.showScores();
    }

    public void exitGame() {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(view, "¿Estás seguro de que quieres salir?", "Salir del juego", javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}