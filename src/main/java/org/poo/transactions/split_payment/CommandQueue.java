package org.poo.transactions.split_payment;

import java.util.LinkedList;
import java.util.Queue;

public class CommandQueue {
    private Queue<Command> queue = new LinkedList<>();

    public void addCommand(Command command) {
        queue.add(command);
    }

    public void processCommands() {
        while (!queue.isEmpty()) {
            Command command = queue.poll();
            command.execute();
        }
    }
}
