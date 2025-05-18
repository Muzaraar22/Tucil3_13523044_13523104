package src.algorithm;

import java.util.List;
import src.Util.Move;
import src.object.*;

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
}