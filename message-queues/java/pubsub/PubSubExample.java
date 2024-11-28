// PubSubExample.java
public class PubSubExample {
    public static void main(String[] args) {
        // Instantiate the PubSub system
        PubSub<String> pubSub = new PubSub<>();

        // Create subscribers
        Subscriber<String> subscriber1 = new Subscriber<>("Subscriber1");
        Subscriber<String> subscriber2 = new Subscriber<>("Subscriber2");
        Subscriber<String> subscriber3 = new Subscriber<>("Subscriber3");

        // Start subscriber threads
        Thread subThread1 = new Thread(subscriber1);
        Thread subThread2 = new Thread(subscriber2);
        Thread subThread3 = new Thread(subscriber3);
        subThread1.start();
        subThread2.start();
        subThread3.start();

        // Subscribers subscribe to topics
        pubSub.subscribe("Sports", subscriber1);
        pubSub.subscribe("News", subscriber1);
        pubSub.subscribe("News", subscriber2);
        pubSub.subscribe("Technology", subscriber3);

        // Create publishers
        Publisher publisher1 = new Publisher("Publisher1", pubSub, "Sports", "Sports News 1", 1000);
        Publisher publisher2 = new Publisher("Publisher2", pubSub, "News", "Breaking News!", 2000);
        Publisher publisher3 = new Publisher("Publisher3", pubSub, "Technology", "Tech Update 1", 3000);
        Publisher publisher4 = new Publisher("Publisher4", pubSub, "News", "Daily News Digest", 4000);
        Publisher publisher5 = new Publisher("Publisher5", pubSub, "Sports", "Sports News 2", 5000);

        // Start publisher threads
        Thread pubThread1 = new Thread(publisher1);
        Thread pubThread2 = new Thread(publisher2);
        Thread pubThread3 = new Thread(publisher3);
        Thread pubThread4 = new Thread(publisher4);
        Thread pubThread5 = new Thread(publisher5);
        pubThread1.start();
        pubThread2.start();
        pubThread3.start();
        pubThread4.start();
        pubThread5.start();

        // Let the system run for a bit
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Shutdown subscribers
        subscriber1.stop();
        subscriber2.stop();
        subscriber3.stop();

        // Shutdown PubSub executor
        pubSub.shutdown();

        // Interrupt subscriber threads to exit blocking take()
        subThread1.interrupt();
        subThread2.interrupt();
        subThread3.interrupt();

        System.out.println("Pub/Sub system shutdown.");
    }
}
