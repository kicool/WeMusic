
package net.kicool.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import net.kicool.config.ConfigApplication;

import java.io.File;

public class LocalPathResolver {
    private static final String TAG = LocalPathResolver.class.getSimpleName();


    private static final String PREFIX_DIR = ConfigApplication.INSTANCE.PREFIX_DIR();

    private static final String BASE_DIR = "/kicool";

    private static String base;

    public static void init(Context context) {
        if (ConfigApplication.INSTANCE.DEBUG()) {
            base = Environment.getExternalStorageDirectory().getPath();
        } else {
            if (true) {
                base = Environment.getExternalStorageDirectory().getPath();
            } else {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    base = context.getExternalCacheDir().getPath();
                } else {
                    File filesDir = context.getApplicationContext().getFilesDir();
                    base = filesDir.getPath();
                }
            }
        }

        base = base + PREFIX_DIR;

        LoggerUtil.i(TAG, "base dir = " + base);
    }

    public static String getBaseDir() {
        return base + BASE_DIR;
    }

    public static String getCachePath(String subpath) {
        return base + BASE_DIR + File.separator + subpath;
    }

    /**
     * Get the Android log dir.
     *
     * @return
     */
    public static String getLogDir() {
        return Environment.getExternalStorageDirectory().getPath() + PREFIX_DIR + File.separator
                + "log";
    }

    /**
     * Get the application internal data/data path like
     * this:/data/data/com.ef.efekta12/
     *
     * @param context
     * @return
     */
    public static String getInternalDataDataPath(Context context) {
        PackageManager m = context.getPackageManager();
        String s = context.getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            s = p.applicationInfo.dataDir;
        } catch (NameNotFoundException e) {
            LoggerUtil.e(TAG, "Error Package name not found ", e);
            return null;
        }

        return s;
    }
}
