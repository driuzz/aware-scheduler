package d.storm;

import java.util.List;

public class Bolt extends Component {

    public Bolt(String id, List<Executor> executors) {
        super(id, executors);
    }
}
