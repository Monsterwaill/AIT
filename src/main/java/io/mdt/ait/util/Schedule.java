package io.mdt.ait.util;

import java.util.*;
import java.util.function.Consumer;

public class Schedule {

    private final String name;
    private final Runnable runnable;

    private Consumer<Integer> onSecond;

    private final long seconds;
    private int repeat = 0;

    private Schedule after;

    public Schedule(Runnable runnable, long seconds) {
        this(UUID.randomUUID().toString(), runnable, seconds);
    }

    public Schedule(String name, Runnable runnable, long seconds) {
        this.name = name;
        this.runnable = runnable;

        this.seconds = seconds;
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

    public Schedule onSecond(Consumer<Integer> onSecond) {
        this.onSecond = onSecond;
        return this;
    }

    public void schedule() {
        Timer timer = new Timer(this.name);
        for (int i = 0; i < this.repeat; i++) {
            for (int seconds = 1; seconds < this.seconds + 1; seconds++) {
                int second = seconds;

                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                onSecond.accept(second);
                            }
                        },
                        1000L);
            }

            this.runnable.run();
            this.after.schedule();
        }
    }
}
