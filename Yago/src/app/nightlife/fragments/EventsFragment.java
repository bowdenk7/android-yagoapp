package app.nightlife.fragments;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.StringEntity;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import app.nightlife.adapters.EventAdapter;
import app.nightlife.contents.EventContent;
import app.nightlife.contents.MapContent;
import app.nightlife.contents.StaticVariables;
import app.nightlife.fragments.MapFragment.LocationFeedAsync;
import app.nightlife.utilities.JsonParser;
import app.nightlife.utilities.WebServicesLinks;
import app.nightlife.yago.MainActivity;
import app.nightlife.yago.R;

@SuppressWarnings("deprecation")
public class EventsFragment extends Fragment {
	ListView eventsListView;
	ArrayList<EventContent> list;
	EventAdapter adapter;
	TextView no_event;
	TextView likes;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_events, container, false);
		eventsListView = (ListView) rootView.findViewById(R.id.events_list);

		no_event=(TextView) rootView.findViewById(R.id.no_event);

		ImageView back=(ImageView)getActivity().findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.fragment = new VenueFeedFragment();
				MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_load, MainActivity.fragment).commit();
			}
		});
		TextView dynamic_text=(TextView)getActivity().findViewById(R.id.dynamic_text);

		dynamic_text.setText("Events");

		ImageView profileImage=(ImageView)getActivity().findViewById(R.id.profile_icon);
		profileImage.setBackgroundResource(android.R.color.transparent); 
		ImageView yagoImage=(ImageView)getActivity().findViewById(R.id.yago_icon);
		yagoImage.setBackgroundResource(android.R.color.background_dark);
		ImageView moneyImage=(ImageView)getActivity().findViewById(R.id.money_bag);
		moneyImage.setBackgroundResource(android.R.color.transparent);

		list = new ArrayList<EventContent>();
		if(list.isEmpty()){
			no_event.setVisibility(View.VISIBLE);
			eventsListView.setVisibility(View.GONE);
		}
		adapter = new EventAdapter(getActivity(), list);
		eventsListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			new RecentDistrictPostsAsync(getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else{
			new RecentDistrictPostsAsync(getActivity()).execute();
		}
		
		//new PostLike(getActivity(), "13").execute();
		return rootView;
	}
	//	public void onLike(View view){
	//		int pos=(Integer)view.getTag();
	//		Toast.makeText(getActivity(), pos+"", Toast.LENGTH_LONG).show();
	//		//new PostLike(getActivity(), "13").execute();
	//	}
	public class RecentDistrictPostsAsync extends AsyncTask<String, Void, String> {
		private String TAG_APPLICATION="application";
		public RecentDistrictPostsAsync(Context context) {
		}
		@Override
		public void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		public String doInBackground(String... arg0) {
			/*
			 * Will make http call here This call will download required data
			 */
			Log.i("Response: ", "Start background");

			String url = WebServicesLinks.recent_district_posts+StaticVariables.district_venue_id;
			//String url = WebServicesLinks.closest_venues+"33.3333,120.2345";
			//String url = WebServicesLinks.top_district_feed+"1";

			//String url=WebServicesLinks.users+"1";
			//			url=url.replace("'", "");
			//			url = url.replace(" ", "%20");
			//			url=url.replace("\n", "%0A");

			JsonParser jsonParser = new JsonParser();
			String json = jsonParser
					.getJSONFromUrl(url);
			Log.w("URL ",url);
			//Log.i("Response: ", "after calling json ");
			//Log.i("Response: ", "> " + json);
			if (json != null) {
				try {
					//Log.w("Here","1");
					JSONArray dataArray = new JSONArray(json);
					Log.w("length",dataArray.length()+"");
					if(dataArray.length()>0){

						for(int i=0; i<dataArray.length();i++){
							JSONObject dataIndex = dataArray.getJSONObject(i);
							EventContent e1=new EventContent();
							e1.setEvent_id(dataIndex.getString("id"));
							e1.setEvent_image_url(dataIndex.getString("image_url"));
							e1.setEvent_like_count(dataIndex.getString("like__count"));
							e1.setEvent_position(dataIndex.getString("position"));
							e1.setEvent_thumbnail_url(dataIndex.getString("thumbnail_url"));
							e1.setEvent_time_text(dataIndex.getString("time_text"));
							e1.setEvent_timestamp(dataIndex.getString("timestamp"));
							e1.setEvent_user(dataIndex.getString("user"));
							e1.setEvent_venue(dataIndex.getString("venue"));

							list.add(e1);
						}

					}
					else{

					}
				}
				catch(JSONException e){
					e.getStackTrace();
				}
			}

			return json;
		}

		@SuppressLint("ShowToast")
		@Override
		public void onPostExecute(String result) {
			super.onPostExecute(result);
			//generateNoteOnSD("errorfile.html", result);
			if(list.size()>0){
			no_event.setVisibility(View.GONE);
			eventsListView.setVisibility(View.VISIBLE);
			adapter.notifyDataSetChanged();
			}
			//Log.w("result",result);

		}
	}
	public void generateNoteOnSD(String sFileName, String sBody){
		try
		{
			File root = new File(Environment.getExternalStorageDirectory(), "Notes");
			if (!root.exists()) {
				root.mkdirs();
			}
			File gpxfile = new File(root, sFileName);
			FileWriter writer = new FileWriter(gpxfile);
			writer.append(sBody);
			writer.flush();
			writer.close();
			Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
		}
		catch(IOException e)
		{
			e.printStackTrace();

		}
	}

	//	 class PostLike extends AsyncTask<String, Void, String> {
	//
	//			
	//			String post_id;
	//			public PostLike(Context context, String post_id) {
	//				this.post_id=post_id;
	//			}
	//			
	//			@Override
	//			protected void onPreExecute() {
	//			}
	//
	//			public String likePosMethod() {
	//				
	//				System.setProperty("http.keepAlive", "true");
	//				String json = "";
	//				
	//				// Create a new HttpClient and Post Header
	//				HttpClient httpclient = new DefaultHttpClient();
	//				HttpPost httppost = new HttpPost(WebServicesLinks.like_post);
	//				httppost.addHeader("Content-type", "application/json");
	//				
	//				try {
	//				    // Add your data
	//				    //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	//				    //nameValuePairs.add(new BasicNameValuePair("post",post_id ));
	//				    JSONObject dato = new JSONObject();
	//				    dato.put("post", post_id);
	//				    StringEntity entity = new StringEntity(dato.toString());
	//				    //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	//				    httppost.setEntity(entity);
	//				    // Execute HTTP Post Request
	//				    HttpResponse response = WebServicesLinks.httpClient.execute(httppost);
	//				    
	//			    	HttpEntity entity1 = response.getEntity();
	//			    	
	//
	//					InputStream is = entity1.getContent();
	//
	//					json = IOUtils.toString(is, "ISO-8859-1");
	//					Log.w("JSON",json);
	//				} 
	//				catch (ClientProtocolException e) {
	//				    // TODO Auto-generated catch block
	//				} 
	//				catch (IOException e) {
	//				    // TODO Auto-generated catch block
	//				} catch (JSONException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				}
	//				
	//				return json;
	//			} 
	//		
	//
	//			@Override
	//			protected String doInBackground(String... arg0) {
	//				
	//				String answer = likePosMethod();
	//				return answer;
	//			}
	//
	//			@SuppressLint("ShowToast")
	//			@Override
	//			protected void onPostExecute(String result) {
	//				Log.w("result1",result);
	//			} 
	//		}
}
