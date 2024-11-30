public class LeakyBucketDemo {
    public static void main(String[] args) throws InterruptedException {
        // Create a rate limiter with capacity of 5 and outflow rate of 1 request per second
        LeakyBucketRateLimiter rateLimiter = new LeakyBucketRateLimiter(5, 1.0);
        
        // Simulate burst of requests
        System.out.println("Simulating burst of 7 requests:");
        for (int i = 1; i <= 7; i++) {
            boolean accepted = rateLimiter.tryAcquire();
            System.out.printf("Request %d: %s%n", i, accepted ? "Accepted" : "Rejected");
        }
        
        // Wait for some requests to leak out
        System.out.println("\nWaiting 3 seconds...");
        Thread.sleep(3000);
        
        // Try more requests
        System.out.println("\nTrying 3 more requests:");
        for (int i = 1; i <= 3; i++) {
            boolean accepted = rateLimiter.tryAcquire();
            System.out.printf("Request %d: %s%n", i, accepted ? "Accepted" : "Rejected");
        }
    }
} 