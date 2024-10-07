package visualization;

import javax.swing.JOptionPane;

public class AlgorithmSelectionPanel {
    private String selectedAlgorithm;
    private int numberOfProcesses;
    private int timeQuantum = -1; 

    public void showSelectionDialog() {
        String[] algorithms = {
            "First Come First Serve (FCFS)", 
            "Shortest Job First (SJF)", 
            "Shortest Job First Preemptive (SJF Preemptive)", 
            "Priority Scheduling", 
            "Round Robin"
        };

        selectedAlgorithm = (String) JOptionPane.showInputDialog(null, "Select a scheduling algorithm", "Algorithm Selection", JOptionPane.QUESTION_MESSAGE, null, algorithms, algorithms[0]);

        if (selectedAlgorithm == null) {
            JOptionPane.showMessageDialog(null, "No algorithm selected. Exiting.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        while (true) {
            String numProcessInput = JOptionPane.showInputDialog("Enter the number of processes");

            if (numProcessInput == null) {
                JOptionPane.showMessageDialog(null, "No number of processes entered. Exiting.", "Warning", JOptionPane.WARNING_MESSAGE);
                numberOfProcesses = 0; 
                break;
            }

            try {
                numberOfProcesses = Integer.parseInt(numProcessInput);

                if (numberOfProcesses > 0) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Number of processes must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

   
        if ("Round Robin".equals(selectedAlgorithm)) {
            while (true) {
                String timeQuantumInput = JOptionPane.showInputDialog("Enter the time quantum for Round Robin scheduling");

                if (timeQuantumInput == null) {
                    JOptionPane.showMessageDialog(null, "No time quantum entered. Exiting.", "Warning", JOptionPane.WARNING_MESSAGE);
                    timeQuantum = -1; 
                    break;
                }

                try {
                    timeQuantum = Integer.parseInt(timeQuantumInput);

                    if (timeQuantum > 0) {
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Time quantum must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public String getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public int getNumberOfProcesses() {
        return numberOfProcesses;
    }

    public int getTimeQuantum() {
        return timeQuantum; 
    }
}
