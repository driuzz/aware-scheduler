package d.storm;

import java.util.List;
import java.util.Objects;

public class Topology {
    private final String id;
    private final List<Spout> spouts;
    private final List<Bolt> bolts;

    public Topology(String id, List<Spout> spouts, List<Bolt> bolts) {
        this.id = id;
        this.spouts = spouts;
        this.bolts = bolts;
    }

    public String getId() {
        return id;
    }

    public List<Spout> getSpouts() {
        return spouts;
    }

    public List<Bolt> getBolts() {
        return bolts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Topology topology = (Topology) o;
        return Objects.equals(id, topology.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Topology{id='" + id + "'}";
    }
}
