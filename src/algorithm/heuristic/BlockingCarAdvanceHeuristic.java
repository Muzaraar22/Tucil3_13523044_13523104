package src.algorithm.heuristic;

import java.util.HashSet;
import java.util.Set;

import src.data.Position;
import src.data.Car;
import src.data.RushHourBoard;

public class BlockingCarAdvanceHeuristic implements IHeuristic {
    private static final int MAX_DEPTH = 8; // Maximum recursion depth to prevent infinite loops
    
    @Override
    public int calculate(RushHourBoard board) {
        Car primaryCar = board.getPrimaryCar();
        Position exitPosition = board.getExitPosition();
        
        Set<Character> visitedCars = new HashSet<>();
        return calculateMovesNeeded(board, primaryCar, exitPosition, visitedCars, 0);
    }
    
    private int calculateMovesNeeded(RushHourBoard board, Car car, Position exitPosition, 
                                    Set<Character> visitedCars, int depth) {
        if (depth >= MAX_DEPTH) {
            return depth; // Return current depth as a penalty
        }
        
        if (car == null) {
            return 0;
        }
        
        visitedCars.add(car.id);
        
        if (car.id == board.getPrimaryCar().id) {
            return calculateBlockingCarsForPrimary(board, car, exitPosition, visitedCars, depth);
        } else {
            return calculateMovesForBlockingCar(board, car, visitedCars, depth);
        }
    }
    
    private int calculateBlockingCarsForPrimary(RushHourBoard board, Car primaryCar, 
                                              Position exitPosition, Set<Character> visitedCars, int depth) {
        Set<Character> blockingCars = new HashSet<>();
        char[][] boardArray = board.getBoard();
        
        if (primaryCar.isHorizontal) {
            Position rightmostPos = primaryCar.positions.get(primaryCar.positions.size() - 1);
            Position leftmostPos = primaryCar.positions.get(0);
            int row = rightmostPos.row;
            
            if (exitPosition.col > rightmostPos.col) {
                for (int col = rightmostPos.col + 1; col <= exitPosition.col; col++) {
                    char cell = boardArray[row][col];
                    if (cell != '.' && cell != primaryCar.id && cell != 'K' && cell != ' ') {
                        blockingCars.add(cell);
                    }
                }
            }
            else if (exitPosition.col < leftmostPos.col) {
                for (int col = leftmostPos.col - 1; col >= exitPosition.col; col--) {
                    char cell = boardArray[row][col];
                    if (cell != '.' && cell != primaryCar.id && cell != 'K' && cell != ' ') {
                        blockingCars.add(cell);
                    }
                }
            }
        } 
        else {
            Position bottommostPos = primaryCar.positions.get(primaryCar.positions.size() - 1);
            Position topmostPos = primaryCar.positions.get(0);
            int col = bottommostPos.col;
            
            if (exitPosition.row > bottommostPos.row) {
                for (int row = bottommostPos.row + 1; row <= exitPosition.row; row++) {
                    char cell = boardArray[row][col];
                    if (cell != '.' && cell != primaryCar.id && cell != 'K' && cell != ' ') {
                        blockingCars.add(cell);
                    }
                }
            }
            else if (exitPosition.row < topmostPos.row) {
                for (int row = topmostPos.row - 1; row >= exitPosition.row; row--) {
                    char cell = boardArray[row][col];
                    if (cell != '.' && cell != primaryCar.id && cell != 'K' && cell != ' ') {
                        blockingCars.add(cell);
                    }
                }
            }
        }
        
        if (blockingCars.isEmpty()) {
            return 0;
        }
        
        int totalMoves = 0;
        for (char blockingCarId : blockingCars) {
            if (!visitedCars.contains(blockingCarId)) {
                Car blockingCar = board.getVehicles().get(blockingCarId);
                if (blockingCar == null) {
                    continue;
                }
                
                totalMoves += 1 + calculateMovesNeeded(board, blockingCar, exitPosition, 
                                                     new HashSet<>(visitedCars), depth + 1);
            }
        }
        
        return totalMoves;
    }
    
    private int calculateMovesForBlockingCar(RushHourBoard board, Car car, 
                                           Set<Character> visitedCars, int depth) {
        char[][] boardArray = board.getBoard();
        int movesNeeded = 0;
        
        if (car.isHorizontal) {
            boolean canMoveLeft = false;
            Position leftmost = car.positions.get(0);
            if (leftmost.col > 0 && boardArray[leftmost.row][leftmost.col - 1] == '.') {
                canMoveLeft = true;
            }
            
            boolean canMoveRight = false;
            Position rightmost = car.positions.get(car.positions.size() - 1);
            if (rightmost.col < boardArray[0].length - 1 && 
                boardArray[rightmost.row][rightmost.col + 1] == '.') {
                canMoveRight = true;
            }
            
            if (canMoveLeft || canMoveRight) {
                return 1; // One move needed
            }
            
            Set<Character> leftBlockers = new HashSet<>();
            if (!canMoveLeft && leftmost.col > 0) {
                char blocker = boardArray[leftmost.row][leftmost.col - 1];
                if (blocker != '.' && blocker != 'K' && blocker != ' ' && !visitedCars.contains(blocker)) {
                    leftBlockers.add(blocker);
                }
            }
            
            Set<Character> rightBlockers = new HashSet<>();
            if (!canMoveRight && rightmost.col < boardArray[0].length - 1) {
                char blocker = boardArray[rightmost.row][rightmost.col + 1];
                if (blocker != '.' && blocker != 'K' && blocker != ' ' && !visitedCars.contains(blocker)) {
                    rightBlockers.add(blocker);
                }
            }
            
            if (!leftBlockers.isEmpty() && (rightBlockers.isEmpty() || leftBlockers.size() <= rightBlockers.size())) {
                for (char blockerId : leftBlockers) {
                    Car blockerCar = board.getVehicles().get(blockerId);
                    
                    if (blockerCar == null) {
                        continue;
                    }
                    
                    movesNeeded += 1 + calculateMovesNeeded(board, blockerCar, null, 
                                                          new HashSet<>(visitedCars), depth + 1);
                }
            } else if (!rightBlockers.isEmpty()) {
                for (char blockerId : rightBlockers) {
                    Car blockerCar = board.getVehicles().get(blockerId);
                    
                    if (blockerCar == null) {
                        continue;
                    }
                    
                    movesNeeded += 1 + calculateMovesNeeded(board, blockerCar, null, 
                                                          new HashSet<>(visitedCars), depth + 1);
                }
            }
        } 
        else {
            boolean canMoveUp = false;
            Position topmost = car.positions.get(0);
            if (topmost.row > 0 && boardArray[topmost.row - 1][topmost.col] == '.') {
                canMoveUp = true;
            }
            
            boolean canMoveDown = false;
            Position bottommost = car.positions.get(car.positions.size() - 1);
            if (bottommost.row < boardArray.length - 1 && 
                boardArray[bottommost.row + 1][bottommost.col] == '.') {
                canMoveDown = true;
            }
            
            if (canMoveUp || canMoveDown) {
                return 1; // One move needed
            }
            
            Set<Character> upBlockers = new HashSet<>();
            if (!canMoveUp && topmost.row > 0) {
                char blocker = boardArray[topmost.row - 1][topmost.col];
                if (blocker != '.' && blocker != 'K' && blocker != ' ' && !visitedCars.contains(blocker)) {
                    upBlockers.add(blocker);
                }
            }
            
            Set<Character> downBlockers = new HashSet<>();
            if (!canMoveDown && bottommost.row < boardArray.length - 1) {
                char blocker = boardArray[bottommost.row + 1][bottommost.col];
                if (blocker != '.' && blocker != 'K' && blocker != ' ' && !visitedCars.contains(blocker)) {
                    downBlockers.add(blocker);
                }
            }
            
            if (!upBlockers.isEmpty() && (downBlockers.isEmpty() || upBlockers.size() <= downBlockers.size())) {
                for (char blockerId : upBlockers) {
                    Car blockerCar = board.getVehicles().get(blockerId);
                    
                    if (blockerCar == null) {
                        continue;
                    }
                    
                    movesNeeded += 1 + calculateMovesNeeded(board, blockerCar, null, 
                                                          new HashSet<>(visitedCars), depth + 1);
                }
            } else if (!downBlockers.isEmpty()) {
                for (char blockerId : downBlockers) {
                    Car blockerCar = board.getVehicles().get(blockerId);
                    
                    if (blockerCar == null) {
                        continue;
                    }
                    
                    movesNeeded += 1 + calculateMovesNeeded(board, blockerCar, null, 
                                                          new HashSet<>(visitedCars), depth + 1);
                }
            }
        }
        
        return movesNeeded;
    }
}