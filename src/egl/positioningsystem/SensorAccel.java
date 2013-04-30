package egl.positioningsystem;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorAccel implements SensorEventListener{

	private boolean mInitialized;
	public Sensor mAccelerometer;
	//private float mLastX, mLastY, mLastZ;
	//private final float NOISE = (float) 0.0;
	
	private float f_ax,f_ay,f_az ;
	
	public float getAccelX(){
		return f_ax;
	}
	
	public float getAccelY(){
		return f_ay;
	}
	
	public float getAccelZ(){
		return f_az;
	}
	
	public SensorAccel(SensorManager mSensorManager){
		mInitialized = false;
	    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
    
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// can be safely ignored for this demo
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		
		f_ax = event.values[0];
		f_ay = event.values[1];
		f_az = event.values[2];
		
		/*if(!mInitialized){
			mLastX = f_ax;
			mLastY = f_ay;
			mLastZ = f_az;
			mInitialized = true;
		} else {
			float deltaX = Math.abs(mLastX - f_ax);
			float deltaY = Math.abs(mLastY - f_ay);
			float deltaZ = Math.abs(mLastZ - f_az);
		}*/
		
		// TODO Criar um filtro para os dados
		
	}
	// ---------------- end acceleration ----------------------
}
