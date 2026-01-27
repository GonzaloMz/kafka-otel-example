# Observability

This application uses Open Telemetry and Zipking to provide observability over the events exchanged by microservices.
Once all containers are running, all micro-services send data to collector, wich export traces to Zipking. Zipking UI it is available in http://localhost:9411/zipkin
To view traces in Zipking, follow these steps:

1. Open your web browser and navigate to http://localhost:9411/zipkin.
2. In the "Service Name" dropdown, select the service you want to investigate (e.g., user-service, stock-service, order-service, billing-service).
3. Optionally, you can filter traces by entering specific trace IDs or time ranges.
4. Click on the "Find Traces" button to view the traces for the selected service.
5. Click on individual traces to see detailed information about each span, including timestamps, duration, and any associated metadata.

By following these steps, you can effectively monitor and troubleshoot the interactions between microservices in your system using Zipking's observability features.