package com.example.hello.hellomovie.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Hello on 2016/12/14.
 */

public class MovieImageView extends ImageView {
    public MovieImageView(Context context) {
        super(context);
    }

    public MovieImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MovieImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int refactWidth = widthMeasureSpec;
        int refactHeight = widthMeasureSpec * 3 / 2;
        setMeasuredDimension(refactWidth, refactHeight);
    }
}
