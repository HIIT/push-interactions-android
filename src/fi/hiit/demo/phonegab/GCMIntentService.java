package fi.hiit.demo.phonegab;

import java.net.URI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	
	public GCMIntentService(){
		super(PhoneGabDemoAndroidActivity.SENDER_ID);
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.d(TAG, arg1);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		// get the executable js
		// TODO: FIXME
		String js = intent.getStringExtra("javascript");
		Intent i = new Intent(PhoneGabDemoAndroidActivity.SENDER_ID );
        i.putExtra("javascript", js);
		context.sendBroadcast(i);
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		Log.d(TAG, arg1);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}


}
