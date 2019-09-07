package com.dylantjohnson.webserver;

import java.lang.Runnable;
import java.lang.Thread;
import java.util.concurrent.Executor;

class MultiThreadExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
