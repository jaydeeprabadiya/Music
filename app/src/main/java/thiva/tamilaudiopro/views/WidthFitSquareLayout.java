package thiva.tamilaudiopro.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class WidthFitSquareLayout extends FrameLayout {
    private boolean forceSquare = true;

    public WidthFitSquareLayout(Context context) {
        super(context);
    }

    public WidthFitSquareLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public WidthFitSquareLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @TargetApi(21)
    public WidthFitSquareLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void forceSquare(boolean z) {
        this.forceSquare = z;
        requestLayout();
    }

    @Override
    protected void onMeasure(int i, int i2) {
        if (this.forceSquare) {
            i2 = i;
        }
        super.onMeasure(i, i2);
    }
}
