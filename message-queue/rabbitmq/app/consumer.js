const amqp = require('amqplib');

async function consumeMessages() {
    try {
        // Connect to RabbitMQ server
        const connection = await amqp.connect('amqp://localhost');
        const channel = await connection.createChannel();

        // Set up exchanges
        const mainExchange = 'main_exchange';
        const dlExchange = 'dlx';
        await channel.assertExchange(mainExchange, 'topic', { durable: true });
        await channel.assertExchange(dlExchange, 'direct', { durable: true });

        // Define queue configurations
        const queues = {
            highPriority: {
                name: 'high_priority_queue',
                pattern: 'task.priority.high',
                options: {
                    durable: true,
                    deadLetterExchange: dlExchange,
                    deadLetterRoutingKey: 'dead_letter',
                    maxPriority: 10,
                    messageTtl: 60000
                }
            },
            normalPriority: {
                name: 'normal_priority_queue',
                pattern: 'task.priority.normal',
                options: { durable: true, maxPriority: 5 }
            },
            notifications: {
                name: 'notifications_queue',
                pattern: 'notification.#',
                options: { durable: true }
            },
            logs: {
                name: 'logs_queue',
                pattern: 'system.log',
                options: { durable: true }
            }
        };

        // Set up dead letter queue
        await channel.assertQueue('dead_letter_queue', { durable: true });
        await channel.bindQueue('dead_letter_queue', dlExchange, 'dead_letter');

        // Set up queues and bindings
        for (const [key, queue] of Object.entries(queues)) {
            await channel.assertQueue(queue.name, queue.options);
            await channel.bindQueue(queue.name, mainExchange, queue.pattern);
        }

        console.log('[*] Waiting for messages. To exit press CTRL+C');

        // Enhanced message processing with retry mechanism
        const processMessage = async (message, queueName) => {
            const retryCount = (message.properties.headers?.['x-retry-count'] || 0);
            
            try {
                const content = JSON.parse(message.content.toString());
                console.log(`[${queueName}] Received:`, {
                    content,
                    properties: message.properties,
                    retryCount
                });

                // Add artificial processing delay based on priority
                const delay = message.properties.priority === 10 ? 500 : 1000;
                await new Promise(resolve => setTimeout(resolve, delay));

                // Process based on message type
                switch (content.type) {
                    case 'task':
                        await processTask(content);
                        break;
                    case 'notification':
                        await processNotification(content);
                        break;
                    case 'log':
                        await processLog(content);
                        break;
                    default:
                        throw new Error(`Unknown message type: ${content.type}`);
                }

                channel.ack(message);
            } catch (error) {
                console.error(`Processing error (attempt ${retryCount + 1}):`, error);
                
                if (retryCount < 3) {
                    // Reject the message and send to dead letter queue
                    channel.reject(message, false);
                } else {
                    // Reject the message and send to dead letter queue
                    channel.reject(message, true);
                }
            }
        };

        // Set up consumers for each queue
        for (const [key, queue] of Object.entries(queues)) {
            channel.consume(queue.name, (message) => {
                if (message) {
                    processMessage(message, key);
                }
            });
        }

        // Graceful shutdown
        process.on('SIGINT', async () => {
            try {
                await channel.close();
                await connection.close();
            } finally {
                process.exit(0);
            }
        });

    } catch (error) {
        console.error('Connection error:', error);
        process.exit(1);
    }
}

// Simulate processing functions
async function processTask(content) {
console.log('Processing task:', content);
await new Promise(resolve => setTimeout(resolve, 1000));
}

async function processNotification(content) {
console.log('Processing notification:', content);
await new Promise(resolve => setTimeout(resolve, 500));
}

async function processLog(content) {
console.log('Processing log:', content);
await new Promise(resolve => setTimeout(resolve, 1000));
}

consumeMessages();