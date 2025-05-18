package src;

public class Util {
    public static class Position {
        public int row;
        public int col;
        
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

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    
    public static class Move {
        public char carId;
        public int distance;
        public Direction direction;
        
        public Move(char carId, Direction direction, int distance) {
            this.carId = carId;
            this.direction = direction;
            this.distance = distance;
        }
        
        @Override
        public String toString() {
            String dirStr = "";
            switch (direction) {
                case UP: dirStr = "atas"; break;
                case DOWN: dirStr = "bawah"; break;
                case LEFT: dirStr = "kiri"; break;
                case RIGHT: dirStr = "kanan"; break;
            }
            return carId + "-" + dirStr + " : " + distance;
        }
    }
}


