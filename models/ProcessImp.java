package models;

public class ProcessImp implements Runnable {
    private int ProcessID;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int waitingTime;
    private int turnaroundTime;
    private int remainingTime;
    private int completionTime;
    private boolean isCompleted;

    public ProcessImp(int ProcessID, int arrivalTime, int burstTime, int priority) {
        this.ProcessID = ProcessID;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.remainingTime = burstTime;
        this.completionTime = 0;
        this.isCompleted = false;
    }
    public int getProcessID() {
        return ProcessID;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getBurstTime() {
        return burstTime;
    }
    public int getPriority() {
        return priority;
    }
    public int getWaitingTime() {
        return waitingTime;
    }
    public int getTurnaroundTime() {
        return turnaroundTime;
    }
    public int getRemainingTime() {
        return remainingTime;
    }
    public int getCompletionTime() {
        return completionTime;
    }
    public boolean isCompleted() {
        return isCompleted;
    }
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }
    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }
    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    @Override
    public void run(){
        try{
            while(remainingTime > 0){
                remainingTime--;
                Thread.sleep(1000);

                System.out.println("Process ID: " + ProcessID + " | Remaining Time: " + remainingTime);
            }
            isCompleted = true;
            
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
