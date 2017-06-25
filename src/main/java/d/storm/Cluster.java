package d.storm;

import java.util.List;
import java.util.Objects;

public class Cluster {
    private final String id;
    private final List<Supervisor> supervisors;

    public Cluster(String id, List<Supervisor> supervisors) {
        this.id = id;
        this.supervisors = supervisors;
    }

    public String getId() {
        return id;
    }

    public List<Supervisor> getSupervisors() {
        return supervisors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cluster cluster = (Cluster) o;
        return Objects.equals(id, cluster.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cluster{id='" + id + "'}";
    }
}
