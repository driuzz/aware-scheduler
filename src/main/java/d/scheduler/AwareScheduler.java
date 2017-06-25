package d.scheduler;

import d.storm.*;

import java.util.*;
import java.util.stream.Collectors;

public class AwareScheduler extends Scheduler {

    @Override
    public void schedule(Topology topology, Cluster cluster) {

        Map<Supervisor, Queue<Worker>> availableSlots = new LinkedHashMap<>();
        cluster.getSupervisors().forEach(
                supervisor -> availableSlots.computeIfAbsent(supervisor, $ -> new LinkedList<>())
                                            .addAll(supervisor.getWorkers()));

        String[] isolateNames = cluster.getSupervisors().get(0).getConfig().get("isolate").split(",");
        String[] groupNames = cluster.getSupervisors().get(0).getConfig().get("group").split(",");
        // verify that groupNames has only one common group with isolateNames

        Map<String, List<Executor>> types2Executors = new HashMap<>();
        mapComponents(types2Executors, topology.getSpouts());
        mapComponents(types2Executors, topology.getBolts());

        // type -> executors table
        Map<String, List<List<Executor>>> associations = new HashMap<>();

        // add isolated
        Arrays.stream(isolateNames).forEach(type -> {
            List<List<Executor>> assocExecutors = associations.computeIfAbsent(type, $ -> new ArrayList<>());
            types2Executors.remove(type).forEach(executor -> {
                List<Executor> list = new ArrayList<>();
                list.add(executor);
                assocExecutors.add(list);
            });
        });

        // add grouped
        List<String> typesToGroup = new ArrayList<>();
        final String[] mainGroupType = {null};
        Arrays.stream(groupNames).forEach(type -> {
            if (associations.containsKey(type)) {
                mainGroupType[0] = type;
            } else {
                typesToGroup.add(type);
            }
        });
        {
            List<List<Executor>> assocExecutors = associations.get(mainGroupType[0]);
            typesToGroup.forEach(type -> types2Executors.remove(type).forEach(
                    executor -> findSmallestList(assocExecutors).ifPresent(list -> list.add(executor))));
        }

        // add rest
        {
            List<List<Executor>> assocExecutors =
                    associations.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
            types2Executors.values().stream().flatMap(Collection::stream)
                           .forEach(executor -> findSmallestList(assocExecutors).ifPresent(list -> list.add(executor)));
        }

        // map to workers (round-robin)
        List<Supervisor> supervisors = new ArrayList<>(availableSlots.keySet());
        final int[] index = {0};
        associations.values().stream().flatMap(Collection::stream).forEach(executors -> {
            Worker worker = availableSlots.get(supervisors.get(index[0]++)).poll();
            executors.forEach(executor -> associate(executor, worker));
            if (index[0] >= supervisors.size()) {
                index[0] = 0;
            }
        });
    }

    private <T> Optional<List<T>> findSmallestList(List<List<T>> lists) {
        int min = lists.stream().mapToInt(List::size).min().orElse(0);
        return lists.stream().filter(list -> list.size() == min).findFirst();
    }

    private void mapComponents(Map<String, List<Executor>> types2Executors, List<? extends Component> components) {
        components.forEach(comp -> types2Executors.computeIfAbsent(comp.getConfig().get("type"), $ -> new ArrayList<>())
                                                  .addAll(comp.getExecutors()));
    }
}
