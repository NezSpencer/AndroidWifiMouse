package com.nuhiara.nezspencer.androidwifimouse.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorEvent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.nuhiara.nezspencer.androidwifimouse.view.interfaces.MouseMovementListener;

import androidx.annotation.NonNull;

public class AnimatedView extends View {
    private static final int CIRCLE_RADIUS = 25; //pixels
    private Paint mPaint;
    private int x;
    private int y;
    private int viewWidth;
    private int viewHeight;
    private int lastX = 0;
    private int lastY = 0;
    private long lastUpdateTime = 0;
    private MouseMovementListener listener;

    public AnimatedView(Context context) {
        super(context);
    }

    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AnimatedView(Context context, @NonNull MouseMovementListener listener) {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        this.listener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    public void onSensorEvent(SensorEvent event) {
        x -= (int) event.values[0];
        y += (int) event.values[1];
        //Make sure we do not draw outside the bounds of the view.
        //So the max values we can draw to are the bounds + the size of the circle
        if (x <= CIRCLE_RADIUS) {
            x = CIRCLE_RADIUS;
        }
        if (x >= viewWidth - CIRCLE_RADIUS) {
            x = viewWidth - CIRCLE_RADIUS;
        }
        if (y <= CIRCLE_RADIUS) {
            y = CIRCLE_RADIUS;
        }
        if (y >= viewHeight - CIRCLE_RADIUS) {
            y = viewHeight - CIRCLE_RADIUS;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        synchronized (this) {

            canvas.drawCircle(x, y, CIRCLE_RADIUS, mPaint);
            //We need to call invalidate each time, so that the view continuously draws
            invalidate();
            Log.e("LOGGER", "x=" + x + " y=" + y);
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastUpdateTime > 500) {
                lastUpdateTime = currentTime;
                if (Math.abs(lastX - x) > 20 || Math.abs(lastY - y) > 20) {
                    lastX = x;
                    lastY = y;
                    listener.onMouseMoved(x, y);
                }

            }
        }


    }
}
