public class LeakyBucketRateLimiter {
    private final long capacity;
    private final double outflowRate;  // requests per second
    private double currentSize;
    private long lastLeakTimestamp;

    public LeakyBucketRateLimiter(long capacity, double outflowRate) {
        this.capacity = capacity;
        this.outflowRate = outflowRate;
        this.currentSize = 0;
        this.lastLeakTimestamp = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire() {
        leak();
        if (currentSize < capacity) {
            currentSize++;
            return true;
        }
        return false;
    }

    private void leak() {
        long now = System.currentTimeMillis();
        double timeElapsed = (now - lastLeakTimestamp) / 1000.0;
        double leakAmount = timeElapsed * outflowRate;
        currentSize = Math.max(0, currentSize - leakAmount);
        lastLeakTimestamp = now;
    }
} 