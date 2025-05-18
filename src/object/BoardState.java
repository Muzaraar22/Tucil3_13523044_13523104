package src.object;

import java.util.ArrayList;
import java.util.List;
import src.Util.Move;

public class BoardState implements Comparable<BoardState> {
    private RushHourBoard board;
    private BoardState parent;
    private Move lastMove;
    private int cost;
    private int heuristic;
    
    public BoardState(RushHourBoard board, BoardState parent, Move lastMove, int cost, int heuristic) {
        this.board = board;
        this.parent = parent;
        this.lastMove = lastMove;
        this.cost = cost;
        this.heuristic = heuristic;
    }
    
    public RushHourBoard getBoard() {return board;}
    public BoardState getParent() {return parent;}
    public Move getLastMove() {return lastMove;}
    public int getCost() {return cost;}
    public int getHeuristic() {return heuristic;}
    public int getTotalCost() {return cost + heuristic;}
    
    @Override
    public int compareTo(BoardState other) {
        return Integer.compare(this.getTotalCost(), other.getTotalCost());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        BoardState other = (BoardState) obj;
        return board.equals(other.board);
    }
    
    @Override
    public int hashCode() {
        return board.hashCode();
    }
    
    public List<Move> getPathFromRoot() {
        List<Move> path = new ArrayList<>();
        BoardState current = this;
        
        while (current.parent != null) {
            path.add(0, current.lastMove);
            current = current.parent;
        }
        return path;
    }
}