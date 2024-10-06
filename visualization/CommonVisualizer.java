package visualization;

import algorithms.SchedulingAlgorithm;
import models.ProcessImp;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonVisualizer extends JFrame implements SimulationListener {

    private JPanel processPanel;
    private JLabel clockLabel;
    private SchedulingAlgorithm algorithm;  // General interface for all algorithms
    private Map<Integer, ProcessVisualizerPanel> processPanels = new HashMap<>();

    public CommonVisualizer(String algorithmName, List<ProcessImp> processList) {
        setTitle(algorithmName + " Scheduling Visualization");
        setLayout(new BorderLayout());

        // Clock label at the top
        clockLabel = new JLabel("Clock: 0", SwingConstants.CENTER);
        clockLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(clockLabel, BorderLayout.NORTH);

        // Panel for process progress bars
        processPanel = new JPanel();
        processPanel.setLayout(new GridLayout(processList.size(), 1));
        add(processPanel, BorderLayout.CENTER);

        // Create visualizer panels for each process
        for (ProcessImp process : processList) {
            ProcessVisualizerPanel processPanel = new ProcessVisualizerPanel(process);
            this.processPanel.add(processPanel);
            processPanels.put(process.getProcessID(), processPanel);
        }

        // Configure JFrame
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Initialize the algorithm based on the name passed
        algorithm = AlgorithmFactory.createAlgorithm(algorithmName, processList);

        if (algorithm == null) {
            JOptionPane.showMessageDialog(this, "Unknown algorithm: " + algorithmName, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Start algorithm in a new thread and pass the listener to execute method
        new Thread(() -> algorithm.execute(this)).start();
    }

    @Override
    public void onProcessArrived(ProcessImp process) {
        System.out.println("Process " + process.getProcessID() + " arrived.");
    }

    @Override
    public void onProcessPreempted(ProcessImp process) {
        System.out.println("Process " + process.getProcessID() + " preempted.");
    }

    @Override
    public void onProcessStarted(ProcessImp process) {
        System.out.println("Process " + process.getProcessID() + " started.");
    }

    @Override
    public void onProcessUpdated(ProcessImp process) {
        ProcessVisualizerPanel panel = processPanels.get(process.getProcessID());
        if (panel != null) {
            panel.updateProcess(process.getRemainingTime(), process.getBurstTime());
        }
    }

    @Override
    public void onProcessCompleted(ProcessImp process) {
        ProcessVisualizerPanel panel = processPanels.get(process.getProcessID());
        if (panel != null) {
            panel.markAsCompleted();
        }
    }

    @Override
    public void onClockUpdate(int currentTime) {
        clockLabel.setText("Clock: " + currentTime);
    }
}
