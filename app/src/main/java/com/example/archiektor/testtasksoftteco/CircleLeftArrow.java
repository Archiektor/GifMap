package com.example.archiektor.testtasksoftteco;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Archiektor on 26.06.2017.
 */

public class CircleLeftArrow extends View {
    private int circleColor = 0xffacff2f;
    private int arrowColor = 0xff1d45bb;
    private int measuredSize;
    private int strokeWidth;

    private Paint mCirclePiant, mArrowPaint;

    public CircleLeftArrow(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CircleLeftArrow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CircleLeftArrow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attributeSet, int defStyle) {
        mCirclePiant = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePiant.setColor(circleColor);
        mCirclePiant.setStyle(Paint.Style.FILL);

        mArrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArrowPaint.setColor(arrowColor);
        mArrowPaint.setStyle(Paint.Style.STROKE);
        mArrowPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int measuredWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);

        measuredSize = Math.min(measuredHeight, measuredWidth);
        strokeWidth = Math.round(measuredSize * 0.05f);
        mCirclePiant.setStrokeWidth(strokeWidth);
        mArrowPaint.setStrokeWidth(strokeWidth);
        // Make a square
        setMeasuredDimension(measuredSize + strokeWidth, measuredSize + strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (measuredSize <= 0) {
            // Not much we can draw, can we
            return;
        }

        float center = (measuredSize + strokeWidth) * 0.5f;
        canvas.drawCircle(center, center, measuredSize * 0.5f, mCirclePiant);

        canvas.drawLine(center - 0.2f * measuredSize, center, center + 0.1f * measuredSize, center - 0.2f * measuredSize, mArrowPaint);
        canvas.drawLine(center - 0.2f * measuredSize, center, center + 0.1f * measuredSize, center + 0.2f * measuredSize, mArrowPaint);
    }

    public void setCustomColor(int color) {
        mCirclePiant.setColor(color);
        invalidate();
    }
}
