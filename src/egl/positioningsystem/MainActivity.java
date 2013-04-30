package egl.positioningsystem;

import java.util.Timer;
import java.util.TimerTask;

import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import egl.positioningsystem.SensorGPS;

public class MainActivity extends Activity {

	// Objects to deal with location ------------------
	private TextView tv_latitude = null;
	private TextView tv_longitude = null;
	private String str_longitude;
	private String str_latitude;
	private Double latitude;
	private Double longitude;
	private SensorManager mSensorManager;
	private LocationManager locationManager = null;
	
	private SensorAccel myAccel = null;
	private SensorGPS myGPS = null;
	
	private Timer timer;
	// --------- end location objects -----------------
	
	//---------- end acceleration objects -------------

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// Objects to deal with acceleration
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //GPS --------------------------------------------------
        tv_latitude = (TextView) findViewById(R.id.latitude_value);
        tv_longitude = (TextView) findViewById(R.id.longitude_value);
    	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	myGPS = new SensorGPS(locationManager);
    	
        // -----------------------------------------------------
        
        //Acceleration -----------------------------------------
    	mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    	myAccel = new SensorAccel(mSensorManager);
        // -----------------------------------------------------
    }
 
    //Acceleration --------------------------------------------
	protected void onResume() {
    	super.onResume();
    	mSensorManager.registerListener(myAccel, myAccel.mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    	timer = new Timer();
    	timer.schedule(new TimerTask() {
    	@Override
    	public void run() {
    			getGPSData();
    	    	tv_latitude.setText(str_latitude);
    	    	tv_longitude.setText(str_longitude);
    	    	
    			TextView tvAccelX= (TextView)findViewById(R.id.value_accel_x);
    			TextView tvAccelY= (TextView)findViewById(R.id.value_accel_y);
    			TextView tvAccelZ= (TextView)findViewById(R.id.value_accel_z);
    			
    			tvAccelX.setText(Float.toString(myAccel.getAccelX()));
    			tvAccelY.setText(Float.toString(myAccel.getAccelY()));
    			tvAccelZ.setText(Float.toString(myAccel.getAccelZ()));

    		}
    	}, 0, 100);
	}
    	
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(myAccel);
	}

	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    // -------------- GSP ------------------
    public void onClick(View view){
    	getGPSData();
    	tv_latitude.setText(str_latitude);
    	tv_longitude.setText(str_longitude);
    	
		TextView tvAccelX= (TextView)findViewById(R.id.value_accel_x);
		TextView tvAccelY= (TextView)findViewById(R.id.value_accel_y);
		TextView tvAccelZ= (TextView)findViewById(R.id.value_accel_z);
		
		tvAccelX.setText(Float.toString(myAccel.getAccelX()));
		tvAccelY.setText(Float.toString(myAccel.getAccelY()));
		tvAccelZ.setText(Float.toString(myAccel.getAccelZ()));
    }
    
    public void getGPSData(){
    	latitude = (Double) myGPS.getLatitude();
    	longitude = (Double) myGPS.getLongitude();
    	str_latitude = latitude.toString();
    	str_longitude =longitude.toString();
    }

    // -------------- end gps --------------
	
	/*/Acceleration --------------------------------------------
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
	*/
}
