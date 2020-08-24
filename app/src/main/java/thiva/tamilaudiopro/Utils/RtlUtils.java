package thiva.tamilaudiopro.Utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;

import androidx.core.view.ViewCompat;

import java.util.Locale;

import thiva.tamilaudiopro.Constant.Constant;

public class RtlUtils {
    public static void setScreenDirection(Context context){
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        String language = config.locale.getLanguage();
        if (Constant.ENABLE_RTL){
            if (TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                config.setLayoutDirection(new Locale(language));
            } else {
                config.setLayoutDirection(new Locale("en"));
            }
        }else {
            //use ltr
            config.setLayoutDirection(new Locale("en"));
        }
    }
}
