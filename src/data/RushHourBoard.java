package src.data;

import java.io.PrintStream;
import java.util.*;

public class RushHourBoard {
    private int length;
    private int width;
    private int numPieces;
    private char[][] board;
    private Position exitPosition;
    private Map<Character, Car> cars;

    public int getRows() {return length;}
    public int getCols() {return width;}
    public char[][] getBoard() {return board;}
    public Car getPrimaryCar() {return cars.get('P');}
    public Position getExitPosition() {return exitPosition;}
    public Map<Character, Car> getVehicles() {return cars;}
    public Car getCar(char id) {
        return cars.get(id);
    }

    public RushHourBoard(int a, int b, int numPieces, char[][] board, Position exitPosition) throws InvalidBoardException{
        this.length = a;
        this.width = b;
        this.numPieces = numPieces;
        this.board = board;
        this.cars = new HashMap<>();
        this.exitPosition = exitPosition;
        //secara tidak langsung clone car
        identifyVehicles();
    }

    public RushHourBoard clone() {
        char[][] newBoard = new char[length][width];
        for (int i = 0; i < length; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, width);
        }
        try {
            return new RushHourBoard(length, width, numPieces, newBoard, exitPosition);
        } catch (InvalidBoardException e){
            return null;
        }
    }

    private void identifyVehicles() throws InvalidBoardException{
        Map<Character, List<Position>> carPositions = new HashMap<>();
        
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                char cell = board[i][j];
                
                if (!(cell == '.' || cell == 'K' || cell == ' ')) {
                    if (!carPositions.containsKey(cell)) {
                        carPositions.put(cell, new ArrayList<>());
                    }
                    carPositions.get(cell).add(new Position(i, j));
                }
            }
        }
        
        for (Map.Entry<Character, List<Position>> entry : carPositions.entrySet()) {
            char id = entry.getKey();
            List<Position> positions = entry.getValue();

            boolean isHorizontal = true;
            boolean isVertical = true;

            int x1 = positions.get(0).row;
            int y1 = positions.get(0).col;

            for (Position p : positions) {
                int x2 = p.row;
                int y2 = p.col;
                if (x2 != x1) {isHorizontal = false;}
                if (y2 != y1) {isVertical = false;}
            }

            if ((!isHorizontal && !isVertical) || positions.size() <= 1) {
                throw new InvalidCarException("Car " + id + " is neither horizontal nor vertical");
            }

            //_TODO : throw error untuk mobil length 1 (positions.length) harusnya udah kehandle atasnya
            if (id == 'P' && isHorizontal && x1 != exitPosition.row){
                throw new PrimaryCannotExitException("Primary Car is oriented horizontally in row " + x1 + " but exit is in row " + exitPosition.row);
            }
            if (id == 'P' && isVertical && y1 != exitPosition.col){
                throw new PrimaryCannotExitException("Primary Car is oriented vertically in column " + y1 + " but exit is in column " + exitPosition.col);
            }

            if (isHorizontal) {positions.sort((p1, p2) -> Integer.compare(p1.col, p2.col));} 
            else {positions.sort((p1, p2) -> Integer.compare(p1.row, p2.row));}
            
            cars.put(id, new Car(id, positions, isHorizontal, id == 'P'));
        }
    }

    public void printBoard(PrintStream out) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                out.print(board[i][j] + " ");
            }
            out.println();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        RushHourBoard other = (RushHourBoard) obj;
        if (length != other.length || width != other.width) return false;
        
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if (board[i][j] != other.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override //klo ga ada ini, itu si equals berisik jir butuh hashcode
    public int hashCode() {
        int result = length;
        result = 31 * result + width;
        
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                result = 31 * result + board[i][j];
            }
        }
        
        return result;
    }

    //sebelum di apply pasti pake board yang sudah di clone
    public void applyMove(Move move){
        Car car = cars.get(move.carId);
        List<Position> oldPositions = car.positions;
        List<Position> newPositions = new ArrayList<>();
        
        for (Position p : oldPositions) {board[p.row][p.col] = '.';}
        for (Position p : oldPositions) {
            Position newPos = new Position(p.row, p.col);
            
            switch (move.direction) {
                case UP: newPos.row -= move.distance; break;
                case DOWN: newPos.row += move.distance; break;
                case LEFT: newPos.col -= move.distance; break;
                case RIGHT: newPos.col += move.distance; break;
            }
            newPositions.add(newPos);
        }

        car.positions = newPositions;
        for (Position p : newPositions) {
            board[p.row][p.col] = car.id;
        }

    }

    public List<Move> getAllPossibleMoves(){
        List<Move> moves = new ArrayList<>(); 

        for (Car c : cars.values()){
            // this.printBoard();
            // System.out.println(this.length + "  " + this.width);
            // System.out.println(c);

            Position frontP = c.positions.get(0); // left/Top
            Position backP = c.positions.get(c.positions.size()-1); // Right/Bottom
            if (c.isHorizontal){
                for (int i=0; i < frontP.col; i++){
                    // System.out.println(frontP.col);
                    // System.out.println(frontP.row + "  " + (frontP.col - (1+i)));

                    if (board[frontP.row][frontP.col - (1+i)] == '.') {moves.add(new Move(c.id, Direction.LEFT, i+1));}
                    else {break;}
                }
                for (int i=0; i < width - backP.col - 1; i++){
                    // System.out.println(backP.col);
                    // System.out.println(backP.row + "  " + (backP.col + (1+i)));
                    if (board[backP.row][backP.col + (1+i)] == '.') {moves.add(new Move(c.id, Direction.RIGHT, i+1));}
                    else {break;}
                }
            }

            if (!c.isHorizontal){
                for (int i=0; i < frontP.row; i++){
                    // System.out.println(frontP.row);
                    // System.out.println(frontP.row-(i+1) + "  " + (frontP.col));
                    if (board[frontP.row-(i+1)][frontP.col] == '.') {moves.add(new Move(c.id, Direction.UP, i+1));}
                    else {break;}
                }
                for (int i=0; i < length - backP.row - 1; i++){
                    // System.out.println(backP.row);
                    // System.out.println(backP.row+(i+1) + "  " + (backP.col));
                    if (board[backP.row+(i+1)][backP.col] == '.') {moves.add(new Move(c.id, Direction.DOWN, i+1));}
                    else {break;}
                }
            }
        }
        return moves;
    }

    public boolean isGoalState(){
        Car primaryVehicle = cars.get('P');
        
        for (Position pos : primaryVehicle.positions) {
            if (pos.row == exitPosition.row && pos.col == exitPosition.col) {
                return true;
            }
            
            if (primaryVehicle.isHorizontal) {
                if (exitPosition.col == 0 && pos.col == 1 && pos.row == exitPosition.row) {return true;}
                if (exitPosition.col == width - 1 && pos.col == width - 2 && pos.row == exitPosition.row) {return true;}
            } else {
                if (exitPosition.row == 0 && pos.row == 1 && pos.col == exitPosition.col) {return true;}
                if (exitPosition.row == length - 1 && pos.row == length - 2 && pos.col == exitPosition.col) {return true;}
            }
        }
        return false;
    }

    public void printColoredBoard(Move lastMove) {
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_RED = "\u001B[31m";     
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_YELLOW = "\u001B[33m";
        
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                char cell = board[i][j];
                
                if (cell == 'P') {System.out.print(ANSI_RED + cell + ANSI_RESET);} 
                else if (cell == 'K') {System.out.print(ANSI_GREEN + cell + ANSI_RESET);} 
                else if (lastMove != null && cell == lastMove.carId) {System.out.print(ANSI_YELLOW + cell + ANSI_RESET);} 
                else {System.out.print(cell);}
            }
            System.out.println();
        }
    }
}
