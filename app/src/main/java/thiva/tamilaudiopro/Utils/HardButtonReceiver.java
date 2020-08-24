package thiva.tamilaudiopro.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class HardButtonReceiver extends BroadcastReceiver {

	private final static String TAG = "gauntface";
	private HardButtonListener mButtonListener;

	public HardButtonReceiver(HardButtonListener buttonListener) {
		super();
		mButtonListener = buttonListener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "HardButtonReceiver: Button press received");
		if(mButtonListener != null) {
			abortBroadcast();
			// Pull out the KeyEvent from the intent
			KeyEvent key = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
			// This is just some example logic, you may want to change this for different behaviour
			if(key.getAction() == KeyEvent.ACTION_UP) {
				int keycode = key.getKeyCode();
				// These are examples for detecting key presses on a Nexus One headset
				if(keycode == KeyEvent.KEYCODE_MEDIA_NEXT) {
					mButtonListener.onNextButtonPress();
				} else if(keycode == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
					mButtonListener.onPrevButtonPress();
				} else if(keycode == KeyEvent.KEYCODE_HEADSETHOOK) {
					mButtonListener.onPlayPauseButtonPress();
				}
			}
		}
	}

	public interface HardButtonListener {
		void onPrevButtonPress();
		void onNextButtonPress();
		void onPlayPauseButtonPress();
	}
}
