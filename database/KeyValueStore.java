import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

    private void recoverFromWAL() {
        lock.writeLock().lock();
        try {
            for (LogEntry entry : wal.readEntries()) {
                if ("PUT".equals(entry.operation)) {
                    @SuppressWarnings("unchecked")
                    K key = (K) entry.key;
                    @SuppressWarnings("unchecked")
                    V value = (V) entry.value;
                    store.put(key, value);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Inner class for Write Ahead Log
    private static class WriteAheadLog {
        private final String path;
        private final File file;

        public WriteAheadLog(String path) {
            this.path = path;
            this.file = new File(path);
            createFileIfNotExists();
        }

        private void createFileIfNotExists() {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to create WAL file", e);
                }
            }
        }

        public void log(LogEntry entry) {
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(file, true))) {
                oos.writeObject(entry);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write to WAL", e);
            }
        }

        public Iterable<LogEntry> readEntries() {
            if (!file.exists() || file.length() == 0) {
                return () -> new java.util.ArrayList<LogEntry>().iterator();
            }

            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(file))) {
                java.util.List<LogEntry> entries = new java.util.ArrayList<>();
                while (true) {
                    try {
                        LogEntry entry = (LogEntry) ois.readObject();
                        entries.add(entry);
                    } catch (EOFException e) {
                        break;
                    }
                }
                return entries;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("Failed to read from WAL", e);
            }
        }
    }

    // Inner class for Log Entries
    private static class LogEntry implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String operation;
        private final Object key;
        private final Object value;

        public LogEntry(String operation, Object key, Object value) {
            this.operation = operation;
            this.key = key;
            this.value = value;
        }
    }
}