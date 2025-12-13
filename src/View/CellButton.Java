package View;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;

public class CellButton extends JButton {
    private int row;
    private int col;
    private boolean revealed;

    public CellButton(int row, int col) {
        this.row = row;
        this.col = col;
        this.revealed = false;
        setFont(new Font("Arial", Font.BOLD, 16));
        setBackground(new Color(100, 150, 200));
        setForeground(Color.WHITE);
        setText("?");
        setFocusPainted(false);
    }

    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public boolean isRevealed() {
        return revealed;
    }

    public void reveal(String content) {
        revealed = true;
        setText(content);

        if (content.equals("T")) {
            setBackground(new Color(255, 215, 0)); // Color Oro
            setForeground(Color.BLACK);
        } else if (content.equals("X")) {
            setBackground(Color.RED);
            setForeground(Color.WHITE);
        } else if (content.matches("\\d")) {
            int num = Integer.parseInt(content);
            Color[] colors = {
                Color.BLUE,
                Color.GREEN,
                Color.RED,
                Color.MAGENTA,
                Color.ORANGE,
                Color.CYAN,
                Color.GRAY
            };
            if (num < colors.length) {
                setBackground(colors[num]);
            } else {
                setBackground(Color.LIGHT_GRAY);
            }
            setForeground(Color.WHITE);
        } else {
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.DARK_GRAY);
        }

        setEnabled(false);
    }

    public void reset() {
        revealed = false;
        setText("?");
        setBackground(new Color(100, 150, 200));
        setForeground(Color.WHITE);
        setEnabled(true);
    }
}