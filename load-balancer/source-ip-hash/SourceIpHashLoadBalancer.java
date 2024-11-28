import java.util.HashMap;
import java.util.Map;

class SourceIpHashLoadBalancer {
    private Map<String, String> ipToServerMap;

    public SourceIpHashLoadBalancer() {
        this.ipToServerMap = new HashMap<>();
    }

    public void addServer(String serverName) {
        // Add server to the mapping
        ipToServerMap.put(serverName, serverName);
    }

    public String getServerForIp(String sourceIp) {
        // Calculate hash of the source IP
        int hash = sourceIp.hashCode();

        // Get the list of available servers
        String[] servers = ipToServerMap.keySet().toArray(new String[0]);

        // Map the hash value to a server index
        int serverIndex = Math.abs(hash) % servers.length;

        // Return the selected server
        return servers[serverIndex];
    }
}

