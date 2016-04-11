package zerogerc.com.artistinfo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ZeRoGerc on 11/04/16.
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
