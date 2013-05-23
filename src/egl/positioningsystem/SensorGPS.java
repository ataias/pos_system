package egl.positioningsystem;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class SensorGPS implements SensorEventListener{

	private double longitude;
	private double latitude;
	private LocationListener locationListener=null;
	private float velocidade;	
	
	public SensorGPS(LocationManager locationManager){
    	locationListener = new MyLocationListener();
    	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
	}
	
	// TODO Criar um filtro para os dados
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	public double getVelocidade(){
		return velocidade;
	}
	
    //TODO Melhorar GPS
    //TODO outro teste 
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
    		velocidade = position.getSpeed();
    		longitude = position.getLongitude();
    		latitude  = position.getLatitude();
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
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}
}