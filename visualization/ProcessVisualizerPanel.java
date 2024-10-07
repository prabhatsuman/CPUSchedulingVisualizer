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

        processLabel = new JLabel("Process " + process.getProcessID());
        progressBar = new JProgressBar(0, process.getBurstTime());
        remainingTimeLabel = new JLabel("Remaining: " + process.getRemainingTime());

        progressBar.setValue(process.getBurstTime() - process.getRemainingTime());

        add(processLabel);
        add(progressBar);
        add(remainingTimeLabel);
    }

    public void updateProcess(int remainingTime, int burstTime) {
        progressBar.setValue(burstTime - remainingTime);
        remainingTimeLabel.setText("Remaining: " + remainingTime);
    }

    public void markAsCompleted(int completionTime) {
        progressBar.setValue(progressBar.getMaximum());
        remainingTimeLabel.setText("Completed at: " + completionTime);
    }
}
