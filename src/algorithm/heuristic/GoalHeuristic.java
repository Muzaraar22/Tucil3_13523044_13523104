package src.algorithm.heuristic;

import src.data.Car;
import src.data.RushHourBoard;
import src.data.Position;

public class GoalHeuristic implements IHeuristic{
    @Override
    public int calculate(RushHourBoard board) {
        Car primaryCar = board.getPrimaryCar();
        Position exitPosition = board.getExitPosition();

        //intinya nyari jarak lurus ke goal
        if (primaryCar.isHorizontal) {
            Position rightmostPos = primaryCar.positions.get(primaryCar.positions.size() - 1);
            if (exitPosition.col > rightmostPos.col) {
                return exitPosition.col - rightmostPos.col;
            }
            if (exitPosition.col < primaryCar.positions.get(0).col) {
                return primaryCar.positions.get(0).col - exitPosition.col;
            }
        } 
        else {
            Position bottommostPos = primaryCar.positions.get(primaryCar.positions.size() - 1);
            if (exitPosition.row > bottommostPos.row) {
                return exitPosition.row - bottommostPos.row;
            }
            if (exitPosition.row < primaryCar.positions.get(0).row) {
                return primaryCar.positions.get(0).row - exitPosition.row;
            }
        }


        //sudah ada di exit
        return 0;
    }
}
