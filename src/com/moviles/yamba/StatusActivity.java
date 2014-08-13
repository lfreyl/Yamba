package com.moviles.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class StatusActivity extends Activity implements OnClickListener {
	private static final String TAG="StatusActivity";
	private EditText editStatus;
	private Button buttonTweet;
	Twitter twitter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
		
		editStatus = (EditText) findViewById(R.id.editStatus);
		buttonTweet = (Button)findViewById(R.id.buttonTweet);
		
		buttonTweet.setOnClickListener(this);
		
		Log.d(TAG,"Create twiter object");
		twitter = new Twitter("student","password");
		twitter.setAPIRootUrl("http://yamba.marakana.com/api");
		Log.d(TAG,"Set twitter object API root URL");
	}
	public void onClick(View View){
		String status = editStatus.getText().toString();
		
		new PostToTwitter().execute(status);
		Log.d(TAG, "Tweet Enviado"+status);
		
	}
	
	
	
	private final class PostToTwitter extends AsyncTask<String, Integer, String>{
		
		protected String doInBackground(String...  statuses){
			try{
				Twitter.Status status=twitter.updateStatus(statuses[0]);
				return status.text;
			}catch (TwitterException e){
				Log.e(TAG, e.toString());
				e.printStackTrace();
				return "Failed to post";
			}
			
		}
		protected void onProgressUpdate(Integer... values){
			
			super.onProgressUpdate(values);
		
		}
		
		
		protected void onPostExecute(String result){
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
		}
		
	
	
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.status, menu);
		return true;
	}
	
	public boolean onOptionsItemSelect(MenuItem item){
		int id=item.getItemId();
		if(id==R.id.action_settings){
			return true;
		}
		return super.onOptionsItemSelected(item);
		
	}

}

