class MessageQueue {
    constructor() {
        this.queue = []; // Array to store messages
        this.consumers = []; // Array to store waiting consumers
    }

    // Method for producers to add messages to the queue
    produce(message) {
        if (this.consumers.length > 0) {
            // If there are consumers waiting, resolve the oldest one with the message
            const consumerResolve = this.consumers.shift();
            consumerResolve(message);
        } else {
            // Otherwise, enqueue the message
            this.queue.push(message);
        }
    }

    // Method for consumers to retrieve messages from the queue
    consume() {
        return new Promise((resolve) => {
            if (this.queue.length > 0) {
                // If there are messages in the queue, resolve immediately with the first message
                const message = this.queue.shift();
                resolve(message);
            } else {
                // Otherwise, add the resolver to the consumers array to be called when a message is produced
                this.consumers.push(resolve);
            }
        });
    }
}

function producer(queue, message, delay) {
    setTimeout(() => {
        console.log(`Producing message: ${message}`);
        queue.produce(message);
    }, delay);
}

async function consumer(queue, id) {
    while (true) {
        const message = await queue.consume();
        console.log(`Consumer ${id} consumed message: ${message}`);
        // Simulate processing time
        await new Promise(resolve => setTimeout(resolve, 1000));
    }
}

// Instantiate the message queue
const queue = new MessageQueue();

// Start consumers
consumer(queue, 1);
consumer(queue, 2);

// Start producers
producer(queue, 'Message 1', 1000); // Produce after 1 second
producer(queue, 'Message 2', 2000); // Produce after 2 seconds
producer(queue, 'Message 3', 3000); // Produce after 3 seconds
producer(queue, 'Message 4', 4000); // Produce after 4 seconds
