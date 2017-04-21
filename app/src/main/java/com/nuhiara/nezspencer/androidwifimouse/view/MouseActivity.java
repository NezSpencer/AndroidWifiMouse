package com.nuhiara.nezspencer.androidwifimouse.view;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

    private float buttonX1,buttonX2,buttonY1, buttonY2;

    private int screenWidth;
    private int screenHeight;

    private static final int MOVE_AMOUNT=10;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mouse_ball);
        ButterKnife.bind(this);
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);

        screenWidth = getResources().getDisplayMetrics().widthPixels - 50;
        screenHeight = getResources().getDisplayMetrics().heightPixels - 50 - leftMouseButton.getHeight();
        buttonX1 = mouseTrackBall.getX();
        buttonX2 = buttonX1 + mouseTrackBall.getWidth();
        buttonY1 = mouseTrackBall.getY();
        buttonY2 = buttonY1 + mouseTrackBall.getHeight();

        leftMouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMouseClickToService(Constants.KEY_LEFT);
            }
        });

        rightMouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMouseClickToService(Constants.KEY_RIGHT);
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

            double mouseX;
            double mouseY;
            if ((currentTime - lastUpdateTime)> 100)
            {
                /*long diffTime = currentTime -lastUpdateTime;
                lastUpdateTime=currentTime;

                float speedOfMovement = (x + y + z - lastX - lastY - lastZ)/diffTime * 10000;

                if (speedOfMovement > SHAKE_THRESHOLD){


                }*/

                //ToDo implement method to transfer coordinates to service
                if (Math.abs(x) > Math.abs(y))
                {
                    //motion on x axis

                    if (x > 0)
                    {
                        //positive motion
                        buttonX2 = mouseTrackBall.getX() + mouseTrackBall.getWidth();
                        //xPos +=10;
                        if ((buttonX2 + MOVE_AMOUNT) < (screenWidth-mouseTrackBall.getWidth()) &&
                                (buttonX2 + MOVE_AMOUNT) > 60)
                        {
                            mouseTrackBall.animate().translationXBy(MOVE_AMOUNT);
                            mouseX = buttonX2 + MOVE_AMOUNT;
                            mouseY = buttonY2;

                            sendXYcoordinatesToService(mouseX,mouseY);

                        }


                    }
                    else {
                        //negative motion
                        buttonX1 = mouseTrackBall.getX();
                        //xPos -=10;
                        if ((buttonX1 -MOVE_AMOUNT) <(screenWidth-mouseTrackBall.getWidth()) &&
                                (buttonX1 -MOVE_AMOUNT) > 60)
                        {
                            mouseTrackBall.animate().translationXBy(-MOVE_AMOUNT);
                            mouseX = buttonX1 - MOVE_AMOUNT;
                            mouseY = buttonY2;

                            sendXYcoordinatesToService(mouseX,mouseY);
                        }

                    }

                }
                else {
                    // motion on y axis

                    if (y > 0)
                    {
                        //positive motion
                        buttonY2 = mouseTrackBall.getY() + mouseTrackBall.getHeight();
                        //yPos +=10;
                        if ((buttonY2 + MOVE_AMOUNT) <screenHeight && (buttonY2 + MOVE_AMOUNT) > 60)
                            mouseTrackBall.animate().translationYBy(MOVE_AMOUNT);

                    }
                    else {
                        //negative motion
                        buttonY1=mouseTrackBall.getY();
                        //yPos -=10;
                        if ((buttonY1 - MOVE_AMOUNT) > 2 && (buttonY1 - MOVE_AMOUNT) < screenHeight)
                            mouseTrackBall.animate().translationYBy(-MOVE_AMOUNT);
                    }
                }


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
        Intent btnIntent = new Intent(MouseActivity.this,SocketService.class);
        btnIntent.putExtra(Constants.KEY_BUTTON,buttonClicked);
        startService(btnIntent);
    }
}
