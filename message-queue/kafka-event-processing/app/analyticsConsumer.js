// analyticsConsumer.js
const { Kafka } = require('kafkajs');

// Initialize Kafka
const kafka = new Kafka({
  clientId: 'analytics-consumer',
  brokers: ['localhost:9092'],
});

// Create a consumer
const consumer = kafka.consumer({ groupId: 'analytics-group' });

// In-memory store for analytics
const userActionCounts = {};

// Function to run the consumer
const run = async () => {
  await consumer.connect();
  console.log('Analytics Consumer connected');

  await consumer.subscribe({ topic: 'events', fromBeginning: true });

  await consumer.run({
    eachMessage: async ({ topic, partition, message }) => {
      const event = JSON.parse(message.value.toString());
      const { userId, action } = event;

      if (!userActionCounts[userId]) {
        userActionCounts[userId] = {};
      }

      if (!userActionCounts[userId][action]) {
        userActionCounts[userId][action] = 0;
      }

      userActionCounts[userId][action] += 1;

      console.log(`[Analytics] User ${userId} performed '${action}' ${userActionCounts[userId][action]} times.`);
    },
  });
};

run().catch(console.error);
