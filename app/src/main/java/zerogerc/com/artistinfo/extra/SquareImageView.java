package zerogerc.com.artistinfo.extra;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Image view with width and height equal to width from first {@link #onMeasure(int, int)}.
 * So it just measure width and set height equal to it.
 */
public class SquareImageView extends ImageView {
    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
