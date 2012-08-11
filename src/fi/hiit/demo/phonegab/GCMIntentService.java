package fi.hiit.demo.phonegab;

import java.net.URI;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
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
		Intent push = new Intent(PhoneGabDemoAndroidActivity.SENDER_ID );
        push.putExtra("javascript", js);
        
        
        PendingIntent pendingPush = null;
		
		// check if application is running
		if( PhoneGabDemoAndroidActivity.isRunning ) {
			pendingPush = PendingIntent.getBroadcast(context, -1, push, PendingIntent.FLAG_ONE_SHOT );
			sendBroadcast( push );
		} else {
	        // if application is not running, following attributes are needed
	        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    push.setClass( context, PhoneGabDemoAndroidActivity.class );

		    pendingPush = PendingIntent.getActivity(context, -1, push, PendingIntent.FLAG_ONE_SHOT );
		    
		    startActivity( push );
		}
		
		long[] vibraPattern = {0, 500, 250, 500 };
		
		Notification alert = 
				new Notification.Builder( context )
				.setDefaults( Notification.DEFAULT_SOUND )
				.setFullScreenIntent( pendingPush, true)
				.setVibrate( vibraPattern )
				.getNotification(); // note, newer apis require this to be .build()
		
		PhoneGabDemoAndroidActivity.notifMng.notify( 5, alert );
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
