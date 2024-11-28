// MessageQueueExample.java
public class MessageQueueExample {
    public static void main(String[] args) {
        // Instantiate the message queue with a capacity of 10
        MessageQueue<String> queue = new MessageQueue<>(10);

        // Start consumers
        Thread consumer1 = new Thread(new Consumer(queue, 1));
        Thread consumer2 = new Thread(new Consumer(queue, 2));
        consumer1.start();
        consumer2.start();

        // Start producers
        Thread producer1 = new Thread(new Producer(queue, "Message 1", 1000)); // Produce after 1 second
        Thread producer2 = new Thread(new Producer(queue, "Message 2", 2000)); // Produce after 2 seconds
        Thread producer3 = new Thread(new Producer(queue, "Message 3", 3000)); // Produce after 3 seconds
        Thread producer4 = new Thread(new Producer(queue, "Message 4", 4000)); // Produce after 4 seconds

        producer1.start();
        producer2.start();
        producer3.start();
        producer4.start();
    }
}
