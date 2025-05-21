package src.data;

import java.util.List;

public class Solution {
    private RushHourBoard initialBoard;
    private List<Move> moves;
    
    public Solution(RushHourBoard initialBoard, List<Move> moves) {
        this.initialBoard = initialBoard;
        this.moves = moves;
    }
    
    public void printSteps() {
        System.out.println("Papan Awal");
        initialBoard.printBoard();
        System.out.println();
        
        RushHourBoard currentBoard = initialBoard.clone();
        
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            currentBoard.applyMove(move);
            
            System.out.println("Gerakan " + (i + 1) + ": " + move);
            currentBoard.printColoredBoard(move);
            System.out.println();
        }
    }

    public char[][][] generateStates(){
        char[][][] result = new char[moves.size()+1][initialBoard.getBoard().length][initialBoard.getBoard()[0].length];
        System.out.println("Generate states");
        initialBoard.printBoard();
        RushHourBoard currentBoard = initialBoard.clone();
        char[][] currBoard = currentBoard.getBoard();
        for (int j = 0; j < currBoard.length; j++) {
            char[] row = currBoard[0];
            for (int k = 0; k < row.length; k++) {
                result[0][j][k] = currBoard[j][k];
            }
        }
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            currentBoard.applyMove(move);
            currBoard = currentBoard.getBoard();
            for (int j = 0; j < currBoard.length; j++) {
                char[] row = currBoard[j];
                for (int k = 0; k < row.length; k++) {
                    result[i+1][j][k] = currBoard[j][k];
                }
            }
        }
        return result;
    }
}