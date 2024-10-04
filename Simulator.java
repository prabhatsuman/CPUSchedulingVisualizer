

import algorithms.*;
import models.*;
import java.util.Scanner;

public class Simulator {
    public static void main(String[] args) {
        //test fcfs
        FCFSAlgorithm fcfs = new FCFSAlgorithm();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of processes: ");
        int n = sc.nextInt();
        for(int i = 0; i < n; i++)
        {
            System.out.println("Enter Process ID: ");
            int processID = sc.nextInt();
            System.out.println("Enter Arrival Time: ");
            int arrivalTime = sc.nextInt();
            System.out.println("Enter Burst Time: ");
            int burstTime = sc.nextInt();
            System.out.println("Enter Priority: ");
            int priority = sc.nextInt();
            ProcessImp process = new ProcessImp(processID, arrivalTime, burstTime, priority);
            fcfs.add(process);
        }
        fcfs.execute();
    }

}
