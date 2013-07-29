package egl.positioningsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver
{
	//private static final String msg="Hello! I am alive!";
	private SensorGPS myGPS = null;
	private Double latitude;
	private Double longitude;
	private String str_longitude;
	private String str_latitude;
	private SharedPreferences mPrefs = null;
	private String mDeviceID;
    @Override
    public void onReceive(Context context, Intent intent) 
    {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String str = "";            
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object pdus[] = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];            
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
            //    str += "SMS from " + msgs[i].getOriginatingAddress();                     
            //    str += " :";
                str += msgs[i].getMessageBody().toString();
            //    str += "\n";        
            }
            String phoneNumber=msgs[0].getOriginatingAddress();
            
            //---display the new SMS message---
            //Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            //Parser
            String delims = "[ ]+";
            //Se quiser separar por vírgulas, coloque "[,]+"
            String[] tokens = str.split(delims);
            //tokes são os elementos de cada mensagem recebida
            //Cada token, indo de 0 a n, são as "palavras" que há numa mensagem recebida
            //Se a mensagem é "asd asdfb casdf   asdf" os tokens são as palavras separadas
            //Ignoram-se os espaços
            if(tokens[0].equals("coordinates")){
            	getGPSData();
            	String msg = "lat: "+str_latitude+"; lon: "+str_longitude+";";
            	sendSms(phoneNumber,msg);
            } else if(tokens[0].equals("adminip")){
            	String host = tokens[1];
            	String port = tokens[2];
            	mPrefs.edit().putString(SettingsActivity.KEY_PREF_HOST, host).commit();
            	mPrefs.edit().putString(SettingsActivity.KEY_PREF_PORT, port).commit();
            	String msg = "Ok! adminip";
            	sendSms(phoneNumber,msg);
            } else if(tokens[0].equals("imei")){
            	sendSms(phoneNumber,mDeviceID);
            }
            //sendSms(phoneNumber,msg);
        }                         
    }
    
    public void externalData(SensorGPS myGPS_, SharedPreferences mPrefs_, String device_id){ //"constructor"
    	myGPS = myGPS_;
    	mPrefs = mPrefs_;
    	mDeviceID = device_id;
    }
    
    private void sendSms(String phonenumber,String message)
	{
		SmsManager manager = SmsManager.getDefault();
		manager.sendTextMessage(phonenumber, null, message, null, null);
	}
    
    /**
     * Get Location Data by location provider
     */
    private void getGPSData(){
    	latitude = (Double) myGPS.getLatitude();
    	longitude = (Double) myGPS.getLongitude();
   // 	velocidade = (Double) myGPS.getVelocidade();
    	str_latitude = latitude.toString();
    	str_longitude =longitude.toString();
    }
    
}