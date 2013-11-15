package net.kicool.common.utils;

import android.app.Activity;
import android.content.Context;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.ExceptionParser;
import com.google.analytics.tracking.android.ExceptionReporter;
import com.google.analytics.tracking.android.GAServiceManager;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.StandardExceptionParser;
import com.google.analytics.tracking.android.Tracker;

public class GAStatUtil {

    private static Context ctx;

    /*
     * Google Analytics configuration values.
     */

    // Prevent hits from being sent to reports, i.e. during testing.
    private static final boolean GA_IS_DRY_RUN = false;

    /*
     * Method to handle basic Google Analytics initialization. This call will not
     * block as all Google Analytics work occurs off the main thread.
     */
    public static void init(Context context, boolean flag) {
        ctx = context;

        GoogleAnalytics mGa = GoogleAnalytics.getInstance(ctx);
        mGa.setDryRun(GA_IS_DRY_RUN);

        if (flag) {
            setGoogleAnalyticsExceptionHandler(ctx);
        }
    }

    public static void init(Context context) {
        init(context, false);
    }

    public static void onStart(Activity context) {
        EasyTracker.getInstance(ctx).activityStart(context);
    }

    public static void onStop(Activity context) {
        EasyTracker.getInstance(ctx).activityStop(context);
    }

    private static void setGoogleAnalyticsExceptionHandler(Context context) {
        Tracker easyTracker = EasyTracker.getInstance(context);
        if (easyTracker != null) {
            Thread.UncaughtExceptionHandler myHandler = Thread.getDefaultUncaughtExceptionHandler(); // Current default uncaught exception handler.
            ExceptionReporter exceptionReporter = new ExceptionReporter(
                    easyTracker,                                        // Currently used Tracker.
                    GAServiceManager.getInstance(),                   // GAServiceManager singleton.
                    myHandler, context);
            exceptionReporter.setExceptionParser(new MyExceptionParser());
        }
    }

    static class MyExceptionParser implements ExceptionParser {
        @Override
        public String getDescription(String s, Throwable throwable) {
            if (null == throwable) {
                return "";
            }

            return "Uncaught Exception." + new StandardExceptionParser(ctx, null).getDescription(s, throwable);
        }
    }

    static public void sendEvent(String category, String action, String label, Long value) {
        EasyTracker easyTracker = EasyTracker.getInstance(ctx);
        if (easyTracker != null) {
            easyTracker.send(MapBuilder
                    .createEvent(category,      // Event category (required)
                            action,             // Event action (required)
                            label,              // Event label
                            value)              // Event value
                    .build()
            );
        }
    }

    static public void sendEeception(Exception e, boolean fatal) {
        EasyTracker easyTracker = EasyTracker.getInstance(ctx);
        if (easyTracker != null) {
            final StandardExceptionParser parser = new StandardExceptionParser(ctx, null);
            easyTracker.send(MapBuilder
                    .createException(parser.getDescription(Thread.currentThread().getName(), e), fatal)
                    .build());
        }
    }

}
