package algorithms;

import models.ProcessImp;

public interface SchedulingAlgorithm {
    void add(ProcessImp process);
    void remove(ProcessImp process);
    void execute();
    void display();
}
