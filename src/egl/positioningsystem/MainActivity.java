package egl.positioningsystem;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity{

	private TextView latitude = null;
	private TextView longitude = null;
	private String str_longitude;
	private String str_latitude;
	
	private LocationManager locationManager=null;
	private LocationListener locationListener=null;	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitude = (TextView) findViewById(R.id.TextView01);
        longitude = (TextView) findViewById(R.id.TextView02);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onClick(View view){
    	locationListener = new MyLocationListener();
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    	
    }
    
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
    
}
