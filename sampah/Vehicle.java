package sampah;

import java.util.List;
import java.util.Map;

public class Vehicle {
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
}
