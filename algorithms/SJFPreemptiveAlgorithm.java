package algorithms;

import models.ProcessImp;
import visualization.SimulationListener;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class SJFPreemptiveAlgorithm implements SchedulingAlgorithm {

    private TreeSet<ProcessImp> readyQueue; // Ready queue of processes sorted by remaining time
    private ProcessImp currentProcess = null; // Currently running process
    private int currentTime = 0; // System clock
    private int totalProcesses; // Total number of processes
    private int completedProcesses = 0; // Tracks how many processes have been completed
    private Map<Integer, List<ProcessImp>> processStore; // Stores processes by arrival time

    // Constructor without listener
    public SJFPreemptiveAlgorithm(Map<Integer, List<ProcessImp>> processStore, int totalProcesses) {
        this.totalProcesses = totalProcesses;
        this.processStore = processStore;
        this.readyQueue = new TreeSet<>((p1, p2) -> {
            if (p1 == null) return 1;
            if (p2 == null) return -1;
            if (p1.getRemainingTime() == p2.getRemainingTime()) {
                return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
            }
            return Integer.compare(p1.getRemainingTime(), p2.getRemainingTime());
        });
    }

    @Override
    public void execute(SimulationListener listener) {
        // Main scheduling loop - processes the queue and scheduling decisions
        while (completedProcesses < totalProcesses) {

            // Add processes to the ready queue that have arrived at the current time
            List<ProcessImp> arrivedProcesses = processStore.get(currentTime);
            if (arrivedProcesses != null) {
                for (ProcessImp process : arrivedProcesses) {
                    if (!readyQueue.contains(process)) {
                        readyQueue.add(process); // Add to ready queue directly
                        System.out.println("Process ID: " + process.getProcessID() + " arrived at time: " + currentTime);
                        listener.onProcessArrived(process); // Notify the listener
                    }
                }
            }

            // Preempt the current process if necessary
            if (currentProcess != null && !readyQueue.isEmpty()) {
                ProcessImp shortestProcess = readyQueue.first(); // Get the process with the shortest remaining time
                if (shortestProcess.getRemainingTime() < currentProcess.getRemainingTime()) {
                    System.out.println("Preempting Process ID: " + currentProcess.getProcessID() +
                            " for Process ID: " + shortestProcess.getProcessID());
                    readyQueue.add(currentProcess); // Add the current process back to the ready queue
                    currentProcess = shortestProcess; // Switch to the new process
                    readyQueue.remove(shortestProcess); // Remove the new process from the ready queue
                    listener.onProcessPreempted(shortestProcess); // Notify the listener
                }
            }

            // If no process is running and the ready queue is not empty, start the next process
            if (currentProcess == null && !readyQueue.isEmpty()) {
                currentProcess = readyQueue.pollFirst(); // Start and remove the shortest process from the ready queue
                System.out.println("Starting Process ID: " + currentProcess.getProcessID() + " at time: " + currentTime);
                listener.onProcessStarted(currentProcess); // Notify the listener
            }

            // Execute the current process if there is one
            if (currentProcess != null) {
                System.out.println("Executing Process ID: " + currentProcess.getProcessID() + " at time: " + currentTime
                        + " | Remaining Time: " + currentProcess.getRemainingTime());

                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1); // Decrease the remaining time first
                listener.onProcessUpdated(currentProcess); // Notify listener of remaining time update

                // Check if the current process is finished
                if (currentProcess.getRemainingTime() == 0) {
                    System.out.println("Process ID: " + currentProcess.getProcessID() + " completed at time: " + (currentTime + 1));
                    currentProcess.setCompletionTime(currentTime + 1); // Set the completion time as time after finishing this second
                    currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime()); // Set turnaround time
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime()); // Set waiting time
                    listener.onProcessCompleted(currentProcess); // Notify listener of process completion
                    currentProcess = null; // Mark as no process is currently running
                    completedProcesses++; // Increment completed processes count
                }
            }

            // Now, increment the system clock only after decreasing the remaining time
            currentTime++;

            listener.onClockUpdate(currentTime); // Notify listener of clock update

            // Simulate the time increment by waiting (this can be removed for real-time display updates)
            try {
                Thread.sleep(1000); // Simulate a 1-second wait
            } catch (InterruptedException e) {
                System.out.println("Simulation interrupted.");
                break;
            }

        }

        // All processes have been completed
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
        return "SJF Preemptive Scheduling";
    }
}
