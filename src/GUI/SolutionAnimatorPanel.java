package src.GUI;

import javax.swing.*;
import java.awt.*;

public class SolutionAnimatorPanel extends JPanel {

    private char[][][] solutionStates;
    private int currentIndex = 0;
    private final int cellSize = 80;

    private BoardPanel boardPanel;
    private Timer timer;
    private JSlider slider;
    private JButton startButton, stopButton, resetButton;
    private JPanel controlsPanel, bottomPanel;

    public SolutionAnimatorPanel() {
        this(null, 1000);
    }

    public SolutionAnimatorPanel(char[][][] solutionStates, int delayMillis) {
        this.setLayout(new BorderLayout());
        initializeComponents(delayMillis);
        setSolutionStates(solutionStates);
    }

    private void initializeComponents(int delayMillis) {
        boardPanel = new BoardPanel(null, cellSize);
        this.add(boardPanel, BorderLayout.CENTER);

        timer = new Timer(delayMillis, e -> {
            if (solutionStates != null && currentIndex < solutionStates.length - 1) {
                currentIndex++;
                updateState();
            } else {
                currentIndex = 0;
                updateState();
            }
        });

        slider = new JSlider();
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting() && solutionStates != null) {
                currentIndex = slider.getValue();
                updateState();
            }
        });

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        resetButton = new JButton("Reset");

        startButton.addActionListener(e -> {
            if (solutionStates != null && !timer.isRunning()) timer.start();
        });

        stopButton.addActionListener(e -> timer.stop());

        resetButton.addActionListener(e -> {
            timer.stop();
            currentIndex = 0;
            updateState();
        });

        controlsPanel = new JPanel();
        controlsPanel.add(startButton);
        controlsPanel.add(stopButton);
        controlsPanel.add(resetButton);

        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(slider, BorderLayout.CENTER);
        bottomPanel.add(controlsPanel, BorderLayout.SOUTH);

        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setSolutionStates(char[][][] states) {
        this.solutionStates = states;

        if (states == null || states.length == 0) {
            this.setVisible(false);
            boardPanel.setBoard(null);
            return;
        }

        this.setVisible(true);
        currentIndex = 0;
        boardPanel.setBoard(states[0]);

        slider.setMaximum(states.length - 1);
        slider.setMinimum(0);
        slider.setValue(0);
        slider.setMajorTickSpacing(Math.max(1, states.length / 10));
        slider.setLabelTable(slider.createStandardLabels(Math.max(1, states.length / 10)));
        slider.repaint();
    }

    private void updateState() {
        if (solutionStates != null && solutionStates.length > 0) {
            boardPanel.setBoard(solutionStates[currentIndex]);
            slider.setValue(currentIndex);
        }
    }
}
