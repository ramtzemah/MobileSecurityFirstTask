package com.example.securitiesles1;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.appcompat.app.AppCompatActivity;

public class MovementSensors {

    private SensorManager sensorManager;
    private Sensor accSensor ;
    private AppCompatActivity activity ;
    private SensorEventListener accSensorEventListener;

    public MovementSensors(){}

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public MovementSensors setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        return this;
    }

    public Sensor getAccSensor() {
        return accSensor;
    }

    public MovementSensors setAccSensor(Sensor accSensor) {
        this.accSensor = accSensor;
        return this;
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    public MovementSensors setActivity(AppCompatActivity activity) {
        this.activity = activity;
        return this;
    }

    public void registerListener(SensorEventListener accSensorEventListener, int sensorDelayNormal) {
        this.accSensorEventListener = accSensorEventListener;
        sensorManager.registerListener(accSensorEventListener, accSensor, sensorDelayNormal);
    }

    public void unregisterListener() {
        sensorManager.unregisterListener(accSensorEventListener, accSensor);
    }
}