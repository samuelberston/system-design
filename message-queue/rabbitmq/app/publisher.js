const amqp = require('amqplib');

async function publishMessage() {
    try {
        const connection = await amqp.connect('amqp://localhost');
        const channel = await connection.createChannel();

        // Declare exchange
        const exchange = 'main_exchange';
        await channel.assertExchange(exchange, 'topic', { durable: true });

        // Define message types and routing keys
        const messageTypes = {
            'high_priority': 'task.priority.high',
            'normal_priority': 'task.priority.normal',
            'notification': 'notification.#',
            'log': 'system.log'
        };

        // Generate sample messages
        const sampleMessages = [
            {
                type: 'task',
                priority: 'high',
                data: { id: 1, action: 'process_payment', amount: 100 },
                routingKey: messageTypes.high_priority
            },
            {
                type: 'task',
                priority: 'normal',
                data: { id: 2, action: 'send_email', to: 'user@example.com' },
                routingKey: messageTypes.normal_priority
            },
            {
                type: 'notification',
                data: { userId: 123, message: 'Your order is ready' },
                routingKey: 'notification.user.order'
            },
            {
                type: 'log',
                data: { level: 'info', message: 'System check completed' },
                routingKey: messageTypes.log
            }
        ];

        // Publish messages with different properties
        for (const msg of sampleMessages) {
            channel.publish(exchange, msg.routingKey, Buffer.from(JSON.stringify(msg)), {
                persistent: true,
                priority: msg.priority === 'high' ? 10 : 5,
                timestamp: Date.now(),
                headers: {
                    'message-type': msg.type,
                    'source': 'demo-publisher'
                }
            });
            console.log(`[x] Sent ${msg.type} message with routing key: ${msg.routingKey}`);
        }

        // Close the connection after sending all messages
        setTimeout(() => {
            connection.close();
            process.exit(0);
        }, 1000);

    } catch (error) {
        console.error('Error:', error);
        process.exit(1);
    }
}

publishMessage();