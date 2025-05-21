package src.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

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
    private JLabel solutionMetaData;
    private long startTime;
    private long endTime;
    private JButton saveButton;

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

        // 5. Save solution button
        saveButton = new JButton("Save Solution");
        saveButton.setEnabled(false); // Disabled until a solution is available
        saveButton.addActionListener(_ -> saveSolutionToFile());
        controlPanel.add(saveButton);


        JPanel boardContainer = new JPanel();
        boardContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20)); // horizontal gap = 40, vertical = 20

        initialBoardPanel = new BoardPanel();
        boardContainer.add(initialBoardPanel);
        solutionAnimatorPanel = new SolutionAnimatorPanel();
        solutionAnimatorPanel.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));
        boardContainer.add(solutionAnimatorPanel);
        // Error label setup

        JPanel messageContainer = new JPanel();
        messageContainer.setLayout(new BoxLayout(messageContainer, BoxLayout.Y_AXIS));
        messageContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(UIManager.getFont("Label.font").deriveFont(Font.PLAIN, 30));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setVisible(false);

        solutionMetaData = new JLabel();
        solutionMetaData.setText("Hello");
        messageContainer.add(errorLabel);
        messageContainer.add(solutionMetaData);

        // Add panel to frame
        this.add(controlPanel, BorderLayout.NORTH);
        this.add(boardContainer, BorderLayout.CENTER);
        this.add(messageContainer, BorderLayout.SOUTH);
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
                saveButton.setEnabled(false);
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

    private void saveSolutionToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Solution");

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (PrintStream out = new PrintStream(fileToSave)) {
                // Replace this with your actual solution data
                String metadata = "";
                if (solution != null){
                    metadata += "Solution found! \n";
                    metadata += "Number of moves examined: " + solver.getNodesVisited() + "\n";
                    metadata += "Execution time: " + (endTime - startTime) + " ms";
                } else {
                    metadata += "No solution found." + "\n";
                    metadata += "Number of moves examined: " + solver.getNodesVisited() + "\n";
                    metadata += "Execution time: " + (endTime - startTime) + " ms" + "\n";
                }
                out.println(metadata);
                solution.printStepsNoColor(out);
                JOptionPane.showMessageDialog(this, "Solution saved successfully.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to save solution: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        startTime = System.currentTimeMillis();
        solution = solver.solve();
        endTime = System.currentTimeMillis();
        solution.printSteps();
        solutionAnimatorPanel.setSolutionStates(solution.generateStates());
        String metadata = "<html> <div style='font-size:20pt;'> ";
        if (solution != null){
            metadata += "Solution found! <br>";
            metadata += "Number of moves examined: " + solver.getNodesVisited() + "<br>";
            metadata += "Execution time: " + (endTime - startTime) + " ms";
        } else {
            metadata += "No solution found." + "<br>";
            metadata += "Number of moves examined: " + solver.getNodesVisited() + "<br>";
            metadata += "Execution time: " + (endTime - startTime) + " ms" + "<br>";
        }
        metadata += "</div></html>";
        System.out.println(metadata);
        solutionMetaData.setText(metadata);

        saveButton.setEnabled(true);
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
