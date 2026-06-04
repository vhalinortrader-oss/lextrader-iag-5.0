
import { EventEmitter } from 'events';

export const MessagePriority = { LOW: 0, NORMAL: 1, HIGH: 2, CRITICAL: 3 };

export class NeuralBus extends EventEmitter {
    constructor() {
        super();
        this.registry = new Map();
    }

    static getInstance() {
        if (!NeuralBus.instance) NeuralBus.instance = new NeuralBus();
        return NeuralBus.instance;
    }

    broadcast(event, payload, priority = MessagePriority.NORMAL) {
        this.emit(event, { payload, priority, timestamp: Date.now() });
    }
}

export default NeuralBus;
