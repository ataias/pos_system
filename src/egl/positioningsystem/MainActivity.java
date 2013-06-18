package egl.positioningsystem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
//import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
//import android.socket.SocketTask;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import egl.positioningsystem.SensorGPS;
import egl.positioningsystem.SocketTask;

public class MainActivity extends FragmentActivity {
	
	// Objects to deal with location ------------------
	private TextView tv_latitude = null;
	private TextView tv_longitude = null;
	private String str_longitude;
	private String str_latitude;
	private Double latitude;
	private Double longitude;
	private Double velocidade;
	//private SensorManager mSensorManager;
	private LocationManager locationManager = null;

	// Objects to deal with acceleration ------------------
	//private SensorAccel myAccel = null;
	private SensorGPS myGPS = null;
	
	//To create infinite loop
	private Handler handler_connection = null;
	private Handler handler_screen = null;
	private final int CONNECTION_TIME_DELAY = 10000;//10s
	private final int SCREEN_TIME_DELAY = 1000;//10s
	
	//Socket
	private String host = "164.41.65.20";//"164.41.209.30";
	private int port = 8090;
	//private String host = "186.193.7.38";/*IP for tests*/
	//private int port = 6001;
//	private Button btnSend;
    private TextView txtStatus;
    //private TextView txtValor;
    private TextView txtHostPort;
    private SocketTask automaticSender;
    
    // Handle to SharedPreferences for this app
    SharedPreferences mPrefs; 
    String device_id;
    TelephonyManager tm;
    // Handle to a SharedPreferences editor
    SharedPreferences.Editor mEditor;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {   	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //GPS --------------------------------------------------
        tv_latitude = (TextView) findViewById(R.id.latitude_value);
        tv_longitude = (TextView) findViewById(R.id.longitude_value);
    	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	myGPS = new SensorGPS(locationManager);
        
        /*Acceleration -----------------------------------------
    	mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    	myAccel = new SensorAccel(mSensorManager);*/

    	//Socket
      //  btnSend = (Button) findViewById(R.id.buttonSetHostPort);
        txtStatus = (TextView) findViewById(R.id.textViewStatus);
        txtHostPort = (TextView) findViewById(R.id.editHostPort);
        txtHostPort.setHint(host+":"+port);
    	
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        device_id = tm.getDeviceId();
        
    	//Automation of Sensor Handling
    	handler_connection = new Handler();
    	handler_connection.postDelayed(connection_runnable, 100);
    	handler_screen = new Handler();
    	handler_screen.postDelayed(screen_runnable, 100);
    	
    	
    }
 
    //Acceleration --------------------------------------------
	protected void onResume() {
    	super.onResume();
    	//mSensorManager.registerListener(myAccel, myAccel.mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
    	
	protected void onPause() {
		super.onPause();		
		//mSensorManager.unregisterListener(myAccel);
	}

	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
	protected void onDestroy() {
    	automaticSender.cancel(true);
    	super.onDestroy();
	}
    
    /**
     * Open map with the location:
     * @param Uri location
     */
    public void openMap(Uri location){
    	//Create a mapIntent:
    	Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
    	
    	//Verify if has some app to run the Intent:
    	PackageManager packageManager = getPackageManager();
    	List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
    	
    	if(activities.size() > 0){
    		//Execute Intent
    		startActivity(mapIntent);
    	}else{
    		Toast.makeText(MainActivity.this, "Problems to open a Map", Toast.LENGTH_SHORT).show();
    	}
    }
    
    /**
     * Action to the button Map
     * @param view
     */
    public void openMap(View view){
    	//TODO Check Uri for location.
    	Uri location = Uri.parse("geo: "+latitude+","+longitude);
    	openMap(location);
    }
    
    /**
     * Get Location Data by location provider
     */
    public void getGPSData(){
    	latitude = (Double) myGPS.getLatitude();
    	longitude = (Double) myGPS.getLongitude();
    	velocidade = (Double) myGPS.getVelocidade();
    	str_latitude = latitude.toString();
    	str_longitude =longitude.toString();
    }
    
    /**
     * Called when click in button buttonSetHostPort, get the host and port
     * @param view
     */
    public void setHostPort(View view){
    	// Recupera host e porta
        String hostPort = txtHostPort.getText().toString();
        
        try{
        	if(hostPort.length()>8){
        		int idxHost = hostPort.indexOf(":");
        		host = hostPort.substring(0, idxHost);
        		port = Integer.parseInt(hostPort.substring(idxHost + 1));
        	}
        	txtHostPort.setHint(host+":"+port);
            txtHostPort.clearComposingText();
            Toast.makeText(MainActivity.this, "IP: "+host+":"+port, Toast.LENGTH_SHORT).show();
        }catch(Exception e){
        	Toast.makeText(MainActivity.this, "INPUT ERROR", Toast.LENGTH_SHORT).show();
        }
        
    }
    
    /**
     * Send message to the last defined host
     */
    public void sendBySocket(){
    	sendBySocket(host,port);
    }
    
    /**
     * Send message to specified host and port by socket
     * @param host IP of server host
     * @param port Port of server host
     */
    @SuppressLint("SimpleDateFormat")
	public void sendBySocket(String host,int port){
    	try{
    	automaticSender = new SocketTask(host, port, 1500){
			@Override
            protected void onProgressUpdate(String... progress) {
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm:ss");
                // Recupera o retorno
                txtStatus.setText(sdf.format(new Date()) + " - "
                        + progress[0]);
            }
    	};
    	SimpleDateFormat sdf = new SimpleDateFormat(
            "dd/MM/yyyy HH:mm:ss");
    
    	String sendThis = device_id + "," + sdf.format(new Date()) + "," + latitude + "," +  longitude + "," + velocidade;
    	automaticSender.execute(sendThis);
    	}catch(Exception e){
        	Toast.makeText(MainActivity.this, "CONNECTION ERROR!", Toast.LENGTH_SHORT).show();
        }
    }
   
    private Runnable screen_runnable = new Runnable() {
   	   @SuppressLint("SimpleDateFormat")
   	   @Override
   	   public void run() {
   	      /* do what you need to do */
   	    	getGPSData();
   	    	tv_latitude.setText(str_latitude);
   	    	tv_longitude.setText(str_longitude);
   	    	
   	      /* and here comes the "trick" */    		

          handler_screen.postDelayed(this, SCREEN_TIME_DELAY);
       }
    };
    
    private Runnable connection_runnable = new Runnable() {
    	   @SuppressLint("SimpleDateFormat")
    	   @Override
    	   public void run() {
    	      /* do what you need to do */
    	    	sendBySocket();    	    	
    	      /* and here comes the "trick" */    		

           handler_connection.postDelayed(this, CONNECTION_TIME_DELAY);
        }
     };
}