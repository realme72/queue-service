import java.util.Comparator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InMemoryPriorityQueue extends ConcurrentLinkedQueue<Message> {
    private Comparator<Message> priorityComparator;

    public InMemoryPriorityQueue(Comparator<Message> priorityComparator) {
        this.priorityComparator = priorityComparator;
    }

    @Override
    public boolean offer(Message message) {
        for (Message existingMessage : this) {
            if (priorityComparator.compare(message, existingMessage) < 0) {
                addBefore(existingMessage, message);
                return true;
            }
        }
        return super.offer(message);
    }

    private void addBefore(Message existingMessage, Message message) {
        if (existingMessage == null) {
            add(message);
        } else {
            remove(message);
            add(message);
            add(existingMessage);
        }
    }
}
