package visualization;

import models.ProcessImp;

import javax.swing.*;
import java.awt.*;

public class ProcessVisualizerPanel extends JPanel {
    private JLabel processLabel;
    private JProgressBar progressBar;
    private JLabel remainingTimeLabel;

    public ProcessVisualizerPanel(ProcessImp process) {
        setLayout(new GridLayout(1, 3));

        // Initialize components
        processLabel = new JLabel("Process " + process.getProcessID());
        progressBar = new JProgressBar(0, process.getBurstTime());
        remainingTimeLabel = new JLabel("Remaining: " + process.getRemainingTime());

        // Configure progress bar
        progressBar.setValue(process.getBurstTime() - process.getRemainingTime());

        // Add components to panel
        add(processLabel);
        add(progressBar);
        add(remainingTimeLabel);
    }

    // Method to update the remaining time and progress bar value
    public void updateProcess(int remainingTime, int burstTime) {
        progressBar.setValue(burstTime - remainingTime);
        remainingTimeLabel.setText("Remaining: " + remainingTime);
    }

    // Method to mark the process as completed
    public void markAsCompleted() {
        progressBar.setValue(progressBar.getMaximum());
        remainingTimeLabel.setText("Completed");
    }
}
