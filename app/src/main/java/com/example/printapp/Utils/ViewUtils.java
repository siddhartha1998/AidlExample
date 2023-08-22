package com.example.printapp.Utils;

import android.os.SystemClock;

public class ViewUtils {
    private static long lastClickTime;
    public static boolean isFastClick() {
        return isFastClick(1000);
    }

    public static boolean isFastClick(int millisecond) {
        long time = SystemClock.elapsedRealtime();
        if (time - lastClickTime < millisecond) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
