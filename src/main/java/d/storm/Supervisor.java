package d.storm;

import java.util.List;
import java.util.Objects;

public class Supervisor {
    private final String id;
    private final List<Worker> workers;
    private final Config config;

    public Supervisor(String id, List<Worker> workers) {
        this.id = id;
        this.workers = workers;
        this.config = new Config();
    }

    public String getId() {
        return id;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public Config getConfig() {
        return config;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Supervisor that = (Supervisor) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Supervisor{id='" + id + "', config=" + config + '}';
    }
}
