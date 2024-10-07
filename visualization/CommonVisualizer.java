package visualization;

import algorithms.SchedulingAlgorithm;
import models.ProcessImp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class CommonVisualizer extends JFrame implements SimulationListener {

    private JPanel processPanel;
    private JLabel clockLabel;
    private JLabel resultLabel;
    private JLabel readyQueueLabel;
    private SchedulingAlgorithm algorithm;
    private Map<Integer, ProcessVisualizerPanel> processPanels = new HashMap<>();
    private JTable processTable;
    private DefaultTableModel tableModel;

    public CommonVisualizer(String algorithmName, List<ProcessImp> processList, int quantum) {
        setTitle(algorithmName + " Scheduling Visualization");
        setLayout(new BorderLayout());

        clockLabel = new JLabel("Clock: 0", SwingConstants.CENTER);
        clockLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(clockLabel, BorderLayout.NORTH);

        processPanel = new JPanel();
        processPanel.setLayout(new GridLayout(processList.size(), 1));
        add(processPanel, BorderLayout.CENTER);

        // Determine if the selected algorithm is a priority scheduling algorithm
        boolean isPriorityScheduling = algorithmName.toLowerCase().contains("priority");

        // Define table columns conditionally
        String[] columnNames;
        if (isPriorityScheduling) {
            columnNames = new String[]{"Process ID", "Arrival Time", "Burst Time", "Priority"};
        } else {
            columnNames = new String[]{"Process ID", "Arrival Time", "Burst Time"};
        }

        // Create table model with conditional columns
        tableModel = new DefaultTableModel(columnNames, 0);
        processTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(processTable);
        add(scrollPane, BorderLayout.WEST);

        // Add process data to the table and panel
        for (ProcessImp process : processList) {
            Object[] rowData;
            if (isPriorityScheduling) {
                rowData = new Object[]{
                    process.getProcessID(),
                    process.getArrivalTime(),
                    process.getBurstTime(),
                    process.getPriority()  // Only include priority if it is a priority algorithm
                };
            } else {
                rowData = new Object[]{
                    process.getProcessID(),
                    process.getArrivalTime(),
                    process.getBurstTime()
                };
            }
            tableModel.addRow(rowData);

            ProcessVisualizerPanel processPanel = new ProcessVisualizerPanel(process);
            this.processPanel.add(processPanel);
            processPanels.put(process.getProcessID(), processPanel);
        }

        // Bottom panel for results and ready queue
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        resultLabel = new JLabel("Results will be displayed here.");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(resultLabel);

        readyQueueLabel = new JLabel("Ready Queue: ");
        readyQueueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(Box.createHorizontalStrut(20)); // Add gap between result and ready queue
        bottomPanel.add(readyQueueLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Instantiate the algorithm based on user selection
        algorithm = AlgorithmFactory.createAlgorithm(algorithmName, processList, quantum);

        if (algorithm == null) {
            JOptionPane.showMessageDialog(this, "Unknown algorithm: " + algorithmName, "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Start executing the algorithm in a new thread
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
            panel.markAsCompleted(process.getCompletionTime());
        }
    }

    @Override
    public void onClockUpdate(int currentTime) {
        clockLabel.setText("Clock: " + currentTime);
    }

    @Override
    public void onSimulationCompleted(List<ProcessImp> processes) {
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int numProcesses = processes.size();

        for (ProcessImp process : processes) {
            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
        }

        double avgWaitingTime = (double) totalWaitingTime / numProcesses;
        double avgTurnaroundTime = (double) totalTurnaroundTime / numProcesses;

        resultLabel.setText(
            String.format("Avg Waiting Time: %.2f | Avg Turnaround Time: %.2f", avgWaitingTime, avgTurnaroundTime));
    }

    @Override
    public void onReadyQueueUpdated(Object readyQueue) {
        StringBuilder queueState = new StringBuilder();

        if (readyQueue instanceof TreeSet) {
            @SuppressWarnings("unchecked")
            TreeSet<ProcessImp> treeSet = (TreeSet<ProcessImp>) readyQueue;
            for (ProcessImp process : treeSet) {
                queueState.append("P").append(process.getProcessID()).append(" ");
            }
        } else if (readyQueue instanceof List) {
            @SuppressWarnings("unchecked")
            List<ProcessImp> list = (List<ProcessImp>) readyQueue;
            for (ProcessImp process : list) {
                queueState.append("P").append(process.getProcessID()).append(" ");
            }
        }

        readyQueueLabel.setText("Ready Queue: " + queueState.toString().trim());
    }
}
