package io.mdt.ait.util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ScheduleTask extends TimerTask {

    private final String name;
    private final Runnable runnable;

    public ScheduleTask(Runnable runnable) {
        this(UUID.randomUUID().toString(), runnable);
    }

    public ScheduleTask(String name, Runnable runnable) {
        this.name = name;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        this.runnable.run();
    }

    public void schedule(long delay) {
        new Timer(this.name).schedule(this, delay);
    }
}
