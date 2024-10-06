package visualization;

import algorithms.*;
import models.ProcessImp;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class AlgorithmFactory {

    public static SchedulingAlgorithm createAlgorithm(String algorithmName, List<ProcessImp> processList) {
        // Create the process store for each algorithm
        Map<Integer, List<ProcessImp>> processStore = new HashMap<>();
        for (ProcessImp process : processList) {
            processStore.computeIfAbsent(process.getArrivalTime(), k -> new ArrayList<>()).add(process);
        }

        // Switch statement to create the corresponding algorithm instance
        switch (algorithmName.toLowerCase()) {
            case "first come first serve (fcfs)":
                return new FCFSAlgorithm(processStore, processList.size());
            // case "shortest job first (sjf)":
            //     return new SJFNonPreemptiveAlgorithm(processStore, processList.size());
            case "shortest job first preemptive (sjf preemptive)":
                return new SJFPreemptiveAlgorithm(processStore, processList.size());
            // case "priority scheduling":
            //     return new PrioritySchedulingAlgorithm(processStore, processList.size());
            // case "round robin":
            //     return new RoundRobinAlgorithm(processStore, processList.size());
            default:
                return null;  // Return null if the algorithm is not recognized
        }
    }
}
