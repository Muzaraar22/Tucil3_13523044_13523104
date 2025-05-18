package src.heuristic;

import java.util.HashSet;
import java.util.Set;
import src.Util.Position;
import src.object.*;

public class BlockingCarHeuristic implements IHeuristic {
    @Override
    public int calculate(RushHourBoard board) {
        Car primaryCar = board.getPrimaryCar();
        Position exitPosition = board.getExitPosition();
        
        int blockingCount ;
        Set<Character> blockingCars = new HashSet<>();
        
        if (primaryCar.isHorizontal) {
            Position rightmostPos = primaryCar.positions.get(primaryCar.positions.size() - 1);
            int row = rightmostPos.row;
            
            if (exitPosition.col > rightmostPos.col) {
                for (int col = rightmostPos.col + 1; col <= exitPosition.col; col++) {
                    char cell = board.getBoard()[row][col];
                    if (cell != '.' && cell != 'K' && cell != 'P') {
                        blockingCars.add(cell);
                    }
                }
            }

            else if (exitPosition.col < primaryCar.positions.get(0).col) {
                for (int col = primaryCar.positions.get(0).col - 1; col >= exitPosition.col; col--) {
                    char cell = board.getBoard()[row][col];
                    if (cell != '.' && cell != 'K' && cell != 'P') {
                        blockingCars.add(cell);
                    }
                }
            }
        } 
        // For vertical primary car
        else {
            Position bottommostPos = primaryCar.positions.get(primaryCar.positions.size() - 1);
            int col = bottommostPos.col;
            
            if (exitPosition.row > bottommostPos.row) {
                for (int row = bottommostPos.row + 1; row <= exitPosition.row; row++) {
                    char cell = board.getBoard()[row][col];
                    if (cell != '.' && cell != 'K' && cell != 'P') {
                        blockingCars.add(cell);
                    }
                }
            }
            else if (exitPosition.row < primaryCar.positions.get(0).row) {
                for (int row = primaryCar.positions.get(0).row - 1; row >= exitPosition.row; row--) {
                    char cell = board.getBoard()[row][col];
                    if (cell != '.' && cell != 'K' && cell != 'P') {
                        blockingCars.add(cell);
                    }
                }
            }
        }
        
        blockingCount = blockingCars.size();
        return blockingCount * 2;
    }
}
