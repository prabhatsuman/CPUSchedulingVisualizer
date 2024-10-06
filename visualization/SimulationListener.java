package visualization;

import models.ProcessImp;

public interface SimulationListener {
    void onProcessArrived(ProcessImp process);
    void onProcessPreempted(ProcessImp process);
    void onProcessStarted(ProcessImp process);
    void onProcessUpdated(ProcessImp process);
    void onProcessCompleted(ProcessImp process);
    void onClockUpdate(int currentTime);
}
