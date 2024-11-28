// Producer.java
public class Producer implements Runnable { // implements Runnable allows the class to be run as a thread
    private MessageQueue<String> queue;
    private String message;
    private int delay; // in milliseconds

    public Producer(MessageQueue<String> queue, String message, int delay) {
        this.queue = queue;
        this.message = message;
        this.delay = delay;
    }

    @Override
    public void run() { // run() is the method that is called when the thread is started
        try {
            Thread.sleep(delay);
            queue.produce(message);
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt();
            System.out.println("Producer interrupted.");
        }
    }
}
