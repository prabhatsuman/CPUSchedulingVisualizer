package algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import models.ProcessImp;

public class FCFSAlgorithm implements SchedulingAlgorithm {

    List <ProcessImp> processes;
    private int currentTime;

    public FCFSAlgorithm() {
        this.processes = new LinkedList<>();
        this.currentTime = 0;
    }

    @Override
    public void add(ProcessImp process) {
        this.processes.add(process);
    }

    @Override
    public void remove(ProcessImp process) {
        this.processes.remove(process);
    }

    @Override
    public void execute()
    {
        processes.sort((p1, p2) -> p1.getArrivalTime() - p2.getArrivalTime());
        while(!processes.isEmpty())
        {
            ProcessImp process = processes.get(0);
            waitProcessArrival(process);

            Thread processThread = new Thread(process);
            processThread.start();

            try {
                processThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            currentTime += process.getBurstTime();

            System.out.println("Process ID: " + process.getProcessID() + " | Completion Time: " + currentTime);

            process.setCompletionTime(currentTime);

            remove(process);

        }
        System.out.println("All processes completed");

    }
    @Override
    public void display()
    {
        for(ProcessImp process : processes)
        {
            System.out.println("Process Id : "+ process.getProcessID() );
        }
    }

    public void waitProcessArrival(ProcessImp process) {
        while (currentTime < process.getArrivalTime()) {
            System.out.println("Waiting for process arrival: " + process.getProcessID());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTime++;
        }
    }
    

}

