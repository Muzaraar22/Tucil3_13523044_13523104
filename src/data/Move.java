package src.data;

public class Move {
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