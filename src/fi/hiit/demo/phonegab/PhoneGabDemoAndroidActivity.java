package fi.hiit.demo.phonegab;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gcm.*;

import org.apache.cordova.*;

public class PhoneGabDemoAndroidActivity extends DroidGap {
	
	// service id for GCM
	public static String SENDER_ID = "";
	
	public static boolean isRunning = false;
	
	private BroadcastReceiver receiver = null;
		
	public void onResume() {
		super.onResume();
		isRunning = true;
		
        receiver = new PushReciever();
        registerReceiver(receiver, new IntentFilter(SENDER_ID) );
        
        Log.d(TAG, "JS Resumed");
        
        String js = getIntent().getStringExtra("javascript");
        
        if( js != null ) {
        	this.executeJS( js );
        }

	}
	
	public void onPause() {
		super.onPause();
		isRunning = false;
		
		unregisterReceiver(receiver);
		// remove this activity
		this.finish();
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Kissa!");
        
        // open PhoneGab app
        super.setIntegerProperty("loadUrlTimeoutValue", Integer.MAX_VALUE);
        super.loadUrl("file:///android_asset/html/clean.html");
        
        SENDER_ID = getString( R.string.gcm_app_id );
        
        // register device
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
          GCMRegistrar.register(this, SENDER_ID);
          Log.v(TAG, "Register");
        } else {
          Log.v(TAG, "OK");
          Log.v(TAG, regId );
        }
        
        // disable keyboard lock
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        
        Log.v(TAG, "Created!!!");
    }
    
    public void executeJS(String js){
    	
    	// create notification
       	Log.d( TAG , "JS executed " + js);
    	// alert the user that a new message has arrived    	
    	super.loadUrl("javascript:" + js);
    }
    
	private class PushReciever extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			Log.v(TAG, "GOT JS");
			// get message and execute it
			String js = intent.getStringExtra("javascript");
			PhoneGabDemoAndroidActivity.this.executeJS(js);
			
		}
		
	}
	
}