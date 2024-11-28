import java.util.ArrayList;
import java.util.List;

public class RoundRobinExample {
    public static void main(String[] args) {
        // Sample list of servers
        List<String> serverList = new ArrayList<>();
        serverList.add("Server1");
        serverList.add("Server2");
        serverList.add("Server3");

        // Create a load balancer with the server list
        LoadBalancer loadBalancer = new LoadBalancer(serverList);

        // Simulate requests to the load balancer
        for (int i = 0; i < 10; i++) {
            String nextServer = loadBalancer.getNextServer();
            System.out.println("Request " + (i + 1) + ": Routed to " + nextServer);
        }
    }
}