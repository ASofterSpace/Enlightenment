package com.asofterspace.enlightenment;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the backend thread that actually connects to the hardware
 */

public class BackendThread implements Runnable {

    private List<BackendTask> todoQueue;

    public BackendThread() {
        todoQueue = new ArrayList<>();
    }

    public void performTask(BackendTask todo) {
        synchronized (todoQueue) {
            todoQueue.add(todo);
        }
    }

    @Override
    public void run() {

        while (true) {

            BackendTask todo = null;

            synchronized (todoQueue) {
                if (todoQueue.size() > 0) {
                    todo = todoQueue.get(0);
                    todoQueue.remove(0);
                }
            }

            if (todo != null) {
                todo.execute();
            }

            try {
                Thread.sleep(100);
            } catch (Exception e) {
                // Exceptions? Who needs exceptions?
            }
        }
    }
}
