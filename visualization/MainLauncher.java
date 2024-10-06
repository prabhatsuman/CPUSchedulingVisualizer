package visualization;

public class MainLauncher {
    public static void main(String[] args) {
        // Show the algorithm selection dialog
        AlgorithmSelectionPanel selectionPanel = new AlgorithmSelectionPanel();
        selectionPanel.showSelectionDialog();

        // Retrieve the selected algorithm and the number of processes
        String selectedAlgorithm = selectionPanel.getSelectedAlgorithm();
        int numberOfProcesses = selectionPanel.getNumberOfProcesses();

        // If valid selection and number of processes were entered
        if (selectedAlgorithm != null && numberOfProcesses > 0) {
            // Show the input form for the processes
            new InputForm(selectedAlgorithm, numberOfProcesses);
        } else {
            System.out.println("Invalid selection or number of processes. Exiting...");
        }
    }
}
