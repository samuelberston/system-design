public class LeastConnectionLoadBalancerExample {
    public static void main(String[] args) {
        // Create a Least Connection load balancer
        LeastConnectionLoadBalancer loadBalancer = new LeastConnectionLoadBalancer();

        // Add servers to the load balancer
        loadBalancer.addServer("Server1");
        loadBalancer.addServer("Server2");
        loadBalancer.addServer("Server3");

        // Simulate requests and print the server to which each request is routed
        for (int i = 0; i < 10; i++) {
            String selectedServer = loadBalancer.getServerWithLeastConnections();
            System.out.println("Request " + (i + 1) + ": Routed to " + selectedServer);
        }
    }
}