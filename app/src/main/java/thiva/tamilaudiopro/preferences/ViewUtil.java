package thiva.tamilaudiopro.preferences;


import android.content.res.Resources;

import android.util.DisplayMetrics;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class ViewUtil {

  public static float convertDpToPixel(float dp, Resources resources) {
    DisplayMetrics metrics = resources.getDisplayMetrics();
    return dp * metrics.density;
  }
}