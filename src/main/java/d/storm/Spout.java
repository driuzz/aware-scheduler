package d.storm;

import java.util.List;

public class Spout extends Component {

    public Spout(String id, List<Executor> executors) {
        super(id, executors);
    }
}
