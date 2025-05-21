package src.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

import src.IO.BoardReader;
import src.algorithm.AStarSearch;
import src.algorithm.GreedyBestFirstSearch;
import src.algorithm.Solver;
import src.algorithm.UniformCostSearch;
import src.algorithm.heuristic.BlockingCarHeuristic;
import src.algorithm.heuristic.GoalHeuristic;
import src.algorithm.heuristic.IHeuristic;
import src.data.InvalidBoardException;
import src.data.RushHourBoard;
import src.data.Solution;

public class MainFrame extends JFrame {
    private JButton fileButton;
    private JComboBox<String> algorithmChoice;
    private JComboBox<String> heuristicChoice;
    private JButton solveButton;
    private RushHourBoard board;
    private Solution solution;
    private Solver solver;
    private BoardPanel initialBoardPanel;
    private SolutionAnimatorPanel solutionAnimatorPanel;
    private JLabel errorLabel;

    public MainFrame() {
        this.setTitle("RushHour Solver");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1920, 1080);
        this.setLayout(new BorderLayout());

        // Panel to hold control components
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(4, 1, 10, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. File input button
        fileButton = new JButton("Choose Input File");
        fileButton.addActionListener(_ -> chooseFile());
        controlPanel.add(fileButton);

        // 2. Algorithm choice
        String[] algorithms = {"Uniform Cost Search", "Greedy Best First Search", "A*"};
        algorithmChoice = new JComboBox<>(algorithms);
        algorithmChoice.setSelectedIndex(0);
        controlPanel.add(new LabeledComponent("Solving Algorithm:", algorithmChoice));

        // 3. Heuristic choice
        String[] heuristics = {"Distance to Exit", "Blocking Vehicles"};
        heuristicChoice = new JComboBox<>(heuristics);
        heuristicChoice.setEnabled(false); // initially disabled
        controlPanel.add(new LabeledComponent("Heuristic:", heuristicChoice));

        // Enable heuristics only for Greedy and A*
        algorithmChoice.addActionListener(_ -> {
            String selected = (String) algorithmChoice.getSelectedItem();
            heuristicChoice.setEnabled(selected.equals("Greedy Best First Search") || selected.equals("A*"));
        });

        // 4. Solve button
        solveButton = new JButton("Solve");
        solveButton.addActionListener(_ -> startSolving());
        controlPanel.add(solveButton);

        JPanel boardContainer = new JPanel();
        boardContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20)); // horizontal gap = 40, vertical = 20

        initialBoardPanel = new BoardPanel();
        boardContainer.add(initialBoardPanel);
        solutionAnimatorPanel = new SolutionAnimatorPanel();
        solutionAnimatorPanel.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));
        boardContainer.add(solutionAnimatorPanel);
        // Error label setup
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(UIManager.getFont("Label.font").deriveFont(Font.PLAIN, 30));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setVisible(false);

        this.add(errorLabel, BorderLayout.SOUTH);

        // Add panel to frame
        this.add(controlPanel, BorderLayout.NORTH);
        this.add(boardContainer, BorderLayout.CENTER);
        this.setVisible(true);
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            // Handle file here
            System.out.println("Selected file: " + fileChooser.getSelectedFile().getAbsolutePath());
            try {
                fileButton.setText(fileChooser.getSelectedFile().getPath());
                BoardReader br = new BoardReader(
                    new BufferedReader(
                        new FileReader(
                            fileChooser.getSelectedFile().getAbsoluteFile())));
                board = br.loadRushHourBoard();
                initialBoardPanel.setBoard(board.getBoard());
                solutionAnimatorPanel.setSolutionStates(null);
                errorLabel.setVisible(false);
            } catch (IOException e){
                errorLabel.setText("Error: Unable to read the file.");
                errorLabel.setVisible(true);
                e.printStackTrace();
            } catch (InvalidBoardException e){
                errorLabel.setText("Error:" + e.getMessage());
                errorLabel.setVisible(true);
                e.printStackTrace();
            } catch (Exception e){
                errorLabel.setText("Error: Unable to read the file.");
                errorLabel.setVisible(true);
                e.printStackTrace();
            }
        }
    }

    private IHeuristic makeHeuristic(String heuristic){
        if (heuristic.equals("Blocking Vehicles")) {
            return new BlockingCarHeuristic();
        } else {
            return new GoalHeuristic();
        }
    }

    private Solver makeSolver(String algorithm, String heuristic){
        if (algorithm.equals("Uniform Cost Search")){
            return new UniformCostSearch(board);
        } else if (algorithm.equals("Greedy Best First Search")){
            return new GreedyBestFirstSearch(board, makeHeuristic(heuristic));
        } else {
            return new AStarSearch(board, makeHeuristic(heuristic));
        }
    }

    private void startSolving() {
        if (board == null) {
            return;
        }
        String algorithm = (String) algorithmChoice.getSelectedItem();
        String heuristic = (String) heuristicChoice.getSelectedItem();

        System.out.println("Algorithm: " + algorithm);
        if (heuristicChoice.isEnabled()) {
            System.out.println("Heuristic: " + heuristic);
        }
        if (algorithm.equals("UCS")){
            solver = new UniformCostSearch(board);
        }
        solver = makeSolver(algorithm, heuristic);
        solution = solver.solve();
        solution.printSteps();
        solutionAnimatorPanel.setSolutionStates(solution.generateStates());
        // Call your solver logic here
    }

    // Helper class to label components
    private static class LabeledComponent extends JPanel {
        public LabeledComponent(String label, JComponent comp) {
            this.setLayout(new BorderLayout());
            this.add(new JLabel(label), BorderLayout.WEST);
            this.add(comp, BorderLayout.CENTER);
        }
    }
}
