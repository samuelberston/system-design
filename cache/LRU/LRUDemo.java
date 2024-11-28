public class LRUDemo {
    public static void main(String[] args) {
        // Demo with String keys and Integer values
        System.out.println("Demo 1: String -> Integer cache");
        LRU<String, Integer> scoreCache = new LRU<>(3); // Cache with capacity 3
        
        scoreCache.put("Alice", 95);
        scoreCache.put("Bob", 89);
        scoreCache.put("Charlie", 91);
        System.out.println("Alice's score: " + scoreCache.get("Alice")); // Output: 95
        
        // This will evict "Bob" since it's least recently used
        scoreCache.put("David", 88);
        System.out.println("Bob's score: " + scoreCache.get("Bob")); // Output: null
        
        // Demo with Integer keys and String values
        System.out.println("\nDemo 2: Integer -> String cache");
        LRU<Integer, String> userCache = new LRU<>(2); // Cache with capacity 2
        
        userCache.put(1, "John Doe");
        userCache.put(2, "Jane Smith");
        System.out.println("User 1: " + userCache.get(1)); // Output: John Doe
        
        // This will evict ID 2 since it's least recently used
        userCache.put(3, "Bob Johnson");
        System.out.println("User 2: " + userCache.get(2)); // Output: null
        System.out.println("User 3: " + userCache.get(3)); // Output: Bob Johnson
        
        // Demonstrate updating existing key
        userCache.put(1, "John Doe Updated");
        System.out.println("Updated User 1: " + userCache.get(1)); // Output: John Doe Updated
    }
}