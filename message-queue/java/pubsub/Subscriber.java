// Subscriber.java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Subscriber<T> implements Runnable {
    private final String id;
    private final BlockingQueue<T> messageQueue;
    private volatile boolean active;

    public Subscriber(String id) {
        this.id = id;
        this.messageQueue = new LinkedBlockingQueue<>(); // LinkedBlockingQueue is a thread-safe queue that supports blocking operations
        this.active = true;
    }

    public String getId() {
        return id;
    }

    /**
     * Called by PubSub to deliver a message to this subscriber.
     *
     * @param message The message to receive.
     */
    public void receive(T message) {
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Subscriber " + id + " interrupted while receiving message.");
        }
    }

    /**
     * Processes messages from the message queue.
     */
    @Override
    public void run() {
        System.out.println("Subscriber " + id + " started.");
        while (active) {
            try {
                T message = messageQueue.take();
                processMessage(message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Subscriber " + id + " interrupted.");
            }
        }
        System.out.println("Subscriber " + id + " stopped.");
    }

    /**
     * Simulates message processing.
     *
     * @param message The message to process.
     */
    private void processMessage(T message) {
        System.out.println("Subscriber " + id + " processed message: " + message);
        // Simulate processing time
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Subscriber " + id + " interrupted during message processing.");
        }
    }

    /**
     * Stops the subscriber gracefully.
     */
    public void stop() {
        active = false;
        // Interrupt if waiting on the queue
        Thread.currentThread().interrupt();
    }
}
