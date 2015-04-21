package app.nightlife.yago;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.StringEntity;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import app.nightlife.contents.StaticVariables;
import app.nightlife.utilities.WebServicesLinks;


@SuppressWarnings("deprecation")
public class LoginScreen extends Activity {
	 // Your Facebook APP ID
    private static String APP_ID = "1054714047878093"; // Replace your App ID here
 
    // Instance of Facebook Class
    private Facebook facebook;
    private AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    public String fb_access_token;
    public String errorMessage="";
    public String uName,uMail,uGender;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        facebook = new Facebook(APP_ID);
        mAsyncRunner = new AsyncFacebookRunner(facebook);
    }
    public void goToFBLogin(View view){
    	
    	loginToFacebook();
    	
    }
    /**
	 * Function to login into facebook
	 * */
	public void loginToFacebook() {

		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
			fb_access_token=access_token;
			
			Log.d("FB Sessions", "" + facebook.isSessionValid());
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid() || facebook.isSessionValid()) {
			facebook.authorize(this,
					new String[] { "email","user_about_me","public_profile","publish_checkins", "publish_stream" },
					new DialogListener() {

				@Override
				public void onCancel() {
					// Function to handle cancel event
					errorMessage = "You cancelled Operation";
					getResponse();
				}

				@Override
				public void onComplete(Bundle values) {
					// Function to handle complete event
					// Edit Preferences and update facebook acess_token
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString("access_token",
							facebook.getAccessToken());
					fb_access_token=facebook.getAccessToken();
					editor.putLong("access_expires",
							facebook.getAccessExpires());
					editor.commit();

					// Making Login button invisible
					getProfileInformation();
					//Log.w("email",uEmail);


				}

				@Override
				public void onError(DialogError error) {
					// Function to handle error
					
					errorMessage = "Facebook Error Occured";
					getResponse();

				}

				@Override
				public void onFacebookError(FacebookError fberror) {
					// Function to handle Facebook errors
					
					errorMessage = "Facebook Error Occured";
					getResponse();
				}
			});
		}
	}
	
	@SuppressLint("ShowToast")
	public void getResponse()
	{
		if(errorMessage.isEmpty())
		{
			
			
			if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
				new AuthenticateFBToken(getApplicationContext(),fb_access_token).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
			else{
				new AuthenticateFBToken(getApplicationContext(),fb_access_token).execute();
			}
		}
		else
		{
			Toast.makeText(this, errorMessage+"\r\nTry Again", 5000).show();
			errorMessage="";
		}

	}
	/**
	 * Get Profile information by making request to Facebook Graph API
	 * */
	public void getProfileInformation() {
		
		mAsyncRunner.request("me", new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Profile", response);
				String json = response;
				try {
					// Facebook Profile JSON data
					JSONObject profile = new JSONObject(json);
					
					uName = profile.getString("name");
					uMail = profile.getString("email");
					//uGender=profile.getString("gender");
					
					
					//final String birthday=profile.getString("birthday");
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							//Toast.makeText(getApplicationContext(), "Name: " + name + "\nEmail: " + email+"\nGender: "+gender, Toast.LENGTH_LONG).show();
							getResponse();
						}
					});
				} 
				catch (JSONException e) 
				{
					errorMessage = "JSON Error Occured";
					
					e.printStackTrace();
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
				errorMessage = "Facebook Data read Error Occured";
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e, Object state) {
				errorMessage = "Facebook Image read Error Occured";
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				errorMessage = "Facebook URL Error Occured";
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				errorMessage = "Facebook Error Occured OnFacebook";
			}
		});
	}
	
    class AuthenticateFBToken extends AsyncTask<String, Void, String> {

		private Context context;
		ProgressDialog progress1;
		String access_token;
		public AuthenticateFBToken(Context context, String access_token) {
			this.context = context;
			this.access_token=access_token;
			Log.w("Access Token ",fb_access_token);
		}
		
		@Override
		protected void onPreExecute() {
		}

		public String AccessFb() {
			
			System.setProperty("http.keepAlive", "true");
			
			String json = "";
			
			// Create a new HttpClient and Post Header
			//HttpClient httpclient = new DefaultHttpClient();
			WebServicesLinks.httpClient=new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebServicesLinks.authenticate_with_token);
			httppost.addHeader("Content-type", "application/json");
			try {
			    // Add your data
			    //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			    //nameValuePairs.add(new BasicNameValuePair("access_token",access_token ));
			    JSONObject dato = new JSONObject();
			    dato.put("access_token", access_token);
			    StringEntity entity = new StringEntity(dato.toString());
			    //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			    httppost.setEntity(entity);
			    // Execute HTTP Post Request
			    HttpResponse response = WebServicesLinks.httpClient.execute(httppost);
			    
		    	HttpEntity entity1 = response.getEntity();
		    	

				InputStream is = entity1.getContent();

				json = IOUtils.toString(is, "UTF-8");
				Log.w("JSON",json);
			} 
			catch (ClientProtocolException e) {
			    // TODO Auto-generated catch block
			} 
			catch (IOException e) {
			    // TODO Auto-generated catch block
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return json;
		} 
	

		@Override
		protected String doInBackground(String... arg0) {
			
			String answer = AccessFb();
			return answer;
		}

		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(String result) {
			Log.w("result1",result);
			JSONObject data;
			try {
				data = new JSONObject(result);
				StaticVariables.user_id=data.getString("pk");
				StaticVariables.username=data.getString("username");
				StaticVariables.current_points=data.getString("current_points");
				Log.w("Points",StaticVariables.current_points);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Intent intent=new Intent(context, AgeConfirmationActivity.class);
	    	startActivity(intent);
		} 
	}
}
