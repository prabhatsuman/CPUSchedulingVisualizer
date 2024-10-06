package algorithms;

import models.ProcessImp;
import visualization.SimulationListener;

public interface SchedulingAlgorithm {
    void execute(SimulationListener listener);  // Execute the algorithm and pass the listener
    void display();  // Display results or state information
    String getAlgorithmName();  // Return the name of the algorithm
}
