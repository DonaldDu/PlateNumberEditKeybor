package com.dhy.platenumber;

import android.content.Context;
import android.util.AttributeSet;

public class SquareEditText extends android.support.v7.widget.AppCompatEditText {
    public SquareEditText(Context context) {
        super(context);
    }

    public SquareEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        // Children are just made to fill our space.
        int childWidthSize = getMeasuredWidth();
        //高度和宽度一样
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
