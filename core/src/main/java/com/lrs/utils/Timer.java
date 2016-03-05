package com.lrs.utils;

/**
 * Created by fcambarieri on 03/03/16.
 */
public final class Timer {

    private long start;
    private long end = -1;

    public Timer() {
        start();
    }

    public void start() {
        this.start = System.currentTimeMillis();
    }

    public void endit() {
        this.end = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        if (end < 0) {
            endit();
        }
        return end - start;
    }

    public void restart() {
        end = -1;
        start();
    }

}
