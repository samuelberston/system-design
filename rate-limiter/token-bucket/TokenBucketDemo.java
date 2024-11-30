public class TokenBucketDemo {
    public static void main(String[] args) throws InterruptedException {
        // Create a rate limiter with capacity of 5 tokens and refill rate of 2 tokens per second
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 2);
        
        // Demo 1: Burst requests
        System.out.println("Demo 1: Testing burst requests...");
        for (int i = 0; i < 7; i++) {
            boolean allowed = limiter.tryAcquire();
            System.out.println("Request " + (i + 1) + ": " + (allowed ? "Allowed" : "Blocked"));
        }
        
        // Demo 2: Continuous requests with refill
        System.out.println("\nDemo 2: Testing continuous requests with refill...");
        for (int i = 0; i < 5; i++) {
            boolean allowed = limiter.tryAcquire();
            System.out.println("Time " + i + "s - Request: " + (allowed ? "Allowed" : "Blocked"));
            Thread.sleep(1000); // Wait 1 second between requests
        }
    }
} 