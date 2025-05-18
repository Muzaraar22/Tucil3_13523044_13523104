package src.algorithm;

import java.util.PriorityQueue;
import src.Util.Move;
import src.heuristic.IHeuristic;
import src.object.BoardState;
import src.object.RushHourBoard;


public class AStarSearch extends Solver {
    private IHeuristic heuristic;
    public AStarSearch(RushHourBoard initialBoard, IHeuristic heuristic) {
        super(initialBoard);
        this.heuristic = heuristic;
    }
    
    @Override
    public Solution solve() {
        PriorityQueue<BoardState> openSet = new PriorityQueue<>();
        
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
            
            // lanjutin berdasar cost/heuristic terenda
            for (Move move : current.getBoard().getAllPossibleMoves()) {
                RushHourBoard newBoard = current.getBoard().clone();
                newBoard.applyMove(move);
                
                String boardStr = boardToString(newBoard);

                //TODO : walau udah pernah, tapi cost rendah, harusnya push aja
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