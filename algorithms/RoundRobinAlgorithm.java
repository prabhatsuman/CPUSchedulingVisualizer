package algorithms;

import models.ProcessImp;
import visualization.SimulationListener;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;

public class RoundRobinAlgorithm implements SchedulingAlgorithm {

    private int quantum;
    private int currentTime = 0;
    private int totalProcesses;
    private int completedProcesses = 0;
    private Map<Integer, List<ProcessImp>> processStore;
    private Queue<ProcessImp> readyQueue;
    private ProcessImp currentProcess = null;
    private int timeSlice = 0;

    public RoundRobinAlgorithm(Map<Integer, List<ProcessImp>> processStore, int totalProcesses, int quantum) {
        this.totalProcesses = totalProcesses;
        this.processStore = processStore;
        this.readyQueue = new LinkedList<>();
        this.quantum = quantum;
    }

    @Override
    public void execute(SimulationListener listener) {
        while (completedProcesses < totalProcesses) {
            List<ProcessImp> arrivedProcesses = processStore.get(currentTime);
            if (arrivedProcesses != null) {
                for (ProcessImp process : arrivedProcesses) {
                    readyQueue.add(process);
                    System.out.println("Process ID: " + process.getProcessID() + " arrived at time: " + currentTime);
                    listener.onProcessArrived(process);
                }
            }

            if (currentProcess == null && !readyQueue.isEmpty()) {
                currentProcess = readyQueue.poll();
                timeSlice = 0;
                System.out.println("Starting Process ID: " + currentProcess.getProcessID() + " at time: " + currentTime);
                listener.onProcessStarted(currentProcess);
            }

            if (currentProcess != null) {
                System.out.println("Executing Process ID: " + currentProcess.getProcessID() + " at time: " + currentTime
                        + " | Remaining Time: " + currentProcess.getRemainingTime());

                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
                listener.onProcessUpdated(currentProcess);

                timeSlice++;

                if (currentProcess.getRemainingTime() == 0) {
                    System.out.println("Process ID: " + currentProcess.getProcessID() + " completed at time: " + (currentTime + 1));
                    currentProcess.setCompletionTime(currentTime + 1);
                    currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                    listener.onProcessCompleted(currentProcess);
                    currentProcess = null;
                    completedProcesses++;
                }

                if (timeSlice == quantum && currentProcess != null) {
                    System.out.println("Preempting Process ID: " + currentProcess.getProcessID() + " at time: " + currentTime);
                    readyQueue.add(currentProcess);
                    listener.onProcessPreempted(currentProcess);
                    currentProcess = null;
                }
            }

            currentTime++;
            listener.onClockUpdate(currentTime);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Simulation interrupted.");
                break;
            }
        }

        System.out.println("All processes completed at time: " + currentTime);
        listener.onSimulationCompleted(processStore.values().stream().flatMap(List::stream).toList());
        // display();
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
        }
    }

    @Override
    public String getAlgorithmName() {
        return "Round Robin Scheduling";
    }
}
