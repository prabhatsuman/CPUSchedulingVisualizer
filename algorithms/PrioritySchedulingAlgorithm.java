package algorithms;

import models.ProcessImp;
import visualization.SimulationListener;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class PrioritySchedulingAlgorithm implements SchedulingAlgorithm {
    private int currentTime = 0;
    private int totalProcesses;
    private int completedProcesses = 0;
    private Map<Integer, List<ProcessImp>> processStore;
    private TreeSet<ProcessImp> readyQueue;
    private ProcessImp currentProcess = null;

    public PrioritySchedulingAlgorithm(Map<Integer, List<ProcessImp>> processStore, int totalProcesses) {
        this.totalProcesses = totalProcesses;
        this.processStore = processStore;
        this.readyQueue = new TreeSet<>((p1, p2) -> {
            if (p1.getPriority() == p2.getPriority()) {
                if(p1.getArrivalTime() == p2.getArrivalTime()) {
                    return Integer.compare(p1.getProcessID(), p2.getProcessID());
                }
                return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
            }
            return Integer.compare(p2.getPriority(), p1.getPriority()); 
        });
    }

    @Override
    public void execute(SimulationListener listener) {
        while (completedProcesses < totalProcesses) {
            List<ProcessImp> arrivedProcesses = processStore.get(currentTime);

            if (arrivedProcesses != null) {
                for (ProcessImp process : arrivedProcesses) {
                    if (!readyQueue.contains(process)) {
                        readyQueue.add(process);
                        listener.onReadyQueueUpdated(readyQueue);
                        listener.onProcessArrived(process);
                    }
                }
            }

            if (currentProcess == null && !readyQueue.isEmpty()) {
                currentProcess = readyQueue.pollFirst();
                listener.onProcessStarted(currentProcess);
                listener.onReadyQueueUpdated(readyQueue);
            } else if (currentProcess != null && !readyQueue.isEmpty()) {
                ProcessImp highestPriorityProcess = readyQueue.first();
                if (highestPriorityProcess.getPriority() > currentProcess.getPriority()) {
                    listener.onProcessPreempted(currentProcess);
                    readyQueue.add(currentProcess);
                    currentProcess = readyQueue.pollFirst();
                    listener.onProcessStarted(currentProcess);
                    listener.onReadyQueueUpdated(readyQueue);
                }
            }

            if (currentProcess != null) {
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
                listener.onProcessUpdated(currentProcess);

                if (currentProcess.getRemainingTime() == 0) {
                    currentProcess.setCompletionTime(currentTime + 1);
                    currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                    listener.onProcessCompleted(currentProcess);
                    currentProcess = null;
                    completedProcesses++;
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

        listener.onSimulationCompleted(processStore.values().stream().flatMap(List::stream).toList());
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
        return "Priority Scheduling Algorithm (Preemptive)";
    }
}
