package sampah;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class Input {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
    
        // Read board dimensions
        String[] dimensions = reader.readLine().split(" ");
        int rows = Integer.parseInt(dimensions[0]);
        int cols = Integer.parseInt(dimensions[1]);
        
        // Read number of pieces
        int numPieces = Integer.parseInt(reader.readLine());
        
        // Read all lines of the board configuration
        List<String> boardLines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
            boardLines.add(line);
        }
        
        // Determine actual board dimensions based on input
        int actualRows = Math.max(rows, boardLines.size());
        int actualCols = cols;
        for (String boardLine : boardLines) {
            actualCols = Math.max(actualCols, boardLine.length());
        }
        
        // Create board with actual dimensions
        char[][] board = new char[actualRows][actualCols];
        
        // Initialize with empty cells
        for (int i = 0; i < actualRows; i++) {
            for (int j = 0; j < actualCols; j++) {
                board[i][j] = '.';
            }
        }
        
        // Fill board with input data
        for (int i = 0; i < boardLines.size(); i++) {
            String boardLine = boardLines.get(i);
            for (int j = 0; j < boardLine.length(); j++) {
                if (j < actualCols) {
                    board[i][j] = boardLine.charAt(j);
                }
            }
        }
        // boolean isExitFound = false;
        // int[] exitPos = new int[2];
        
        // Scanner scan = new Scanner(System.in);
        // int a = scan.nextInt();
        // int b = scan.nextInt();
        // int carsCountInput = scan.nextInt();

        // scan.nextLine(); //buang line setelah input angka

        // char[][] tempBoard = new char[a+1][b+1];
        // HashMap<Character, ArrayList<int[]>> carsMap = new HashMap<>();

        // for (int i = 0; i < a+1; i++) {
        //     for (int j = 0; j < b+1; j++) {
        //         tempBoard[i][j] = ' ';
        //     }
        // }

        // String line = scan.nextLine();
        // while (line != null && !line.trim().isEmpty()){
        //     System.out.println(line);
        //     line = scan.nextLine();
        // }


        // int rowLoop = a;
        // for (int i = 0; i < a; i++) {
        //     String line = scan.nextLine();

        //     for (int j = 0; j < b+1; j++) {
        //         char x;
        //         if (j >= line.length()) { x = ' ';}
        //         else                    { x = line.charAt(j);}

        //         if (x == 'K' && i == 0) {rowLoop++; isExitFound = true;}
        //         else if(x == 'K')       {isExitFound = true;}
                
        //         // initialBoard.board[i][j] = x;


        //         // int[] point = new int[] {i,j};
        //         // // System.out.println(line);
        //         // // System.out.println(i + "   " + j);

        //         // if (x == '.' || x == ' ' || x == 'K') {continue;}

        //         // if (!carsMap.containsKey(line.charAt(j))){
        //         //     ArrayList<int[]> arrPoint = new ArrayList<>();
        //         //     arrPoint.add(point);
        //         //     carsMap.put(line.charAt(j), arrPoint);
        //         // }else{
        //         //     carsMap.get(line.charAt(j)).add(point);
        //         // }

        //         // initialBoard.board[i][j] = line.charAt(j);
        //     }

        //     if (i == a-1 && !isExitFound) {rowLoop++;}
        //     // System.out.println("loop ke: " + i +"   rowLoop: " + rowLoop);
        // }

        // for (Map.Entry<Character, ArrayList<int[]>> entry : carsMap.entrySet()){
        //     if (entry.getKey() == '.' || entry.getKey() == 'K' || entry.getKey() == ' ') {continue;}
        //     ArrayList<int[]> points = entry.getValue();

        //     //throw error
        //     if (points.size() < 2) {System.out.println("Panjang tidak boleh < 2 (main.input 42)"); return;}

        //     boolean isHorizontal = true;
        //     boolean isVertical = true;
        //     int y1 = points.get(0)[0];
        //     int x1 = points.get(0)[1];

        //     for (int[] point : points) {
        //         int y2 = point[0];
        //         int x2 = point[1];
        //         if (y2 != y1) {isHorizontal = false;}
        //         if (x2 != x1) {isVertical = false;}
        //     }

        //     String type = "";
        //     if (isHorizontal) {type = "horizontal";}
        //     if (isVertical) {type = "vertical";}

        //     //throw error
        //     if (!(isHorizontal || isVertical)) {System.out.println("Ada yang ngaco (main.Input 55)"); return;}

        //     Car c = new Car(entry.getKey(), points.size(), points, type);
        //     initialBoard.cars.add(c);
        // }

        // initialBoard.describeBoard();
        // initialBoard.cars.get(1).move(2);
        // initialBoard.cars.get(2).move(-2);
        // System.out.println();
        // initialBoard.describeBoard();
        // System.out.println();
        // initialBoard.updateBoardState();
        // initialBoard.describeBoard();


    }
}
