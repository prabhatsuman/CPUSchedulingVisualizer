package visualization;

import models.ProcessImp;
import java.util.List;

public interface SimulationListener {
    void onProcessArrived(ProcessImp process);
    void onProcessPreempted(ProcessImp process);
    void onProcessStarted(ProcessImp process);
    void onProcessUpdated(ProcessImp process);
    void onProcessCompleted(ProcessImp process);
    void onClockUpdate(int currentTime);
    void onSimulationCompleted(List<ProcessImp> processes);
    void onReadyQueueUpdated(Object readyQueue);
}
