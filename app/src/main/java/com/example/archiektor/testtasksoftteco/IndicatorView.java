package com.example.archiektor.testtasksoftteco;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Archiektor on 26.06.2017.
 */

public class IndicatorView extends View {

    private Paint paint;
    //private Paint paintBlur;
    private int iColor;

    public IndicatorView(Context context, int mColor) {
        super(context);
        this.iColor = mColor;
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(iColor);
        paint.setStrokeWidth(20f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        /**paintBlur = new Paint();
         paintBlur.set(paint);
         paintBlur.setColor(iColor);
         paintBlur.setStrokeWidth(30f);
         paintBlur.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));*/
    }

    @Override
    protected void onDraw(Canvas canvas) {

        final int width = getWidth() - getPaddingLeft() - getPaddingRight();
        final int height = getHeight() - getPaddingTop() - getPaddingBottom();

        final int cx = width / 2;
        final int cy = height / 2;

        final float diameter = Math.min(width, height) - paint.getStrokeWidth();
        final float radius = diameter / 2;

        canvas.drawCircle(cx, cy, radius, paint);
        //canvas.drawCircle(cx, cy, radius, paintBlur);
    }


    public void setColor(int iColor) {
        this.iColor = iColor;
        paint.setColor(iColor);
        invalidate();
    }
}

