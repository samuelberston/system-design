// producer.js
const { Kafka } = require('kafkajs');

// Initialize Kafka
const kafka = new Kafka({
  clientId: 'event-producer',
  brokers: [
    'kafka-nlb.your-domain.com:9092'  // Your NLB DNS name
  ],
  // SSL configuration if enabled
  ssl: true,
  sasl: {
    mechanism: 'plain',
    username: process.env.KAFKA_USERNAME,
    password: process.env.KAFKA_PASSWORD
  }
});

// Create a producer
const producer = kafka.producer();

// Sample events
const events = [
  { userId: 1, action: 'signup', timestamp: Date.now() },
  { userId: 2, action: 'login', timestamp: Date.now() },
  { userId: 1, action: 'upload_photo', timestamp: Date.now() },
  { userId: 3, action: 'signup', timestamp: Date.now() },
  { userId: 4, action: 'login', timestamp: Date.now() },
  { userId: 3, action: 'upload_photo', timestamp: Date.now() },
];

// Function to send events
const sendEvents = async () => {
  await producer.connect();
  console.log('Producer connected');

  for (const event of events) {
    await producer.send({
      topic: 'events',
      messages: [
        { value: JSON.stringify(event) },
      ],
    });
    console.log(`Sent event: ${JSON.stringify(event)}`);
    await new Promise(res => setTimeout(res, 1000)); // Simulate delay
  }

  await producer.disconnect();
  console.log('Producer disconnected');
};

sendEvents().catch(console.error);
