package com.moviles.yamba;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class RefreshService extends IntentService {
static final String TAG = "RefreshService"; //
public RefreshService() { //
super(TAG);
}
@Override
public IBinder onBind(Intent intent) { //
return null;
}
@Override
public void onCreate() { //
super.onCreate();
Log.d(TAG, "onCreated");
}
@Override
public int onStartCommand(Intent intent, int flags, int startId) { //
super.onStartCommand(intent, flags, startId);
Log.d(TAG, "onStarted");
return START_STICKY;
}
@Override
protected void onHandleIntent(Intent intent) {
	SharedPreferences prefs = PreferenceManager
			.getDefaultSharedPreferences(this);
	final String username = prefs.getString("username", "");
	final String password = prefs.getString("password", "");

	// Check that username and password are not empty
	if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
		Toast.makeText(this, "Please update your username and password",
				Toast.LENGTH_LONG).show();
		return;
	}
	Log.d(TAG, "onStarted");


	YambaClient cloud = new YambaClient(username, password);
	try {
		List<Status> timeline = cloud.getTimeline(20);
		for (Status status : timeline) {

			Log.d(TAG,
					String.format("%s: %s", status.getUser(),
							status.getMessage()));

		}

	} catch (YambaClientException e) {
		Log.e(TAG, "Failed to fetch the timeline", e);
		e.printStackTrace();
	}

	return;
}
@Override
public void onDestroy() { //
super.onDestroy();
Log.d(TAG, "onDestroyed");
}
}