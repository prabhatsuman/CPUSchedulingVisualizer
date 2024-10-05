import algorithms.SJFPreemptiveAlgorithm;
import models.ProcessImp;

import java.util.*;

public class Simulator {
    public static void main(String[] args) {
        // Create a map to store processes based on their arrival time
        Map<Integer, List<ProcessImp>> processStore = new HashMap<>();

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of processes: ");
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.println("Enter Process ID: ");
            int processID = sc.nextInt();
            System.out.println("Enter Arrival Time: ");
            int arrivalTime = sc.nextInt();
            System.out.println("Enter Burst Time: ");
            int burstTime = sc.nextInt();
            System.out.println("Enter Priority: ");
            int priority = sc.nextInt();
            
            // Create a new process instance
            ProcessImp process = new ProcessImp(processID, arrivalTime, burstTime, priority);
            
            // Add the process to the process store based on its arrival time
            processStore.putIfAbsent(arrivalTime, new ArrayList<>());
            processStore.get(arrivalTime).add(process);
        }

        // Initialize the SJF Preemptive Algorithm with the process store
        SJFPreemptiveAlgorithm sjf = new SJFPreemptiveAlgorithm(processStore,n);
        sjf.execute(); // Execute the scheduling algorithm
        sc.close();
    }
}
