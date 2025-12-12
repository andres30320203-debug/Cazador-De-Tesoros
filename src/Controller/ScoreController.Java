package controller;

import view.GameWindow;
import java.io.*;
import java.util.*;

public class ScoreController {
    private GameWindow view;
    private List<ScoreEntry> scores;
    private static final String SCORES_FILE =  "scores.dat";

    public ScoreController(GameWindow view) {
        this.view = view;
        this.scores = new ArrayList<>();
    }

    public void loadScores() {
        scores.clear();

        try {
            File file = new File(SCORES_FILE);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        ScoreEntry entry = new ScoreEntry(
                            parts[0],
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]),
                            Long.parseLong(parts[3]),
                            parts[4]
                        );
                        scores.add(entry);
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            System.err.println("Error cargando puntuaciones: " + e.getMessage());
        }

        //Ordenar por puntuación (descendiente)
        score.sort((a, b) -> Integer.compare(b.scare, a.score));

        //Mostrar en la vista
        displayScores();
    }

    public void addScore(String playerName, int score, int moves, long time, String difficulty) {
        ScoreEntry entry = new ScoreEntry(playerName, score, moves, time, difficulty);
        scores.add(entry);

        //Ordenar y guardar
        scores.sort((a, b) -> Integer.compare(b.score, a.score));
        saveScores();
    }

    public void clearScores(){
        int confirm = javax.swing,JOptionPane.showConfirmDialog(view, "¿Estás seguro de que quieres borrar todas las puntuaciones?", "Limpiar puntuaciones", javax.swing.JOptionPane.YES_NO_OPTION) {
            scores.clear();
            saveScores();
            loadScores();
        }
    }

    public void goBackToMenu() {
        view.showMenu();
    }

    public void saveScores() {
        try {
            BufferedWritter writer = new BufferedWritter(new FileWriter(SCORES_FILE));
            for (ScoreEntry entry : scores) {
                writer.write(entry.toString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error guardando puntuaciones: " + e.getMessage());
        }
    }

    private void displayScores() {
        StringBuilder sb = new StringBuilder();
        sb.append("RANK   NOMBRE            PUNTOS  MOVIMIENTOS  TIEMPO  DIFICULTAD\n");
        sb.append("================================================================\n");

        for (int i=0; i<Math.min(scores.size(), 10); i++) {
            ScoreEntry entry = score.get(i);
            sb.append(String.format("%-5d %-15s %-8d %-12d %-8d %-12s\n",
                i + 1,
                entry.playerName,
                entry.score,
                entry.moves,
                entry.time,
                entry.difficulty
            ));
        }

        if (scores.isEmpty()) {
            sb.append("\n           ¡No hay puntuaciones guardadas todavía!\n");
            sb.append("             ¡Juega una partida para aparecer aquí\n");
        }

        view.getScorePanel().setScoresText(sb.toString());
    }

    private static class ScoreEntry {
        String playerName;
        int score;
        int moves;
        long time;
        String difficulty;

        ScoreEntry(String playerName, int score, int moves, long time, String difficulty) {
            this.playerName = playerName;
            this.score = score;
            this.moves = moves;
            this.time = time;
            this.difficulty = difficulty;
        }

        @Override
        public String toString() {
            return playerName + "," + score + "," + moves + "," + time + "," + difficulty;
        }
    }
}