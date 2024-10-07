package visualization;

public class MainLauncher {
    public static void main(String[] args) {

        AlgorithmSelectionPanel selectionPanel = new AlgorithmSelectionPanel();
        selectionPanel.showSelectionDialog();

        String selectedAlgorithm = selectionPanel.getSelectedAlgorithm();
        int numberOfProcesses = selectionPanel.getNumberOfProcesses();
        int timeQuantum = selectionPanel.getTimeQuantum();

        if (selectedAlgorithm != null && numberOfProcesses > 0) {

            new InputForm(selectedAlgorithm, numberOfProcesses, timeQuantum);
        } else {
            System.out.println("Invalid selection or number of processes. Exiting...");
        }
    }
}
