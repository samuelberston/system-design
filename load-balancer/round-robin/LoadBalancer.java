import java.util.ArrayList;
import java.util.List;

class LoadBalancer {
    private List<String> servers;
    private int currentIndex;

    public LoadBalancer(List<String> servers) {
        this.servers = new ArrayList<>(servers);
        this.currentIndex = 0;
    }

    public String getNextServer() {
        String nextServer = servers.get(currentIndex);
        currentIndex = (currentIndex + 1) % servers.size();
        return nextServer;
    }
}