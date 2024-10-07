package algorithms;

import visualization.SimulationListener;

public interface SchedulingAlgorithm {
    void execute(SimulationListener listener);  
    void display(); 
    String getAlgorithmName(); 
}
