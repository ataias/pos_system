package egl.positioningsystem;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{

	// Objects to deal with location ------------------
	private TextView latitude = null;
	private TextView longitude = null;
	private String str_longitude;
	private String str_latitude;	
	private LocationManager locationManager=null;
	private LocationListener locationListener=null;	
	// --------- end location objects -----------------
	
	// Objects to deal with acceleration
	private SensorManager mSensorManager;
	private boolean mInitialized;
	private Sensor mAccelerometer;
	private float mLastX, mLastY, mLastZ;
	private final float NOISE = (float) 0.0;
	//---------- end acceleration objects -------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //GPS --------------------------------------------------
        latitude = (TextView) findViewById(R.id.latitude_value);
        longitude = (TextView) findViewById(R.id.longitude_value);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // -----------------------------------------------------
        
        //Acceleration -----------------------------------------
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        // -----------------------------------------------------
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    // -------------- GSP ------------------
    public void onClick(View view){
    	locationListener = new MyLocationListener();
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    	
    }
    //TODO Melhorar GPS
	//Class to get location
	private class MyLocationListener implements LocationListener {
    	@Override
    	public void onLocationChanged(Location position){
    		/*
    		 * Atenção!
    		 * Para este método de callback ser chamado,
    		 * é necessário que haja permissão para que 
    		 * o programa acesse o GPS, caso contrário 
    		 * ele trava do nada.
    		 * */
    	//	double d_longitude = position.getLongitude();
    	//	double d_latitude  = position.getLatitude();
    	//	latitude.setText("ataías");
    		str_longitude = ((Double)position.getLongitude()).toString();
    		str_latitude  = ((Double)position.getLatitude()).toString();
            latitude.setText(str_latitude);
            longitude.setText(str_longitude);
    	}
    	
        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub        	
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub        	
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub        	
        }
    }
    // -------------- end gps --------------
	
	//Acceleration --------------------------------------------
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// can be safely ignored for this demo
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		TextView tvAccelX= (TextView)findViewById(R.id.value_accel_x);
		TextView tvAccelY= (TextView)findViewById(R.id.value_accel_y);
		TextView tvAccelZ= (TextView)findViewById(R.id.value_accel_z);
		
		float f_ax = event.values[0];
		float f_ay = event.values[1];
		float f_az = event.values[2];
		
		if(!mInitialized){
			mLastX = f_ax;
			mLastY = f_ay;
			mLastZ = f_az;
			tvAccelX.setText("0.0");
			tvAccelY.setText("0.0");
			tvAccelZ.setText("0.0");
			mInitialized = true;
		} else {
			float deltaX = Math.abs(mLastX - f_ax);
			float deltaY = Math.abs(mLastY - f_ay);
			float deltaZ = Math.abs(mLastZ - f_az);
			
			if (deltaX > NOISE){
				tvAccelX.setText(Float.toString(f_ax));
			}
			
			if (deltaY > NOISE){
				tvAccelY.setText(Float.toString(f_ay));
			}
			
			if(deltaZ > NOISE){
				tvAccelZ.setText(Float.toString(f_az));
			}
		}
		
	}
	// ---------------- end acceleration ----------------------
	
}
