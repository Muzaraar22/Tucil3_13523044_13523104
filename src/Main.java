package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import src.algorithm.AStarSearch;
import src.algorithm.GreedyBestFirstSearch;
import src.algorithm.UniformCostSearch;
import src.algorithm.heuristic.BlockingCarHeuristic;
import src.algorithm.heuristic.GoalHeuristic;
import src.algorithm.heuristic.IHeuristic;
import src.data.*;
import src.algorithm.Solution;
import src.algorithm.Solver;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)){
            System.out.println("Info lokasi file: ");
            String filePath = scanner.nextLine();
            RushHourBoard rushHourBoard = loadRushHourBoard(new BufferedReader(new FileReader(filePath)));

            System.out.println("Choose the pathfinding algorithm:");
            System.out.println("1. Greedy Best First Search");
            System.out.println("2. Uniform Cost Search (UCS)");
            System.out.println("3. A* Search");
            System.out.print("Enter your choice (1-3): ");
            int algorithmChoice = scanner.nextInt();

            IHeuristic heuristic = new GoalHeuristic(); //dummy
            if (algorithmChoice == 1 || algorithmChoice == 3) {
                System.out.println("Choose the heuristic:");
                System.out.println("1. Distance to Exit");
                System.out.println("2. Blocking Vehicles");
                System.out.print("Enter your choice (1-2): ");
                int heuristicChoice = scanner.nextInt();

                if (heuristicChoice == 1) {
                    heuristic = new GoalHeuristic();
                } else {
                    heuristic = new BlockingCarHeuristic();
                }
            }
            
            Solver solver;
            switch (algorithmChoice) {
                case 1:
                    solver = new GreedyBestFirstSearch(rushHourBoard, heuristic);
                    break;
                case 2:
                    solver = new UniformCostSearch(rushHourBoard);
                    break;
                case 3:
                    solver = new AStarSearch(rushHourBoard, heuristic);
                    break;
                default:
                    System.out.println("Invalid choice. Exiting...");
                    return;
            }
            
            long startTime = System.currentTimeMillis();
            Solution solution = solver.solve();
            long endTime = System.currentTimeMillis();
            
            if (solution != null) {
                System.out.println("Solution found!");
                System.out.println("Number of moves examined: " + solver.getNodesVisited());
                System.out.println("Execution time: " + (endTime - startTime) + " ms");
                System.out.println("\nSolution steps:");
                solution.printSteps();
            } else {
                System.out.println("No solution found.");
                System.out.println("Number of moves examined: " + solver.getNodesVisited());
                System.out.println("Execution time: " + (endTime - startTime) + " ms");
            }
        } catch (IOException e) {
            System.out.println("Error reading the input file: " + e.getMessage());
        }
    }


    private static RushHourBoard loadRushHourBoard(BufferedReader reader) throws IOException{
        String[] row_col = reader.readLine().split(" ");
        int rows = Integer.parseInt(row_col[0]);
        int cols = Integer.parseInt(row_col[1]);
        int numPieces = Integer.parseInt(reader.readLine());
        
        List<String> tempBoard = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
            tempBoard.add(line);
        }
        
        //buat cek letak K ada lebihan
        int actualRows = Math.max(rows, tempBoard.size());
        int actualCols = cols;
        for (String boardLine : tempBoard) {
            actualCols = Math.max(actualCols, boardLine.length());
        }
        
        char[][] board = new char[actualRows][actualCols];
        for (int i = 0; i < actualRows; i++) {
            for (int j = 0; j < actualCols; j++) {
                board[i][j] = ' ';
            }
        }
        
        for (int i = 0; i < tempBoard.size(); i++) {
            String boardLine = tempBoard.get(i);
            for (int j = 0; j < boardLine.length(); j++) {
                if (j < actualCols) {
                    board[i][j] = boardLine.charAt(j);
                }
            }
        }

        Position exitPosition = findExitPosition(board, actualRows, actualCols);
        reader.close();
        return new RushHourBoard(actualRows, actualCols, numPieces, board, exitPosition);
    }

    private static Position findExitPosition(char[][] board, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'K') {
                    return new Position(i, j);
                }
            }
        }
        //default ngadi ngadi
        return new Position(rows/2, cols);
    }   
}
