// Consumer.java
public class Consumer implements Runnable {
    private MessageQueue<String> queue;
    private int id;

    public Consumer(MessageQueue<String> queue, int id) {
        this.queue = queue;
        this.id = id;
    }

    @Override
    public void run() { 
        try {
            while (true) { // while loop to keep the consumer running indefinitely
                String message = queue.consume();
                System.out.println("Consumer " + id + " processed message: " + message);
                // Simulate processing time
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Consumer " + id + " interrupted.");
        }
    }
}

