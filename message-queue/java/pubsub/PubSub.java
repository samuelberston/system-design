// PubSub.java
import java.util.*;
import java.util.concurrent.*;

public class PubSub<T> {
    // Map of topic names to list of subscribers
    private final Map<String, List<Subscriber<T>>> subscribersTopicMap;

    // Executor service to handle message dispatching asynchronously
    private final ExecutorService executor;

    public PubSub() {
        this.subscribersTopicMap = new ConcurrentHashMap<>(); // ConcurrentHashMap is thread-safe and allows for concurrent access
        this.executor = Executors.newCachedThreadPool(); // newCachedThreadPool creates a thread pool that reuses a fixed number of threads operating off a shared unbounded queue
    }

    /**
     * Subscribers call this method to subscribe to a specific topic.
     *
     * @param topic      The topic to subscribe to.
     * @param subscriber The subscriber instance.
     */
    public void subscribe(String topic, Subscriber<T> subscriber) {
        // computeIfAbsent is used to get the list of subscribers for a topic. If the topic is not present, it creates a new list and adds the subscriber to it.
        subscribersTopicMap.computeIfAbsent(topic, k -> Collections.synchronizedList(new ArrayList<>())).add(subscriber);        
        System.out.println("Subscriber " + subscriber.getId() + " subscribed to topic: " + topic);
    }

    /**
     * Publishers call this method to publish messages to a specific topic.
     *
     * @param topic   The topic to publish to.
     * @param message The message to publish.
     */
    public void publish(String topic, T message) {
        // get the list of subscribers for the topic
        List<Subscriber<T>> subscribers = subscribersTopicMap.get(topic);
        // if the list of subscribers is not empty, publish the message to each subscriber
        if (subscribers != null && !subscribers.isEmpty()) {
            // Create thread-safe copy of the list of subscribers to avoid ConcurrentModificationException
            List<Subscriber<T>> subscribersCopy = new ArrayList<>(subscribers);
            System.out.println("Publishing message to topic '" + topic + "': " + message);
            for (Subscriber<T> subscriber : subscribersCopy) {
                // Dispatch message asynchronously to subscribers
                executor.submit(() -> subscriber.receive(message));
            }
        } else {
            System.out.println("No subscribers for topic '" + topic + "'. Message not delivered: " + message);
        }
    }

    /**
     * Unsubscribes a subscriber from a specific topic.
     *
     * @param topic      The topic to unsubscribe from.
     * @param subscriber The subscriber instance.
     */
    public void unsubscribe(String topic, Subscriber<T> subscriber) {
        List<Subscriber<T>> subscribers = subscribersTopicMap.get(topic);
        if (subscribers != null) {
            subscribers.remove(subscriber);
            // Remove the topic if there are no subscribers left
            if (subscribers.isEmpty()) {
                subscribersTopicMap.remove(topic);
            }
            System.out.println("Subscriber " + subscriber.getId() + " unsubscribed from topic: " + topic);
        }
    }

    /**
     * Shutdown the executor service gracefully.
     */
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate.");
                }
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
