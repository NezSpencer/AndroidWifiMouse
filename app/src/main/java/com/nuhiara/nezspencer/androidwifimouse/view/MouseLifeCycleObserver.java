package com.nuhiara.nezspencer.androidwifimouse.view;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class MouseLifeCycleObserver implements LifecycleObserver {
    private Sensor accelerometer;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;

    MouseLifeCycleObserver(@NonNull SensorEventListener sensorEventListener,
                           @NonNull Sensor accelerometer,
                           @NonNull SensorManager sensorManager) {
        super();
        this.sensorManager = sensorManager;
        this.accelerometer = accelerometer;
        this.sensorEventListener = sensorEventListener;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void registerSensor() {
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void unRegisterSensor() {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
