# Real-Time Event Processing System with Apache Kafka

A simple demo application showcasing the capabilities of **Apache Kafka** using Node.js. This project demonstrates Kafka's strengths in handling high-throughput, fault-tolerant, and scalable data streams through a real-time event processing system.

## 🚀 Features

- **Producer:** Simulates real-time user events and publishes them to a Kafka topic.
- **Logger Consumer:** Subscribes to the Kafka topic and logs each received event.
- **Analytics Consumer:** Processes events to perform simple real-time analytics, such as counting user actions.
- **Scalable Architecture:** Demonstrates Kafka's pub-sub model with multiple consumers.
- **Dockerized Setup:** Easily set up Kafka and Zookeeper using Docker Compose.

## 🏗 Architecture

[Producer] ---> [Kafka Topic: events] 
                            | 
                ----------------------------- 
                |                           | 
        [Logger Consumer]          [Analytics Consumer]


## 🛠 Prerequisites

- **Docker:** Install Docker from [here](https://www.docker.com/get-started).
- **Docker Compose:** Comes bundled with Docker Desktop for Windows and Mac. For Linux, install it separately [here](https://docs.docker.com/compose/install/).
- **Node.js & npm:** Install from [here](https://nodejs.org/).

## 🔧 Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/kafka-event-processing.git
cd kafka-event-processing

