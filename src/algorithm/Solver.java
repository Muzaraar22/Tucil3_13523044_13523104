package src.algorithm;

import java.util.HashSet;
import java.util.Set;
import src.object.*;

public abstract class Solver {
    protected RushHourBoard initialBoard;
    protected int nodesVisited;
    protected Set<String> visitedStates;
    
    public  Solver(RushHourBoard initialBoard) {
        this.initialBoard = initialBoard;
        this.nodesVisited = 0;
        this.visitedStates = new HashSet<>();
    }
    
    public abstract Solution solve();
    
    public int getNodesVisited() {
        return nodesVisited;
    }
    
    //ofc buat cek apakah sama
    protected String boardToString(RushHourBoard board) {
        StringBuilder sb = new StringBuilder();
        char[][] boardArray = board.getBoard();
        
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                sb.append(boardArray[i][j]);
            }
        }
        return sb.toString();
    }
}