public class SourceIpHashLoadBalancerExample {
    public static void main(String[] args) {
        // Create a source IP hash load balancer
        SourceIpHashLoadBalancer loadBalancer = new SourceIpHashLoadBalancer();

        // Add servers to the load balancer
        loadBalancer.addServer("Server1");
        loadBalancer.addServer("Server2");
        loadBalancer.addServer("Server3");

        // Simulate requests with different source IPs
        String[] sourceIps = {"192.168.1.1", "10.0.0.1", "172.16.0.1"};

        for (String sourceIp : sourceIps) {
            String selectedServer = loadBalancer.getServerForIp(sourceIp);
            System.out.println("Request from " + sourceIp + " routed to " + selectedServer);
        }
    }
}