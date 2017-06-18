package com.example.archiektor.testtasksoftteco;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;

/**
 * Created by Archiektor on 16.06.2017.
 */

public class GifView extends View {

    private InputStream mInputStream;
    private Movie mMovie;
    private int mWidth, mHeight;
    private long movieStart;
    private long movieDuration;
    private Context mContext;

    public GifView(Context context) {
        super(context);
        init(context);
    }

    public GifView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public GifView(Context context, AttributeSet attributeSet, int defStylleAttr) {
        super(context, attributeSet, defStylleAttr);
        init(context);
    }

    private void init(Context context) {
        setFocusable(true);
        mInputStream = context.getResources().openRawResource(+R.drawable.giphy);

        mMovie = Movie.decodeStream(mInputStream);
        mHeight = mMovie.height();
        mWidth = mMovie.width();
        movieDuration = mMovie.duration();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mWidth, mHeight);
    }

    public int getMovieWidth() {
        return mWidth;
    }

    public int getMovieHeight() {
        return mHeight;
    }

    public long getMovieDuration() {
        return movieDuration;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        long now = SystemClock.uptimeMillis();

        if (movieStart == 0) {
            movieStart = now;
        }

        if (mMovie != null) {
            int dur = mMovie.duration();
            if (dur == 0) {
                dur = 1000;
            }

            int relTime = (int) ((now - movieStart) % dur);

            mMovie.setTime(relTime);
            mMovie.draw(canvas, 0, 0);
            invalidate();
        }
    }

}
