package com.tman.fallout;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager =  null;
    private Sensor mAccelerometer =  null;
    //private final Sensor mGravity;
    static float x,y,z;
    static double acceleration;
    final double THRESHOLD_LOW = 4.22;          //min acceleration below which fall is detected
    final double THRESHOLD_HIGH = 39.12;        //max acc above which fall is detected
    /*values cited from
    Raymond Y. W. Lee, Alison J. Carlisle, Detection of falls using accelerometers and mobile phone technology,
     Age and Ageing, Volume 40, Issue 6, November 2011, Pages 690–696, https://doi.org/10.1093/ageing/afr050q*/
    String prevmsg;
    TextView Values,xview,yview,zview;
    static boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        Values = findViewById(R.id.values);
        xview = findViewById(R.id.xview);
        yview = findViewById(R.id.yview);
        zview = findViewById(R.id.zview);

        if(mAccelerometer == null){
            Toast.makeText(getApplicationContext(),"No sensor found",Toast.LENGTH_SHORT).show();
        }
    }


    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        if(prevmsg!=null){
            Values.setText(prevmsg);
        }
    }

    protected void onPause() {
        super.onPause();
        prevmsg="not under fall";
        Values.setText(prevmsg);
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            acceleration = Math.sqrt(x*x + y*y + z*z);

            if(acceleration > THRESHOLD_HIGH){
                if(!flag) {
                    Toast.makeText(getApplicationContext(), "under fall", Toast.LENGTH_SHORT).show();
                    flag = true;
                }
                //prevmsg = "under fall";
                //Values.setText(prevmsg);
                //send notification when device detcts fall
                //reset the state soon after notification is send.
            }
            else{
                prevmsg = "not under fall";
                Values.setText(prevmsg);
            }

            xview.setText(String.valueOf(x));
            yview.setText(String.valueOf(y));
            zview.setText(String.valueOf(z));
        }
    }
}