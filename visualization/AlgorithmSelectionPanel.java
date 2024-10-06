package visualization;

import javax.swing.JOptionPane;

public class AlgorithmSelectionPanel {
    private String selectedAlgorithm;
    private int numberOfProcesses;

    public void showSelectionDialog() {
        // Display a dialog box to select the scheduling algorithm
        String[] algorithms = {
            "First Come First Serve (FCFS)", 
            "Shortest Job First (SJF)", 
            "Shortest Job First Preemptive (SJF Preemptive)", 
            "Priority Scheduling", 
            "Round Robin"
        };

        // Show the algorithm selection dialog
        selectedAlgorithm = (String) JOptionPane.showInputDialog(null, "Select a scheduling algorithm", "Algorithm Selection", JOptionPane.QUESTION_MESSAGE, null, algorithms, algorithms[0]);

        // If the user presses cancel or closes the dialog, stop execution
        if (selectedAlgorithm == null) {
            JOptionPane.showMessageDialog(null, "No algorithm selected. Exiting.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Continuously prompt for number of processes until a valid number is entered
        while (true) {
            String numProcessInput = JOptionPane.showInputDialog("Enter the number of processes");

            // Handle cancel or close action
            if (numProcessInput == null) {
                JOptionPane.showMessageDialog(null, "No number of processes entered. Exiting.", "Warning", JOptionPane.WARNING_MESSAGE);
                numberOfProcesses = 0; // Indicate invalid or canceled input
                break;
            }

            try {
                numberOfProcesses = Integer.parseInt(numProcessInput);

                // Validate that number of processes is positive
                if (numberOfProcesses > 0) {
                    break; // Exit loop if input is valid
                } else {
                    JOptionPane.showMessageDialog(null, "Number of processes must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public String getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public int getNumberOfProcesses() {
        return numberOfProcesses;
    }
}
