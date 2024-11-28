// Publisher.java
public class Publisher implements Runnable {
    private final String id;
    private final PubSub<String> pubSub;
    private final String topic;
    private final String message;
    private final int delay; // in milliseconds

    public Publisher(String id, PubSub<String> pubSub, String topic, String message, int delay) {
        this.id = id;
        this.pubSub = pubSub;
        this.topic = topic;
        this.message = message;
        this.delay = delay;
    }

    /**
     * Publishes the message to the specified topic after the given delay.
     */
    @Override
    public void run() {
        try {
            Thread.sleep(delay);
            System.out.println("Publisher " + id + " publishing to topic '" + topic + "': " + message);
            pubSub.publish(topic, message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Publisher " + id + " interrupted.");
        }
    }
}
