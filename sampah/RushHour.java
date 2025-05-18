package sampah;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RushHour {
    private int rows;
    private int cols;
    private int numPieces;
    private char[][] board;
    private Map<Character, Vehicle> vehicles;
    private Position exitPosition;
    
    public RushHour(int rows, int cols, int numPieces, char[][] board) {
        this.rows = rows;
        this.cols = cols;
        this.numPieces = numPieces;
        this.board = board;
        this.vehicles = new HashMap<>();
        
        // Identify vehicles and exit position
        identifyVehiclesAndExit();
    }
    
    private void identifyVehiclesAndExit() {
        // Temporary storage for vehicle positions
        Map<Character, List<Position>> vehiclePositions = new HashMap<>();
        
        // Scan the board to find vehicles and exit
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char cell = board[i][j];
                
                if (cell == '.') {
                    // Empty cell
                    continue;
                } else if (cell == 'K') {
                    // Exit position
                    exitPosition = new Position(i, j);
                } else {
                    // Vehicle
                    if (!vehiclePositions.containsKey(cell)) {
                        vehiclePositions.put(cell, new ArrayList<>());
                    }
                    vehiclePositions.get(cell).add(new Position(i, j));
                }
            }
        }
        
        // Create Vehicle objects
        for (Map.Entry<Character, List<Position>> entry : vehiclePositions.entrySet()) {
            char id = entry.getKey();
            List<Position> positions = entry.getValue();
            
            // Determine orientation
            boolean isHorizontal = true;
            if (positions.size() > 1) {
                isHorizontal = positions.get(0).row == positions.get(1).row;
            }
            
            // Sort positions
            if (isHorizontal) {
                positions.sort((p1, p2) -> Integer.compare(p1.col, p2.col));
            } else {
                positions.sort((p1, p2) -> Integer.compare(p1.row, p2.row));
            }
            
            // Create vehicle
            vehicles.put(id, new Vehicle(id, positions, isHorizontal, id == 'P'));
        }
    }
    
    public RushHour clone() {
        char[][] newBoard = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, cols);
        }
        
        return new RushHour(rows, cols, numPieces, newBoard);
    }
    
    public List<Move> getPossibleMoves() {
        List<Move> moves = new ArrayList<>();
        
        for (Vehicle vehicle : vehicles.values()) {
            // Check if vehicle can move up
            if (!vehicle.isHorizontal) {
                Position frontPos = vehicle.positions.get(0);
                if (frontPos.row > 0 && board[frontPos.row - 1][frontPos.col] == '.') {
                    moves.add(new Move(vehicle.id, Direction.UP));
                }
                
                // Check if vehicle can move down
                Position backPos = vehicle.positions.get(vehicle.positions.size() - 1);
                if (backPos.row < rows - 1 && board[backPos.row + 1][backPos.col] == '.') {
                    moves.add(new Move(vehicle.id, Direction.DOWN));
                }
            }
            
            // Check if vehicle can move left
            if (vehicle.isHorizontal) {
                Position leftPos = vehicle.positions.get(0);
                if (leftPos.col > 0 && board[leftPos.row][leftPos.col - 1] == '.') {
                    moves.add(new Move(vehicle.id, Direction.LEFT));
                }
                
                // Check if vehicle can move right
                Position rightPos = vehicle.positions.get(vehicle.positions.size() - 1);
                if (rightPos.col < cols - 1 && board[rightPos.row][rightPos.col + 1] == '.') {
                    moves.add(new Move(vehicle.id, Direction.RIGHT));
                }
            }
        }
        
        return moves;
    }
    
    public void applyMove(Move move) {
        Vehicle vehicle = vehicles.get(move.vehicleId);
        List<Position> oldPositions = vehicle.positions;
        List<Position> newPositions = new ArrayList<>();
        
        // Clear old positions on the board
        for (Position pos : oldPositions) {
            board[pos.row][pos.col] = '.';
        }
        
        // Calculate new positions
        for (Position pos : oldPositions) {
            Position newPos = new Position(pos.row, pos.col);
            
            switch (move.direction) {
                case UP:
                    newPos.row--;
                    break;
                case DOWN:
                    newPos.row++;
                    break;
                case LEFT:
                    newPos.col--;
                    break;
                case RIGHT:
                    newPos.col++;
                    break;
            }
            
            newPositions.add(newPos);
        }
        
        // Update vehicle positions
        vehicle.positions = newPositions;
        
        // Update board with new positions
        for (Position pos : newPositions) {
            board[pos.row][pos.col] = vehicle.id;
        }
    }
    
    public boolean isGoalState() {
        Vehicle primaryVehicle = vehicles.get('P');
        
        // Check if any position of the primary vehicle is at the exit
        for (Position pos : primaryVehicle.positions) {
            if (pos.row == exitPosition.row && pos.col == exitPosition.col) {
                return true;
            }
            
            // If the exit is on the edge and the primary vehicle is adjacent to it
            if (primaryVehicle.isHorizontal) {
                if (exitPosition.col == 0 && pos.col == 1 && pos.row == exitPosition.row) {
                    return true;
                }
                if (exitPosition.col == cols - 1 && pos.col == cols - 2 && pos.row == exitPosition.row) {
                    return true;
                }
            } else {
                if (exitPosition.row == 0 && pos.row == 1 && pos.col == exitPosition.col) {
                    return true;
                }
                if (exitPosition.row == rows - 1 && pos.row == rows - 2 && pos.col == exitPosition.col) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }
    
    public void printColoredBoard(Move lastMove) {
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_RED = "\u001B[31m";     // Primary vehicle
        final String ANSI_GREEN = "\u001B[32m";   // Exit
        final String ANSI_YELLOW = "\u001B[33m";  // Last moved vehicle
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char cell = board[i][j];
                
                if (cell == 'P') {
                    System.out.print(ANSI_RED + cell + ANSI_RESET);
                } else if (cell == 'K') {
                    System.out.print(ANSI_GREEN + cell + ANSI_RESET);
                } else if (lastMove != null && cell == lastMove.vehicleId) {
                    System.out.print(ANSI_YELLOW + cell + ANSI_RESET);
                } else {
                    System.out.print(cell);
                }
            }
            System.out.println();
        }
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    public Vehicle getPrimaryVehicle() {
        return vehicles.get('P');
    }
    
    public Position getExitPosition() {
        return exitPosition;
    }
    
    public Map<Character, Vehicle> getVehicles() {
        return vehicles;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        RushHour other = (RushHour) obj;
        if (rows != other.rows || cols != other.cols) return false;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] != other.board[i][j]) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public int hashCode() {
        int result = rows;
        result = 31 * result + cols;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result = 31 * result + board[i][j];
            }
        }
        
        return result;
    }
    
    // Helper classes
    public static class Position {
        int row;
        int col;
        
        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            
            Position other = (Position) obj;
            return row == other.row && col == other.col;
        }
        
        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }
    
    public static class Vehicle {
        char id;
        List<Position> positions;
        boolean isHorizontal;
        boolean isPrimary;
        
        public Vehicle(char id, List<Position> positions, boolean isHorizontal, boolean isPrimary) {
            this.id = id;
            this.positions = positions;
            this.isHorizontal = isHorizontal;
            this.isPrimary = isPrimary;
        }
    }
    
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    
    public static class Move {
        char vehicleId;
        Direction direction;
        
        public Move(char vehicleId, Direction direction) {
            this.vehicleId = vehicleId;
            this.direction = direction;
        }
        
        @Override
        public String toString() {
            String dirStr = "";
            switch (direction) {
                case UP:
                    dirStr = "atas";
                    break;
                case DOWN:
                    dirStr = "bawah";
                    break;
                case LEFT:
                    dirStr = "kiri";
                    break;
                case RIGHT:
                    dirStr = "kanan";
                    break;
            }
            return vehicleId + "-" + dirStr;
        }
    }
}