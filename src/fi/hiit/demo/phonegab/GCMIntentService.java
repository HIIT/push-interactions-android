package fi.hiit.demo.phonegab;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

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
		Intent push = new Intent(); // new Intent( PhoneGabDemoAndroidActivity.SENDER_ID );
        push.putExtra("javascript", js);
        
        PendingIntent pi = null;
		
		// check if application is running
		if( PhoneGabDemoAndroidActivity.isRunning ) {
			sendBroadcast( push );
			pi = null;
			// pi = PendingIntent.getBroadcast( context, 0, intent, PendingIntent.FLAG_ONE_SHOT );
		} else {
	        // if application is not running, following attributes are needed
	        push.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		    push.setClass( context, PhoneGabDemoAndroidActivity.class );
		    startActivity( push ); // This should not be needed, as there is a pending activity
		    pi = PendingIntent.getActivity( context, 0, push, PendingIntent.FLAG_ONE_SHOT );
		}
		
    	long[] vibraPattern = {0, 500, 250, 500 };
		
    	 Notification noti = new Notification.Builder( getApplicationContext() )
    	 .setVibrate( vibraPattern )
         .setDefaults( Notification.DEFAULT_SOUND )
         .setFullScreenIntent( pi , true )
         .setContentIntent(pi)
         .setContentTitle("PushI")
         .setContentText( js )
         .setSmallIcon(R.drawable.ic_launcher)
         .getNotification();
    	 
    	 // noti.fullScreenIntent = pi;

    	 NotificationManager notiMng = 
    		        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	 notiMng.notify( 0 , noti ); 
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
