// producer.js
const { Kafka } = require('kafkajs');

// Initialize Kafka
const kafka = new Kafka({
  clientId: 'event-producer',
  brokers: ['localhost:9092'],
});

// Create a producer
const producer = kafka.producer();

// Sample events
const events = [
  { userId: 1, action: 'signup', timestamp: Date.now() },
  { userId: 2, action: 'login', timestamp: Date.now() },
  { userId: 1, action: 'upload_photo', timestamp: Date.now() },
  // Add more events as needed
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
