import redis.clients.jedis.Jedis;

public class UpstashPriorityQueue {

    private static final String QUEUE_KEY = "priority_queue";
    private Jedis jedis;

    public UpstashPriorityQueue() {
        // Initialize Jedis instance
        this.jedis = new Jedis("usw1-top-kodiak-33259.upstash.io", 33259, true);
        this.jedis.auth("30e27c28d20e48c2a000fd6d081e3f9c"); 
    }

    public static void main(String[] args) {
        
        UpstashPriorityQueue queue = new UpstashPriorityQueue();
        queue.enqueue("urgent message", 1);
        queue.enqueue("normal message", 2);
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
    }

    public void enqueue(String message, int priority) {
        jedis.zadd(QUEUE_KEY, priority, message);
    }

    public String dequeue() {
        String message = jedis.zrange(QUEUE_KEY, 0, 0).iterator().next();
        jedis.zrem(QUEUE_KEY, message);
        return message;
    }
}
