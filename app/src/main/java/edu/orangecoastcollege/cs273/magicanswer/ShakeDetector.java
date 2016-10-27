package edu.orangecoastcollege.cs273.magicanswer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


/**
 * Created by tmorrissey1 on 10/27/2016.
 */

public class ShakeDetector implements SensorEventListener {

    private static final float SHAKE_THRESHOLD = 25f;
    private static final int SHAKE_TIME_LAPSE = 2000;

    private long timeOfLastShake;
    private OnShakeListener shakeListener;

    public ShakeDetector(OnShakeListener listener) {
        shakeListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Get x,y,z values when this event occurs
            float x, y, z;
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            // compare all 3 values against gravity

            float gForceX = x - SensorManager.GRAVITY_EARTH;
            float gForceY = y - SensorManager.GRAVITY_EARTH;
            float gForceZ = z - SensorManager.GRAVITY_EARTH;


            double vector = Math.pow(gForceX, 2.0) + Math.pow(gForceY, 2.0) + Math.pow(gForceZ, 2.0);
            float gForce = (float) Math.sqrt(vector);

            if (gForce > SHAKE_THRESHOLD) {
                long now = System.currentTimeMillis();

                if (now >= timeOfLastShake + SHAKE_TIME_LAPSE) {
                    timeOfLastShake = now;
                    // register a shake event
                    shakeListener.onShake();
                }
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // its the responsibility of the MagicAnswerActivity to implement this method
    public interface OnShakeListener {
        void onShake();
    }
}
