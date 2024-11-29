import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LeastConnectionLoadBalancer {
    private Map<String, Integer> serverConnections;

    public LeastConnectionLoadBalancer() {
        this.serverConnections = new HashMap<>();
    }

    public void addServer(String serverName) {
        // Add a server to the load balancer with 0 initial connections
        serverConnections.put(serverName, 0);
    }

    public String getServerWithLeastConnections() {
        // Find the server with the least active connections
        int minConnections = Integer.MAX_VALUE;
        String selectedServer = null;

        for (Map.Entry<String, Integer> entry : serverConnections.entrySet()) {
            if (entry.getValue() < minConnections) {
                minConnections = entry.getValue();
                selectedServer = entry.getKey();
            }
        }

        // Increment the connection count for the selected server
        if (selectedServer != null) {
            serverConnections.put(selectedServer, minConnections + 1);
        }

        return selectedServer;
    }
}
