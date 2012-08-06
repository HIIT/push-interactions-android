package fi.hiit.demo.phonegab;

import android.content.*;
import android.os.Bundle;
import android.util.Log;

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
        
        // get the current activity javascript
        Intent i = getIntent();
        String js = i.getStringExtra("javascript");
        executeJS(js);
	}
	
	public void onPause() {
		super.onPause();
		isRunning = false;
		
		unregisterReceiver(receiver);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SENDER_ID = getString( R.string.gcm_app_id );

        
        // register device
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
          GCMRegistrar.register(this, SENDER_ID);
        } else {
          Log.v(TAG, regId );
        }
        
        // open PhoneGab app
        super.setIntegerProperty("loadUrlTimeoutValue", Integer.MAX_VALUE);
        super.loadUrl("file:///android_asset/html/clean.html");
    }
    
    public void executeJS(String js){
    	super.loadUrl("javascript:" + js);
    }
    
	private class PushReciever extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			Log.v(TAG, "Hello World");
			// get message and execute it
			String js = intent.getStringExtra("javascript");
			PhoneGabDemoAndroidActivity.this.executeJS(js);
			
		}
		
	}
    
}