// loggerConsumer.js
const { Kafka } = require('kafkajs');

// Initialize Kafka
const kafka = new Kafka({
  clientId: 'logger-consumer',
  brokers: ['localhost:9092'],
});

// Create a consumer
const consumer = kafka.consumer({ groupId: 'logger-group' });

// Function to run the consumer
const run = async () => {
  await consumer.connect();
  console.log('Logger Consumer connected');

  await consumer.subscribe({ topic: 'events', fromBeginning: true });

  await consumer.run({
    eachMessage: async ({ topic, partition, message }) => {
      const event = JSON.parse(message.value.toString());
      console.log(`[Logger] Received event: ${JSON.stringify(event)}`);
    },
  });
};

run().catch(console.error);
