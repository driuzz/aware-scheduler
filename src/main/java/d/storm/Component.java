package d.storm;

import java.util.List;
import java.util.Objects;

public class Component {
    private final String id;
    private final List<Executor> executors;
    private final Config config;

    public Component(String id, List<Executor> executors) {
        this.id = id;
        this.executors = executors;
        this.config = new Config();
    }

    public String getId() {
        return id;
    }

    public List<Executor> getExecutors() {
        return executors;
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
        Component component = (Component) o;
        return Objects.equals(id, component.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Component{id='" + id + "', config=" + config + '}';
    }
}
