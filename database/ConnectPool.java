public class ConnectionPool {
    private final BlockingQueue<Connection> pool;
    private final int maxSize;
    private final String url;
    private final String username;
    private final String password;

    public ConnectionPool(int maxSize, String url, String username, String password) {
        this.maxSize = maxSize;
        this.url = url;
        this.username = username;
        this.password = password;
        this.pool = new LinkedBlockingQueue<>(maxSize);
        initializePool();
    }

    private void initializePool() {
        for (int i = 0; i < maxSize; i++) {
            pool.offer(createConnection());
        }
    }

    public Connection getConnection() throws InterruptedException {
        return pool.take();
    }

    public void releaseConnection(Connection connection) {
        pool.offer(connection);
    }
}