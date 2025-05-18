package sampah;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the path to the input file: ");
        String filePath = scanner.nextLine();
        
        try {
            RushHour rushHour = loadRushHourFromFile(filePath);
            
            System.out.println("Choose the pathfinding algorithm:");
            System.out.println("1. Greedy Best First Search");
            System.out.println("2. Uniform Cost Search (UCS)");
            System.out.println("3. A* Search");
            System.out.print("Enter your choice (1-3): ");
            int algorithmChoice = scanner.nextInt();
            
            // Heuristic heuristic = null;
            // if (algorithmChoice == 1 || algorithmChoice == 3) {
            //     System.out.println("Choose the heuristic:");
            //     System.out.println("1. Distance to Exit");
            //     System.out.println("2. Blocking Vehicles");
            //     System.out.print("Enter your choice (1-2): ");
            //     int heuristicChoice = scanner.nextInt();
                
            //     if (heuristicChoice == 1) {
            //         heuristic = new DistanceToExitHeuristic();
            //     } else {
            //         heuristic = new BlockingVehiclesHeuristic();
            //     }
            // }
            
            // Solver solver = null;
            // switch (algorithmChoice) {
            //     case 1:
            //         solver = new GreedyBestFirstSearch(rushHour, heuristic);
            //         break;
            //     case 2:
            //         solver = new UniformCostSearch(rushHour);
            //         break;
            //     case 3:
            //         solver = new AStarSearch(rushHour, heuristic);
            //         break;
            //     default:
            //         System.out.println("Invalid choice. Exiting...");
            //         return;
            // }
            
            // long startTime = System.currentTimeMillis();
            // Solution solution = solver.solve();
            // long endTime = System.currentTimeMillis();
            
            // if (solution != null) {
            //     System.out.println("Solution found!");
            //     System.out.println("Number of moves examined: " + solver.getNodesVisited());
            //     System.out.println("Execution time: " + (endTime - startTime) + " ms");
            //     System.out.println("\nSolution steps:");
            //     solution.printSteps();
            // } else {
            //     System.out.println("No solution found.");
            //     System.out.println("Number of moves examined: " + solver.getNodesVisited());
            //     System.out.println("Execution time: " + (endTime - startTime) + " ms");
            // }
            
        } catch (IOException e) {
            System.out.println("Error reading the input file: " + e.getMessage());
        }
        
        scanner.close();
    }
    
    private static RushHour loadRushHourFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        
        // Read board dimensions
        String[] dimensions = reader.readLine().split(" ");
        int rows = Integer.parseInt(dimensions[0]);
        int cols = Integer.parseInt(dimensions[1]);
        
        // Read number of pieces
        int numPieces = Integer.parseInt(reader.readLine());
        
        // Read board configuration
        char[][] board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            String line = reader.readLine();
            for (int j = 0; j < cols; j++) {
                board[i][j] = line.charAt(j);
            }
        }
        
        reader.close();
        
        return new RushHour(rows, cols, numPieces, board);
    }
}