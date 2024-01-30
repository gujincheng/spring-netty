package com.digiwin.ltgx.utils;

import org.apache.commons.lang3.time.StopWatch;

public class LogUtil {
    public static long splitTime(StopWatch stopWatch) {
        if (stopWatch.isStarted()) {
            stopWatch.split();
            return stopWatch.getSplitTime();
        } else {
            return 0;
        }
    }
}
