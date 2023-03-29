package io.mdt.ait.util;

import java.util.*;


public class Schedule extends TimerTask {

    private final String name;
    private final Runnable runnable;

    private final long delay;
    private int repeat = 0;

    private Schedule after;

    public Schedule(Runnable runnable, long delay) {
        this(UUID.randomUUID().toString(), runnable, delay);
    }

    public Schedule(String name, Runnable runnable, long delay) {
        this.name = name;
        this.runnable = runnable;

        this.delay = delay;
    }

    @Override
    public void run() {
        this.runnable.run();
        this.after.schedule();
    }

    public Schedule after(Schedule schedule) {
        if (this.after == null) {
            this.after = schedule;
            return this;
        }

        this.after.after(schedule);
        return this;
    }

    public Schedule repeat(int times) {
        this.repeat = times;
        return this;
    }

    public void schedule() {
        for (int i = 0; i < this.repeat; i++) {
            new Timer(this.name).schedule(this, this.delay);
        }
    }
}
