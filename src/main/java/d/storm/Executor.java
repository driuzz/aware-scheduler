package d.storm;

import java.util.Objects;

public class Executor {
    private final String id;

    public Executor(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Executor executor = (Executor) o;
        return Objects.equals(id, executor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Executor{id='" + id + "'}";
    }
}
