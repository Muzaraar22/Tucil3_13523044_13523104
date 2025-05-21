package src.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BoardPanel extends JPanel {

    private char[][] board;
    private int cellSize = 80;
    public final Map<Character, Color> carColors = new HashMap<>();

    public BoardPanel() {
        this(null, 80);  // Call main constructor with default values
    }

    public BoardPanel(char[][] board, int cellSize) {
        this.cellSize = cellSize;
        generateCarColors();
        setBoard(board);  // Handles null visibility
    }

    public void setBoard(char[][] board) {
        this.board = board;
        this.setVisible(board != null);
        if (board != null) {
            this.setPreferredSize(new Dimension(board[0].length * cellSize, board.length * cellSize));
            this.revalidate();
        }
        this.repaint();
    }

    private void generateCarColors() {
        Color[] palette = {
            Color.BLUE, Color.ORANGE, Color.MAGENTA,
            Color.CYAN, Color.PINK, Color.YELLOW, new Color(150, 75, 0),
            new Color(128, 0, 128), new Color(64, 224, 208)
        };
        int colorIndex = 0;

        for (char c = 'A'; c <= 'Z'; c++) {
            carColors.put(c, palette[colorIndex % palette.length]);
            colorIndex++;
        }
        carColors.put('P', Color.RED);
        carColors.put('K', Color.GREEN); // Exit square
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board == null) return;

        setSize(board[0].length * cellSize, board.length * cellSize);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                char cell = board[row][col];
                int x = col * cellSize;
                int y = row * cellSize;

                if (cell == ' ') {
                    g2.setColor(new Color(0, 0, 0, 0));
                    g2.fillRect(x, y, cellSize, cellSize);
                    continue;
                }

                if (cell == '.') {
                    g2.setColor(Color.WHITE);
                    g2.fillRect(x, y, cellSize, cellSize);
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.drawRect(x, y, cellSize, cellSize);
                } else {
                    g2.setColor(carColors.getOrDefault(cell, Color.GRAY));
                    g2.fillRect(x, y, cellSize, cellSize);
                    g2.setColor(Color.BLACK);
                    g2.setFont(new Font("Arial", Font.BOLD, 24));
                    g2.drawString(String.valueOf(cell), x + cellSize / 3, y + 2 * cellSize / 3);
                    g2.setColor(Color.DARK_GRAY);
                    g2.drawRect(x, y, cellSize, cellSize);
                }
            }
        }
    }
}
