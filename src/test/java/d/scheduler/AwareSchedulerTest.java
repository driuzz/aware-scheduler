package d.scheduler;

import d.storm.*;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AwareSchedulerTest {
    private AwareScheduler scheduler;
    private Topology topology;
    private Cluster cluster;

    @Before
    public void setUp() throws Exception {
        topology = new Topology("TOPO", //
                                Collections.singletonList(component(Spout.class, "SPOUT", "listener", "E1")),
                                Arrays.asList(component(Bolt.class, "PROC1", "processor", "E2"),
                                              component(Bolt.class, "PROC2", "processor", "E3", "E31"),
                                              component(Bolt.class, "PROC3", "processor", "E4"),
                                              component(Bolt.class, "PROC4", "processor", "E5"),
                                              component(Bolt.class, "AGG", "aggregator", "E6", "E7", "E8", "E9"),
                                              component(Bolt.class, "REPORT", "report", "E10", "E11"),
                                              component(Bolt.class, "WRITER", "writer", "E12")));

        cluster = new Cluster("CLUSTER", Arrays.asList(
                supervisor("SUPER1", "listener,processor", "aggregator,processor", "W1", "W2", "W3"),
                supervisor("SUPER2", null, null, "W4", "W5", "W6"),
                supervisor("SUPER3", null, null, "W7", "W8", "W9")));

        scheduler = new AwareScheduler();
    }

    @Test
    public void schedule() throws Exception {
        scheduler.schedule(topology, cluster);
        scheduler.print();
    }

    private static <T extends Component> T component(Class<T> clazz, String id, String type, String... executorIds) {
        try {
            T instance = clazz.getConstructor(String.class, List.class)
                              .newInstance(id, Stream.of(executorIds).map(Executor::new).collect(Collectors.toList()));
            instance.getConfig().put("type", type);
            return instance;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Supervisor supervisor(String id, String isolate, String group, String... workers) {
        Supervisor supervisor = new Supervisor(id, Stream.of(workers).map(Worker::new).collect(Collectors.toList()));
        supervisor.getConfig().put("isolate", isolate);
        supervisor.getConfig().put("group", group);
        return supervisor;
    }
}