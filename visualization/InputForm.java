package visualization;

import models.ProcessImp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InputForm extends JFrame {

    private List<ProcessImp> processList = new ArrayList<>();
    private JTextField arrivalField, burstField, priorityField;
    private int processCount = 0;

    public InputForm(String selectedAlgorithm, int numberOfProcesses, int timeQuantum) {
        setTitle("CPU Scheduling Input Form");
        setLayout(new GridLayout(5, 2));  
    

        JLabel arrivalLabel = new JLabel("Arrival Time: ");
        arrivalField = new JTextField();
        JLabel burstLabel = new JLabel("Burst Time: ");
        burstField = new JTextField();
        JLabel priorityLabel = new JLabel("Priority: ");
        priorityField = new JTextField();

        add(arrivalLabel);
        add(arrivalField);
        add(burstLabel);
        add(burstField);

        if (selectedAlgorithm.equals("Priority Scheduling")) {
            add(priorityLabel);
            add(priorityField);
        }

        
        JButton addButton = new JButton("Add Process");
        JButton visualizeButton = new JButton("Start Visualization");

        add(addButton);
        add(visualizeButton);

        visualizeButton.setEnabled(false); 
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int arrivalTime = Integer.parseInt(arrivalField.getText());
                    int burstTime = Integer.parseInt(burstField.getText());
                    int priority = selectedAlgorithm.equals("Priority Scheduling")
                            ? Integer.parseInt(priorityField.getText())
                            : 0;

                    processCount++;
                    processList.add(new ProcessImp(processCount, arrivalTime, burstTime, priority));
                
                    arrivalField.setText("");
                    burstField.setText("");
                    if (selectedAlgorithm.equals("Priority Scheduling")) {
                        priorityField.setText("");
                    }

                    if (processCount == numberOfProcesses) {
                        addButton.setEnabled(false);
                        visualizeButton.setEnabled(true);
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter numeric values.");
                }
            }
        });

     
        visualizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!processList.isEmpty()) {
                    // Launch the common visualizer with the selected algorithm and processes
                    new CommonVisualizer(selectedAlgorithm, processList, timeQuantum);
                    dispose(); // Close the input form
                } else {
                    JOptionPane.showMessageDialog(null, "No processes added. Please add processes first.");
                }
            }
        });

        // JFrame settings
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
