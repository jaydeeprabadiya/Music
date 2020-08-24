package thiva.tamilaudiopro.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import thiva.tamilaudiopro.Activity.PlayerService;
import thiva.tamilaudiopro.SharedPre.Setting;
import thiva.tamilaudiopro.Utils.Control;

import static thiva.tamilaudiopro.Activity.PlayerService.ACTION_STOP;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e("aaa","onreceive");
        if (Control.timer) {
            Log.e("aaa","timer");
            Log.e("aaa","Audio stop");
            Control.timer = false;
            Control.playcheck = true;
            if (Setting.isPlayed) {
                intent = new Intent(context, PlayerService.class);
                intent.setAction(ACTION_STOP);
                context.startService(intent);
                Toast.makeText(context, "Time End Stop Audio", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "Time End Stop Audio", Toast.LENGTH_SHORT).show();
            }
        }
    }
}