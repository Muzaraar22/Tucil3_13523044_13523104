package src.IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import src.data.Position;
import src.data.RushHourBoard;

public class BoardReader {
    public BufferedReader reader;
    
    public BoardReader(BufferedReader br){
        reader = br;
    }

    public RushHourBoard loadRushHourBoard() throws IOException {
        String[] row_col = reader.readLine().split(" ");
        int rows = Integer.parseInt(row_col[0]);
        int cols = Integer.parseInt(row_col[1]);
        int numPieces = Integer.parseInt(reader.readLine());
        
        List<String> tempBoard = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
            tempBoard.add(line);
        }
        
        //buat cek letak K ada lebihan
        int actualRows = Math.max(rows, tempBoard.size());
        int actualCols = cols;
        for (String boardLine : tempBoard) {
            actualCols = Math.max(actualCols, boardLine.length());
        }
        
        char[][] board = new char[actualRows][actualCols];
        for (int i = 0; i < actualRows; i++) {
            for (int j = 0; j < actualCols; j++) {
                board[i][j] = ' ';
            }
        }
        
        for (int i = 0; i < tempBoard.size(); i++) {
            String boardLine = tempBoard.get(i);
            for (int j = 0; j < boardLine.length(); j++) {
                if (j < actualCols) {
                    board[i][j] = boardLine.charAt(j);
                }
            }
        }
    
        Position exitPosition = BoardReader.findExitPosition(board, actualRows, actualCols);
        reader.close();
        return new RushHourBoard(actualRows, actualCols, numPieces, board, exitPosition);
    }

    public static src.data.Position findExitPosition(char[][] board, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'K') {
                    return new src.data.Position(i, j);
                }
            }
        }
        //default ngadi ngadi
        return new src.data.Position(rows/2, cols);
    }
    
}
