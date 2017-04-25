package com.nuhiara.nezspencer.androidwifimouse.view;

import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nuhiara.nezspencer.androidwifimouse.R;
import com.nuhiara.nezspencer.androidwifimouse.utility.Constants;
import com.nuhiara.nezspencer.androidwifimouse.view.interfaces.MouseActivityContract;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nezspencer on 3/31/17.
 */

public class MouseActivity extends AppCompatActivity implements SensorEventListener, MouseActivityContract{

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private static String TAG="mouseAct";

    private float lastX, lastY, lastZ;
    private long lastUpdateTime =0;
    private static final int SHAKE_THRESHOLD=800;
    @Bind(R.id.track_ball) Button mouseTrackBall;
    @Bind(R.id.button_left_mouse)Button leftMouseButton;
    @Bind(R.id.button_right_mouse)Button rightMouseButton;
    @Bind(R.id.mouse_layout)RelativeLayout parentLayout;
    @Bind(R.id.linear_button_layout)LinearLayout mouseButtonLayout;

    private float buttonX1,buttonX2,buttonY1, buttonY2;

    private int screenWidth;
    private int screenHeight;
    static float mouseX =0;
    static float mouseY = 0;

    private static final int MOVE_AMOUNT=30;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mouse_ball);
        ButterKnife.bind(this);
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);

        DisplayMetrics metrics = new DisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = point.x; /*metrics.widthPixels - 100;*/
        screenHeight =point.y;/*metrics.heightPixels -leftMouseButton.getHeight() - 200;*/
                /*getResources().getDisplayMetrics().heightPixels - 50 - leftMouseButton.getHeight();*/
        buttonX1 = mouseTrackBall.getX();
        buttonX2 = buttonX1 + mouseTrackBall.getWidth();
        buttonY1 = mouseTrackBall.getY();
        buttonY2 = buttonY1 + mouseTrackBall.getHeight();

        leftMouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("LOGGER"," left mouse click");
                sendMouseClickToService(Constants.KEY_LEFT);
            }
        });

        rightMouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMouseClickToService(Constants.KEY_RIGHT);
                Log.e("LOGGER"," right mouse click");
            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType()== Sensor.TYPE_ACCELEROMETER){

            //for accelerometer events
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            Log.e(TAG," x= "+x+" y= "+y);
            long currentTime= System.currentTimeMillis();


            if ((currentTime - lastUpdateTime)> 100)
            {
                lastUpdateTime = currentTime;
                /*long diffTime = currentTime -lastUpdateTime;
                lastUpdateTime=currentTime;

                float speedOfMovement = (x + y + z - lastX - lastY - lastZ)/diffTime * 10000;

                if (speedOfMovement > SHAKE_THRESHOLD){


                }*/
                x = x*3;
                y = y *3;
                if (x > 0f)
                {
                    buttonX1 = mouseTrackBall.getX();
                    buttonX2 = mouseTrackBall.getX() + mouseTrackBall.getWidth();
                    Log.e("LOGGER","x1="+buttonX1+" x2="+buttonX2);

                    if ((buttonX2 + MOVE_AMOUNT) < screenWidth)
                    {
                        mouseTrackBall.animate().xBy(MOVE_AMOUNT);
                        buttonX1 = mouseTrackBall.getX();
                        buttonY1 = mouseTrackBall.getY();

                        Log.e("LOGGER","x is increasing");
                        /*sendXYcoordinatesToService(buttonX1,buttonY1);*/
                    }
                }
                else {
                    // x < 0

                    buttonX1 = mouseTrackBall.getX();
                    buttonX2 =buttonX1 + mouseTrackBall.getWidth();

                    if ((buttonX1 - MOVE_AMOUNT) > 10)
                    {
                        mouseTrackBall.animate().xBy(-MOVE_AMOUNT);
                        buttonX1 = mouseTrackBall.getX();
                        buttonY1 = mouseTrackBall.getY();

                        Log.e("LOGGER","x is decreasing");

                        /*sendXYcoordinatesToService(buttonX1,buttonY1);*/
                    }
                }

                /*if (x > 0 && y > 0)
                {
                    if (Math.abs(x) > Math.abs(y))
                    {
                        mouseX = screenWidth/20;
                        mouseY = screenHeight/40;
                        mouseTrackBall.animate().translationXBy(mouseX).translationYBy(mouseY);

                        sendXYcoordinatesToService(mouseX,mouseY);
                    }
                    else {
                        mouseX = screenWidth/30;
                        mouseY = screenHeight/20;
                        mouseTrackBall.animate().translationXBy(mouseX).translationYBy(mouseY);
                        sendXYcoordinatesToService(mouseX,mouseY);
                    }
                }
                else if (x >0 && y < 0)
                {
                    if (Math.abs(x) > Math.abs(y))
                    {
                        mouseX = screenWidth/20;
                        mouseY = -screenHeight/40;
                        mouseTrackBall.animate().translationXBy(mouseX).translationYBy(mouseY);
                        sendXYcoordinatesToService(mouseX,mouseY);
                    }
                    else {
                        mouseX = screenWidth/30;
                        mouseY = -screenHeight/20;
                        mouseTrackBall.animate().translationXBy(mouseX).translationYBy(mouseY);
                        sendXYcoordinatesToService(mouseX,mouseY);
                    }
                }
                else if (x < 0 && y > 0){

                    if (Math.abs(x) > Math.abs(y))
                    {
                        mouseX = -screenWidth/20;
                        mouseY = screenHeight/40;
                        mouseTrackBall.animate().translationXBy(mouseX).translationYBy(mouseY);
                        sendXYcoordinatesToService(mouseX,mouseY);
                    }
                    else {
                        mouseX = -screenWidth/30;
                        mouseY = screenHeight/20;
                        mouseTrackBall.animate().translationXBy(mouseX).translationYBy(mouseY);
                        sendXYcoordinatesToService(mouseX,mouseY);
                    }
                }
                else {
                    if (Math.abs(x) > Math.abs(y))
                    {
                        mouseX = -screenWidth/20;
                        mouseY = -screenHeight/40;
                        mouseTrackBall.animate().translationXBy(mouseX).translationYBy(mouseY);
                        sendXYcoordinatesToService(mouseX,mouseY);
                    }
                    else {
                        mouseX = -screenWidth/30;
                        mouseY = -screenHeight/20;
                        mouseTrackBall.animate().translationXBy(mouseX).translationYBy(mouseY);
                        sendXYcoordinatesToService(mouseX,mouseY);
                    }
                }*/


                //ToDo implement method to transfer coordinates to service
                /*if (Math.abs(x) > Math.abs(y))
                {
                    //motion on x axis

                    if (x > 0)
                    {
                        //positive motion
                        buttonX1 = mouseTrackBall.getX();
                        buttonX2 = mouseTrackBall.getWidth();
                        //xPos +=10;
                        if ((buttonX2 + MOVE_AMOUNT) < (screenWidth-buttonX2) &&
                                (buttonX1 - MOVE_AMOUNT) > 5)
                        {
                            mouseTrackBall.animate().translationXBy(MOVE_AMOUNT);
                            mouseX = buttonX1 + MOVE_AMOUNT;
                            mouseY = buttonY1;

                            sendXYcoordinatesToService(mouseX,mouseY);

                        }


                    }
                    else {
                        //negative motion
                        buttonX1 = mouseTrackBall.getX();
                        buttonX2 = mouseTrackBall.getWidth();
                        //xPos -=10;
                        if ((buttonX1 -MOVE_AMOUNT) >(5) )
                        {
                            mouseTrackBall.animate().translationXBy(-MOVE_AMOUNT);
                            mouseX = buttonX1 - MOVE_AMOUNT;
                            mouseY = buttonY1;

                            sendXYcoordinatesToService(mouseX,mouseY);
                        }

                    }

                }
                else {
                    // motion on y axis

                    if (y > 0)
                    {
                        //positive motion
                        buttonY1  = mouseTrackBall.getY();
                        buttonY2 = mouseTrackBall.getY() + mouseTrackBall.getHeight();
                        //yPos +=10;
                        if ((buttonY2 + MOVE_AMOUNT) <screenHeight )
                        {
                            mouseTrackBall.animate().translationYBy(MOVE_AMOUNT);
                            mouseY = mouseTrackBall.getY();
                            mouseX = mouseTrackBall.getX();

                            sendXYcoordinatesToService(mouseX,mouseY);
                        }


                    }
                    else {
                        //negative motion
                        buttonY1=mouseTrackBall.getY();
                        buttonY2 = mouseTrackBall.getY() + mouseTrackBall.getHeight();
                        //yPos -=10;
                        if ((buttonY1 - MOVE_AMOUNT) > 5)
                        {
                            mouseTrackBall.animate().translationYBy(-MOVE_AMOUNT);
                            mouseX = mouseTrackBall.getX();
                            mouseY = mouseTrackBall.getY();

                            sendXYcoordinatesToService(mouseX,mouseY);
                        }

                    }
                }*/


                lastX=x;
                lastY=y;
                lastZ=z;
            }


        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void moveBall(int x, int y) {

    }

    public void sendXYcoordinatesToService(double xCoord, double yCoord){

        int xx = (int) xCoord*Constants.PC_WIDTH/screenWidth;
        int yy = (int) yCoord*Constants.PC_HEIGHT/screenHeight;
        Intent serviceIntent = new Intent(this,SocketService.class);
        serviceIntent.putExtra(Constants.KEY_DATA,new int[]{xx,yy});
        Log.e("LOGGER"," sending to service");
        startService(serviceIntent);
    }

    public void sendMouseClickToService(String buttonClicked){
        Log.e("LOGGER"," sending click to service");
        Intent btnIntent = new Intent(MouseActivity.this,SocketService.class);
        btnIntent.putExtra(Constants.KEY_BUTTON,buttonClicked);
        startService(btnIntent);
    }
}
