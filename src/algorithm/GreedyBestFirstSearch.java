package src.algorithm;

import java.util.PriorityQueue;
import src.Util.Move;
import src.heuristic.IHeuristic;
import src.object.BoardState;
import src.object.RushHourBoard;


public class GreedyBestFirstSearch extends Solver {
    private IHeuristic heuristic;
    public GreedyBestFirstSearch(RushHourBoard initialBoard, IHeuristic heuristic) {
        super(initialBoard);
        this.heuristic = heuristic;
    }
    
    @Override
    public Solution solve() {
        //beda di sini
        PriorityQueue<BoardState> openSet = new PriorityQueue<>((s1, s2) -> Integer.compare(s1.getHeuristic(), s2.getHeuristic()));
        
        //initial state
        BoardState initialState = new BoardState(initialBoard, null, null, 0, heuristic.calculate(initialBoard));
        openSet.add(initialState);
        visitedStates.add(boardToString(initialBoard));
        
        while (!openSet.isEmpty()) {
            BoardState current = openSet.poll();
            nodesVisited++;
            
            // Check if goal state
            if (current.getBoard().isGoalState()) {
                return new Solution(initialBoard, current.getPathFromRoot());
            }
            
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
                        heuristic.calculate(newBoard)
                    );
                    
                    openSet.add(newState);
                    visitedStates.add(boardStr);
                }
            }
        }
        return null;
    }
}