package com.nuhiara.nezspencer.androidwifimouse.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.nuhiara.nezspencer.androidwifimouse.R;
import com.nuhiara.nezspencer.androidwifimouse.databinding.LayoutMouseBinding;
import com.nuhiara.nezspencer.androidwifimouse.utility.Button;
import com.nuhiara.nezspencer.androidwifimouse.utility.Constants;
import com.nuhiara.nezspencer.androidwifimouse.viewmodel.MouseActivityViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * Created by nezspencer on 4/22/17.
 */

public class MouseActivity extends AppCompatActivity implements SensorEventListener {

    private AnimatedView mouseBall;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        if (sensorManager != null)
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        LayoutMouseBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_mouse);
        MouseActivityViewModel viewModel = ViewModelProviders.of(this)
                .get(MouseActivityViewModel.class);
        viewModel.getCoordinatesObservable().observe(this, new Observer<Pair<Integer, Integer>>() {
            @Override
            public void onChanged(Pair<Integer, Integer> integerIntegerPair) {
                sendXYcoordinatesToService(integerIntegerPair.first, integerIntegerPair.second);
            }
        });
        mouseBall = new AnimatedView(this, viewModel);

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        screenWidth = point.x; /*metrics.widthPixels - 100;*/
        screenHeight =point.y;/*metrics.heightPixels -leftMouseButton.getHeight() - 200;*/
        MouseLifeCycleObserver lifeCycleObserver = new MouseLifeCycleObserver(this,
                accelerometer, sensorManager);
        getLifecycle().addObserver(lifeCycleObserver);

        binding.ballLayout.addView(mouseBall);

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

    public void sendXYcoordinatesToService(double xCoord, double yCoord){

        int xx = (int) xCoord* Constants.PC_WIDTH/screenWidth;
        int yy = (int) yCoord*Constants.PC_HEIGHT/screenHeight;
        Intent serviceIntent = new Intent(this,SocketService.class);
        serviceIntent.putExtra(Constants.KEY_DATA,new int[]{xx,yy});
        startService(serviceIntent);
    }

    public void sendMouseClickToService(View view) {
        Button buttonClicked = view.getId() == R.id.button_left_mouse ? Button.LEFT :
                Button.RIGHT;
        Intent btnIntent = new Intent(MouseActivity.this, SocketService.class);
        btnIntent.putExtra(Constants.KEY_BUTTON,buttonClicked);
        startService(btnIntent);
    }
}
