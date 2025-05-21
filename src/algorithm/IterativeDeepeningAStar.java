package src.algorithm;

import src.data.Move;
import src.algorithm.heuristic.IHeuristic;
import src.data.BoardState;
import src.data.RushHourBoard;
import src.data.Solution;

public class IterativeDeepeningAStar extends Solver {
    private IHeuristic heuristic;
    
    public IterativeDeepeningAStar(RushHourBoard initialBoard, IHeuristic heuristic) {
        super(initialBoard);
        this.heuristic = heuristic;
    }
    
    @Override
    public Solution solve() {
        int threshold = heuristic.calculate(initialBoard);
        
        while (true) {
            visitedStates.clear(); 
            
            
            Result result = new Result();
            result.found = false;
            result.nextThreshold = Integer.MAX_VALUE;
            
        
            BoardState initialState = new BoardState(initialBoard, null, null, 0, heuristic.calculate(initialBoard));
            boolean success = search(initialState, 0, threshold, result);
            
            
            nodesVisited += result.nodesVisited;
            
            if (result.found) {
                return new Solution(initialBoard, result.solution.getPathFromRoot());
            }
            
            if (result.nextThreshold == Integer.MAX_VALUE) {
                return null;
            }
            threshold = result.nextThreshold;
        }
    }
    
    private boolean search(BoardState current, int g, int threshold, Result result) {
        result.nodesVisited++;
        
        int f = g + current.getHeuristic();
        
        if (f > threshold) {
            result.nextThreshold = Math.min(result.nextThreshold, f);
            return false;
        }
        
        if (current.getBoard().isGoalState()) {
            result.found = true;
            result.solution = current;
            return true;
        }
        
        String boardStr = boardToString(current.getBoard());
        visitedStates.add(boardStr);
        
        for (Move move : current.getBoard().getAllPossibleMoves()) {
            RushHourBoard newBoard = current.getBoard().clone();
            newBoard.applyMove(move);
            
            String newBoardStr = boardToString(newBoard);
            
            if (!visitedStates.contains(newBoardStr)) {
                BoardState newState = new BoardState(
                    newBoard,
                    current,
                    move,
                    g + 1,
                    heuristic.calculate(newBoard)
                );
                
                boolean found = search(newState, g + 1, threshold, result);
                
                if (result.found) {
                    return true;
                }
                
                visitedStates.remove(newBoardStr);
            }
        }
        
        return false;
    }
    
    private static class Result {
        public boolean found;
        public int nextThreshold;
        public BoardState solution;
        public int nodesVisited;
        
        public Result() {
            this.found = false;
            this.nextThreshold = Integer.MAX_VALUE;
            this.solution = null;
            this.nodesVisited = 0;
        }
    }
}