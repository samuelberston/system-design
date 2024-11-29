public class KeyValueStore<K, V> {
    private final Map<K, V> store;
    private final WriteAheadLog wal;
    private final ReadWriteLock lock;

    public KeyValueStore(String walPath) {
        this.store = new ConcurrentHashMap<>();
        this.wal = new WriteAheadLog(walPath);
        this.lock = new ReentrantReadWriteLock();
        recoverFromWAL();
    }

    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            wal.log(new LogEntry("PUT", key, value));
            store.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public V get(K key) {
        lock.readLock().lock();
        try {
            return store.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }
}