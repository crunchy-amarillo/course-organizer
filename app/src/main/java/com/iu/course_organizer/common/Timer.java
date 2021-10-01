package com.iu.course_organizer.common;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Timer {
    private static Map<Integer, Timer> instances = new HashMap<>();

    private LocalDateTime start;

    public static Timer getInstance(int index) {
        if (null == instances.get(index)) {
            instances.put(index, new Timer());
        }
        return instances.get(index);
    }

    private Timer() {
    }

    public void start() {
        start = LocalDateTime.now();
    }

    public long getSeconds() {
        Duration dur = Duration.between(start, LocalDateTime.now());
        long millis = dur.toMillis();
        return (0 < millis) ? Math.round((float) millis / 1000) : 0;
    }

    public int stop() {
        Duration dur = Duration.between(start, LocalDateTime.now());
        int minutes = (int) dur.toMinutes();
        start = null;
        return minutes;
    }
}
