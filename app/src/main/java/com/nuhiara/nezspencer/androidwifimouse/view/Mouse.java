package com.nuhiara.nezspencer.androidwifimouse.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nuhiara.nezspencer.androidwifimouse.R;
import com.nuhiara.nezspencer.androidwifimouse.utility.Constants;

/**
 * Created by nezspencer on 4/22/17.
 */

public class Mouse extends AppCompatActivity implements SensorEventListener {

    private LinearLayout ballLayout;
    private Button leftMouse;
    private Button rightMouse;
    private AnimatedView mouseBall;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int screenWidth;
    private int screenHeight;

    private long lastUpdateTime =0;
    private static int lastX, lastY =0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        setContentView(R.layout.layout_mouse);
        mouseBall =new AnimatedView(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        screenWidth = point.x; /*metrics.widthPixels - 100;*/
        screenHeight =point.y;/*metrics.heightPixels -leftMouseButton.getHeight() - 200;*/

        ballLayout =(LinearLayout) findViewById(R.id.ball_layout);
        leftMouse = (Button) findViewById(R.id.button_left_mouse);
        rightMouse = (Button) findViewById(R.id.button_right_mouse);

        leftMouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMouseClickToService("left");
            }
        });

        rightMouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMouseClickToService("right");
            }
        });

        ballLayout.addView(mouseBall);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            mouseBall.onSensorEvent(sensorEvent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }



    public class AnimatedView extends View {

        private static final int CIRCLE_RADIUS = 25; //pixels

        private Paint mPaint;
        private int x;
        private int y;
        private int viewWidth;
        private int viewHeight;

        public AnimatedView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setColor(Color.GREEN);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            viewWidth = w;
            viewHeight = h;
        }

        public void onSensorEvent (SensorEvent event) {
            x = x - (int) event.values[0];
            y = y + (int) event.values[1];
            //Make sure we do not draw outside the bounds of the view.
            //So the max values we can draw to are the bounds + the size of the circle
            if (x <= 0 + CIRCLE_RADIUS) {
                x = 0 + CIRCLE_RADIUS;
            }
            if (x >= viewWidth - CIRCLE_RADIUS) {
                x = viewWidth - CIRCLE_RADIUS;
            }
            if (y <= 0 + CIRCLE_RADIUS) {
                y = 0 + CIRCLE_RADIUS;
            }
            if (y >= viewHeight - CIRCLE_RADIUS) {
                y = viewHeight - CIRCLE_RADIUS;
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            synchronized (this){

                canvas.drawCircle(x, y, CIRCLE_RADIUS, mPaint);
                //We need to call invalidate each time, so that the view continuously draws
                invalidate();
                Log.e("LOGGER","x="+x+" y="+y);
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastUpdateTime >500)
                {
                    lastUpdateTime = currentTime;
                    if (Math.abs(lastX - x) >20 || Math.abs(lastY - y) > 20)
                    {
                        lastX = x;
                        lastY = y;
                        sendXYcoordinatesToService(x,y);
                    }

                }
            }


        }
    }

    public void sendXYcoordinatesToService(double xCoord, double yCoord){

        int xx = (int) xCoord* Constants.PC_WIDTH/screenWidth;
        int yy = (int) yCoord*Constants.PC_HEIGHT/screenHeight;
        Intent serviceIntent = new Intent(this,SocketService.class);
        serviceIntent.putExtra(Constants.KEY_DATA,new int[]{xx,yy});
        Log.e("LOGGER"," sending to service");
        startService(serviceIntent);
    }

    public void sendMouseClickToService(String buttonClicked){
        Log.e("LOGGER"," sending click to service");
        Intent btnIntent = new Intent(Mouse.this,SocketService.class);
        btnIntent.putExtra(Constants.KEY_BUTTON,buttonClicked);
        startService(btnIntent);
    }
}
