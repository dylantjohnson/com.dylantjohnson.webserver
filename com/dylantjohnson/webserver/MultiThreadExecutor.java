package com.dylantjohnson.webserver;

import java.lang.Runnable;
import java.lang.Thread;
import java.util.concurrent.Executor;

/**
 * The {@link Executor} used by this webserver. It will simply create a new
 * thread for every request.
 */
class MultiThreadExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
