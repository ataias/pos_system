package egl.positioningsystem;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
//import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.socket.SocketTask;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import egl.positioningsystem.SensorGPS;

public class MainActivity extends FragmentActivity {
	
	// Objects to deal with location ------------------
	private TextView tv_latitude = null;
	private TextView tv_longitude = null;
	private String str_longitude;
	private String str_latitude;
	private String str_velocidade;
	private Double latitude;
	private Double longitude;
	private Double velocidade;
	private SensorManager mSensorManager;
	private LocationManager locationManager = null;

	// Objects to deal with acceleration ------------------
	private SensorAccel myAccel = null;
	private SensorGPS myGPS = null;
	
	//To create infinite loop
	private Handler handler = null;
	
	//Socket
	private Button btnSend;
    private TextView txtStatus;
    //private TextView txtValor;
    private TextView txtHostPort;
    private SocketTask st;
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
        
        //Acceleration -----------------------------------------
    	mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    	myAccel = new SensorAccel(mSensorManager);

    	//Socket
        btnSend = (Button) findViewById(R.id.button2);
        txtStatus = (TextView) findViewById(R.id.textView8);
        //txtValor = (TextView) findViewById(R.id.editText2);
        txtHostPort = (TextView) findViewById(R.id.editText1);
        btnSend.setOnClickListener(btnConnectListener);        
    	
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        device_id = tm.getDeviceId();
    	//Automation of Sensor Handling
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
    	velocidade = (Double) myGPS.getVelocidade();
    	str_latitude = latitude.toString();
    	str_longitude =longitude.toString();
    }
   
    private Runnable runnable = new Runnable() {
    	   @SuppressLint("SimpleDateFormat")
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
    			
    	     automaticSender = new SocketTask("164.41.209.30", 8080, 1500){
    	            @SuppressLint("SimpleDateFormat")
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
    	    
    	    String sendThis = device_id + "," + sdf.format(new Date()) + "," + str_latitude + "," +  str_longitude + "," + velocidade;
    	    automaticSender.execute("eu" == null ? "" : sendThis);
    		/*  try {
				automaticSender.sendData("Ataias");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
    	      handler.postDelayed(this, 2000);
    	   }
    	};
    	
    	//TODO Verificar problemas e limpar gambiarras
        private View.OnClickListener btnConnectListener = new View.OnClickListener() {
            public void onClick(View v) {
     
                // Recupera host e porta
                String hostPort = txtHostPort.getText().toString();
                int idxHost = hostPort.indexOf(":");
                final String host = hostPort.substring(0, idxHost);
                final String port = hostPort.substring(idxHost + 1);
     
                // Instancia a classe de conexão com socket
                try
                {
                	st = new SocketTask(host, Integer.parseInt(port), 5000) {
                		@SuppressLint("SimpleDateFormat")
                		@Override
                		protected void onProgressUpdate(String... progress) {
                			SimpleDateFormat sdf = new SimpleDateFormat(
                					"dd/MM/yyyy HH:mm:ss");
                			// Recupera o retorno
                			txtStatus.setText(sdf.format(new Date()) + " - "
                                + progress[0]);
                		}
                	};
                
                //TODO Verify problems here:
                st.execute("eu" == null ? " " : "eu"
                        .toString()); // Envia os dado
                }catch(Exception e){
                	Toast.makeText(MainActivity.this, "INPUT ERROR!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        @Override
        protected void onDestroy() {
            super.onDestroy();
            st.cancel(true);
        }
}