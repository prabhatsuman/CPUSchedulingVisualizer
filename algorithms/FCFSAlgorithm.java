package algorithms;

import models.ProcessImp;
import visualization.SimulationListener;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class FCFSAlgorithm implements SchedulingAlgorithm {

    private TreeSet<ProcessImp> readyQueue; // The ready queue of processes sorted by arrival time
    private ProcessImp currentProcess = null; // The currently running process
    private int currentTime = 0; // System clock
    private int totalProcesses; // Total number of processes
    private int completedProcesses = 0; // Tracks how many processes have been completed
    private Map<Integer, List<ProcessImp>> processStore; // Stores processes by their arrival time

    public FCFSAlgorithm(Map<Integer, List<ProcessImp>> tempProcessStore, int totalProcesses) {
        this.totalProcesses = totalProcesses;
        this.processStore = tempProcessStore;
        this.readyQueue = new TreeSet<>((p1, p2) -> {
            if (p1 == null) return 1;
            if (p2 == null) return -1;
            return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()); // Sort by arrival time
        });
    }

    @Override
    public void execute(SimulationListener listener) {
        // Main scheduling loop
        while (completedProcesses < totalProcesses) {

            // Add processes to the ready queue that have arrived at the current time
            List<ProcessImp> arrivedProcesses = processStore.get(currentTime);
            if (arrivedProcesses != null) {
                for (ProcessImp process : arrivedProcesses) {
                    if (!readyQueue.contains(process)) {
                        readyQueue.add(process); // Add process to the ready queue
                        System.out.println("Process ID: " + process.getProcessID() + " arrived at time: " + currentTime);
                        listener.onProcessArrived(process); // Notify the listener
                    }
                }
            }

            // If no process is running and the ready queue is not empty, start the next process
            if (currentProcess == null && !readyQueue.isEmpty()) {
                currentProcess = readyQueue.pollFirst(); // Get the next process in the queue
                System.out.println("Starting Process ID: " + currentProcess.getProcessID() + " at time: " + currentTime);
                listener.onProcessStarted(currentProcess); // Notify the listener
            }

            // Execute the current process if there is one
            if (currentProcess != null) {
                System.out.println("Executing Process ID: " + currentProcess.getProcessID() + " at time: " + currentTime
                        + " | Remaining Time: " + currentProcess.getRemainingTime());

                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1); // Decrease remaining time by 1 unit
                listener.onProcessUpdated(currentProcess); // Notify listener of remaining time update

                // Check if the current process is finished
                if (currentProcess.getRemainingTime() == 0) {
                    System.out.println("Process ID: " + currentProcess.getProcessID() + " completed at time: " + (currentTime + 1));
                    currentProcess.setCompletionTime(currentTime + 1); // Set completion time
                    currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime()); // Turnaround time
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime()); // Waiting time
                    listener.onProcessCompleted(currentProcess); // Notify listener of process completion
                    currentProcess = null; // Mark no process running
                    completedProcesses++; // Increment completed processes count
                }
            }

            // Increment the system clock
            currentTime++;

            listener.onClockUpdate(currentTime); // Notify listener of clock update

            // Simulate time passage (for real-time execution visualization)
            try {
                TimeUnit.SECONDS.sleep(1); // Simulate 1 second of execution
            } catch (InterruptedException e) {
                System.out.println("Simulation interrupted.");
                break;
            }
        }

        System.out.println("All processes completed at time: " + currentTime);
        display();
    }

    @Override
    public void display() {
        if (completedProcesses == totalProcesses) {
            int totalWaitingTime = 0;
            int totalTurnaroundTime = 0;
            for (List<ProcessImp> processes : processStore.values()) {
                for (ProcessImp process : processes) {
                    totalWaitingTime += process.getWaitingTime();
                    totalTurnaroundTime += process.getTurnaroundTime();
                }
            }
            System.out.println("Average Waiting Time: " + (double) totalWaitingTime / totalProcesses);
            System.out.println("Average Turnaround Time: " + (double) totalTurnaroundTime / totalProcesses);
        } else {
            System.out.println("Current Time: " + currentTime);
            System.out.println("Ready Queue: " + readyQueue);
            if (currentProcess != null) {
                System.out.println("Currently Executing Process: " + currentProcess.getProcessID()
                        + " | Remaining Time: " + currentProcess.getRemainingTime());
            } else {
                System.out.println("No process is currently running.");
            }
        }
    }

    @Override
    public String getAlgorithmName() {
        return "First Come First Serve Scheduling";
    }
}
