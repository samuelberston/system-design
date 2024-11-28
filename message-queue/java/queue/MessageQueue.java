// MessageQueue.java
import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue<T> { // Generic type T allows for any type of message
    private Queue<T> queue = new LinkedList<>(); // Using a LinkedList to implement the queue
    private final int capacity; // Maximum number of messages the queue can hold

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    // Method for producers to add messages to the queue
    public synchronized void produce(T message) throws InterruptedException { // synchronized ensures that only one thread can access the method at a time
        while (queue.size() == capacity) {
            // Wait if the queue is full
            wait();
        }
        queue.add(message);
        System.out.println("Produced message: " + message);
        // Notify consumers that a new message is available
        notifyAll(); // notifyAll() wakes up all threads that are waiting on the queue
    }

    // Method for consumers to retrieve messages from the queue
    public synchronized T consume() throws InterruptedException {
        while (queue.isEmpty()) {
            // Wait if the queue is empty
            wait();
        }
        T message = queue.poll(); // poll() removes and returns the head of the queue, or null if the queue is empty
        System.out.println("Consumed message: " + message);
        // Notify producers that there's space available
        notifyAll();
        return message;
    }
}


/**
 * 
 * LinkedBlockingQueue is a thread-safe queue that is implemented using a linked node structure.
 * It is a bounded queue, meaning that it has a maximum capacity that can be specified when the queue is created.
 * When the queue is full, the producer threads will wait until there is space available in the queue.
 * When the queue is empty, the consumer threads will wait until there is a message available in the queue.
 * 
 * import java.util.concurrent.BlockingQueue;
 * import java.util.concurrent.LinkedBlockingQueue;
 * 
 * public class MessageQueue<T> {
 *     private BlockingQueue<T> queue;
 * 
 *     public MessageQueue(int capacity) {
 *         queue = new LinkedBlockingQueue<>(capacity);
 *     }
 * 
 *      public void produce(T message) throws InterruptedException {
 *         queue.put(message);
 *         System.out.println("Produced message: " + message);
 *      }
 * 
 *     public T consume() throws InterruptedException {
 *         T message = queue.take();
 *         System.out.println("Consumed message: " + message);
 *         return message;
 *     }
 * }
 */