package d.scheduler;

import d.storm.Cluster;
import d.storm.Executor;
import d.storm.Topology;
import d.storm.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Scheduler {

    private Map<Worker, List<Executor>> associations = new HashMap<>();

    public abstract void schedule(Topology topology, Cluster cluster);

    protected void associate(Executor executor, Worker worker) {
        associations.computeIfAbsent(worker, $ -> new ArrayList<>());
        associations.get(worker).add(executor);
    }

    public void print() {
        associations.forEach((worker, executors) -> {
            System.out.println(worker + " -> " + executors);
        });
    }
}
