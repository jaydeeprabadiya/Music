package thiva.tamilaudiopro.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class Control {
    public static Context activity;
    public static AlarmManager alarmManager;
    public static PendingIntent pendingIntent;
    public static boolean record = true, playcheck = true, playPauseRecord = false, timer = false,
            allowPermitionExternalStorage = false, isDestroyed = false;

    public static long convert_long(String s) {
        long ms = 0;
        Pattern p;
        if (s.contains(("\\:"))) {
            p = Pattern.compile("(\\d+):(\\d+)");
        } else {
            p = Pattern.compile("(\\d+).(\\d+)");
        }
        Matcher m = p.matcher(s);
        if (m.matches()) {
            int h = Integer.parseInt(m.group(1));
            int min = Integer.parseInt(m.group(2));
            // int sec = Integer.parseInt(m.group(2));
            ms = (long) h * 60 * 60 * 1000 + min * 60 * 1000;
        }
        return ms;
    }


}



