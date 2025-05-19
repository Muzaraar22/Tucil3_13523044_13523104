package src.algorithm;

import java.util.PriorityQueue;

import src.data.*;
import src.data.Move;

public class UniformCostSearch extends Solver{
    public UniformCostSearch(RushHourBoard initialBoard) {
        super(initialBoard);
    }
    
    @Override
    public Solution solve() {
        PriorityQueue<BoardState> openSet = new PriorityQueue<>((s1, s2) -> Integer.compare(s1.getCost(), s2.getCost()));
        
        BoardState initialState = new BoardState(initialBoard, null, null, 0, 0);
        openSet.add(initialState);
        visitedStates.add(boardToString(initialBoard));
        
        while (!openSet.isEmpty()) {
            BoardState current = openSet.poll();
            nodesVisited++;
            
            // Check if goal state
            if (current.getBoard().isGoalState()) {
                return new Solution(initialBoard, current.getPathFromRoot());
            }
            
            // Generate successor states
            for (Move move : current.getBoard().getAllPossibleMoves()) {
                RushHourBoard newBoard = current.getBoard().clone();
                newBoard.applyMove(move);
                
                String boardStr = boardToString(newBoard);

                //TODO : sama kayak di AStar
                if (!visitedStates.contains(boardStr)) {
                    BoardState newState = new BoardState(
                        newBoard,
                        current,
                        move,
                        current.getCost() + 1,
                        0
                    );
                    
                    openSet.add(newState);
                    visitedStates.add(boardStr);
                }
            }
        }
        
        // No solution found
        return null;
    }
}
