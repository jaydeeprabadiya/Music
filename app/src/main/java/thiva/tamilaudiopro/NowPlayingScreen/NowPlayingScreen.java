package thiva.tamilaudiopro.NowPlayingScreen;


import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import thiva.tamilaudiopro.Activity.R;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public enum NowPlayingScreen {
    NORMAL(R.string.normal, R.drawable.np_1, 0),
    FLAT(R.string.flat, R.drawable.np_2, 1),
    FULL(R.string.full, R.drawable.np_3, 2),
    PLAIN(R.string.plain, R.drawable.np_4, 3),
    COLOR(R.string.color, R.drawable.np_5, 4),
    BLUR(R.string.blur, R.drawable.np_6, 5),
    CARD(R.string.card, R.drawable.np_7, 6),
    TINY(R.string.tiny, R.drawable.np_8, 7);

    @StringRes
    public final int titleRes;
    @DrawableRes
    public final int drawableResId;
    public final int id;

    NowPlayingScreen(@StringRes int titleRes, @DrawableRes int drawableResId, int id) {
        this.titleRes = titleRes;
        this.drawableResId = drawableResId;
        this.id = id;
    }
}
