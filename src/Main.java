package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import src.GUI.GUI;
import src.IO.BoardReader;
import src.algorithm.AStarSearch;
import src.algorithm.GreedyBestFirstSearch;
import src.algorithm.UniformCostSearch;
import src.algorithm.heuristic.BlockingCarHeuristic;
import src.algorithm.heuristic.GoalHeuristic;
import src.algorithm.heuristic.IHeuristic;
import src.data.*;
import src.algorithm.Solver;

public class Main {
    public static void main(String[] args) {
        if (Arrays.stream(args).anyMatch((s) -> (s.equals("gui")))){
            GUI.start();
            return;
        }
        while (true){
            try (Scanner scanner = new Scanner(System.in)){
                System.out.println("Info lokasi file: ");
                String filePath = scanner.nextLine();
                BoardReader br = new BoardReader(new BufferedReader(new FileReader(filePath)));
                RushHourBoard rushHourBoard = br.loadRushHourBoard();

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
                break;
            } catch (IOException e) {
                System.out.println("Error reading the input file: " + e.getMessage());
            } catch (InvalidBoardException e){
                System.out.println(e.getMessage());
            }
        }
    }   
}
