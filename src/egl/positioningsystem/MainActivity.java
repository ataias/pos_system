package egl.positioningsystem;

import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
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
	
	private Handler handler = null;
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
    	
    	handler = new Handler();
    	handler.postDelayed(runnable, 100);
    }
 
    //Acceleration --------------------------------------------
	protected void onResume() {
    	super.onResume();
    	mSensorManager.registerListener(myAccel, myAccel.mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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
		float[] Accel = new float[3];
		Accel = myAccel.getAccel();
		tvAccelX.setText(Float.toString(Accel[0]));
		tvAccelY.setText(Float.toString(Accel[1]));
		tvAccelZ.setText(Float.toString(Accel[2]));
    }
    
    public void getGPSData(){
    	latitude = (Double) myGPS.getLatitude();
    	longitude = (Double) myGPS.getLongitude();
    	str_latitude = latitude.toString();
    	str_longitude =longitude.toString();
    }
    
    private Runnable runnable = new Runnable() {
    	   @Override
    	   public void run() {
    	      /* do what you need to do */
    	    	getGPSData();
    	    	tv_latitude.setText(str_latitude);
    	    	tv_longitude.setText(str_longitude);
    	    	
    			TextView tvAccelX= (TextView)findViewById(R.id.value_accel_x);
    			TextView tvAccelY= (TextView)findViewById(R.id.value_accel_y);
    			TextView tvAccelZ= (TextView)findViewById(R.id.value_accel_z);
    			
    			float[] Accel = new float[3];
    			Accel = myAccel.getAccel();
    			tvAccelX.setText(Float.toString(Accel[0]));
    			tvAccelY.setText(Float.toString(Accel[1]));
    			tvAccelZ.setText(Float.toString(Accel[2]));
    	      /* and here comes the "trick" */
    	      handler.postDelayed(this, 100);
    	   }
    	};
}
