package to.doc.android.ipv6config;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class StartAtBootService extends Service 
{
	    public IBinder onBind(Intent intent) {
	    	return null;
	    }
 
	    @Override
	    public void onCreate() {
	    	Log.v(IPv6Config.LOG_TAG, "StartAtBootService Created");
	    }
 
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	    	Log.v(IPv6Config.LOG_TAG, "StartAtBootService -- onStartCommand()");	        

	    	SharedPreferences prefsPrivate = getSharedPreferences(IPv6Config.PREFERENCES_STORE, Context.MODE_PRIVATE);
	        
	        boolean autoStart = prefsPrivate.getBoolean(IPv6Config.PREFERENCE_AUTOSTART, false);
	        boolean enablePrivacy = prefsPrivate.getBoolean(IPv6Config.PREFERENCE_ENABLE_PRIVACY, false);
	        boolean enable6to4Tunnel = prefsPrivate.getBoolean(IPv6Config.PREFERENCE_CREATE_TUNNEL, false);
	        
	        Log.w(IPv6Config.LOG_TAG, "Set to autostart: " + autoStart);
	        Log.w(IPv6Config.LOG_TAG, "Set to enable privacy: " + enablePrivacy);

	        if (autoStart) {
	        	Log.w(IPv6Config.LOG_TAG, "Now enabling address privacy on all currently known interfaces, this might take a few seconds...");
	        	// only force reloading addresses when we enable privacy, not when we explicitly disable it
	        	IPv6Config.applySettingsWithGuiFeedback(getApplicationContext(), 
	        			enablePrivacy, enablePrivacy, enable6to4Tunnel);
	        }

	    	// we only need to apply the settings once, they will remain in the kernel space
	    	return Service.START_NOT_STICKY;
	    }
 
	    /*
	     * In Android 2.0 and later, onStart() is depreciated.  Use
	     * onStartCommand() instead, or compile against API Level 5 and
	     * use both.
	     * http://android-developers.blogspot.com/2010/02/service-api-changes-starting-with.html
	     */
    	@Override
    	public void onStart(Intent intent, int startId) {
    		Log.v(IPv6Config.LOG_TAG, "StartAtBootService -- onStart()");
    		
    		onStartCommand(intent, 0, startId);
    	}
 
	    @Override
	    public void onDestroy() {
	    	Log.v(IPv6Config.LOG_TAG, "StartAtBootService Destroyed");
	    }
}
