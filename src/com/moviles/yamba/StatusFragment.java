package com.moviles.yamba;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class StatusFragment extends Fragment implements OnClickListener {
    	private static final String TAG = StatusFragment.class.getSimpleName();;
        private EditText editStatus;
        private Button buttonTweet;
        private TextView textCount;
        private int defaultTextColor;
        
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	
        	View view = inflater.inflate(R.layout.fragment_status,null, false);
            
            editStatus = (EditText) view.findViewById(R.id.editStatus);
            buttonTweet = (Button) view.findViewById(R.id.buttonTweet);
            textCount = (TextView) view.findViewById(R.id.textViewCount);
            
            
            
            defaultTextColor = textCount.getTextColors().getDefaultColor();
            buttonTweet.setOnClickListener(new OnClickListener() {

    			@Override
    			public void onClick(View v) {
    				String status = editStatus.getText().toString();
    				PostTask postTask = new PostTask();
    				postTask.execute(status);
    				Log.d(TAG, "onClicked");
    			}

    		});
   	     editStatus.addTextChangedListener(new TextWatcher(){ 
   	    	 
   	    	 @Override
   	    	 public void afterTextChanged(Editable s){
   	    		 int count = 140 - editStatus.length();
   	    		 textCount.setText(Integer.toString(count));
   	    		 textCount.setTextColor(Color.GREEN);
   	    		 if (count<10)
   	    			 textCount.setTextColor(Color.RED);
   	    		 else
   	    			 textCount.setTextColor(defaultTextColor);
   	    	 }
   	    	 
   	    	 @Override
   	    	 public void onTextChanged(CharSequence s, int start, int before, int count){
   	    		 
   	    	 }

   			@Override
   			public void beforeTextChanged(CharSequence s, int start, int count,
   					int after) {
   				// TODO Auto-generated method stub
   				
   			}
   	     });
   	     	
   	     	return view;
        }
       
        @Override
        public void onClick(View view) {
            String status = editStatus.getText().toString();
            Log.d(TAG, "onClicked with status: " + status);
            new PostTask().execute(status); // 
        }
        
        private final class PostTask extends AsyncTask<String, Void, String> {  
        	private ProgressDialog progress;
                @Override
                protected void onPreExecute() {
        			progress = ProgressDialog.show(getActivity(), "Posting",
        					"Please wait...");
        			progress.setCancelable(true);
        		}
                @Override
            protected String doInBackground(String... params) {  
               
               try {
            	   SharedPreferences prefs = PreferenceManager
   						.getDefaultSharedPreferences(getActivity());
   				String username = prefs.getString("username", "");
   				String password = prefs.getString("password", "");

   				// Check that username and password are not empty
   				// If empty, Toast a message to set login info and bounce to
   				// SettingActivity
   				// Hint: TextUtils.
   				if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
   					getActivity().startActivity(
   							new Intent(getActivity(), SettingsActivity.class));
   					return "Please update your username and password";
   				}

   				YambaClient cloud = new YambaClient(username, password);
   				cloud.postStatus(params[0]);
                    
                  return "Successfully posted";//solo por ver
               } catch (YambaClientException e) {
                  e.printStackTrace();
                  return "Failed to post to yamba service";
               }
             }
                @Override
             protected void onPostExecute(String result) {
                super.onPostExecute(result);
                progress.dismiss();
    			if (getActivity() != null && result != null)
    				Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
    		}

        }
}