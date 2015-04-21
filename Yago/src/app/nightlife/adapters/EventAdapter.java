package app.nightlife.adapters;

import java.io.IOException;
import java.io.InputStream;
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

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import app.nightlife.contents.EventContent;
import app.nightlife.utilities.AppController;
import app.nightlife.utilities.WebServicesLinks;
import app.nightlife.yago.R;
import app.nightlife.yago.R.id;
import app.nightlife.yago.R.layout;

public class EventAdapter extends BaseAdapter {
	private Activity mContext;
	private List<EventContent> mEc;
	ImageLoader imageLoader;
	TextView eventLikes;
	public EventAdapter(Activity context, List<EventContent> ec) {
		mContext = context;
		mEc = ec;
	}

	@Override
	public int getCount() {
		return mEc.size();
	}

	@Override
	public Object getItem(int pos) {
		return mEc.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}


	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {
		final EventContent ec = mEc.get(pos);
		
		if(convertView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(mContext);

			convertView = inflater.inflate(R.layout.event_item, null);
		}
		if (imageLoader == null){
			imageLoader = AppController.getInstance().getImageLoader();
		}
		TextView eventTime = (TextView)convertView.findViewById(R.id.time);
		eventTime.setText(ec.getEvent_time_text());
		
		eventLikes = (TextView)convertView.findViewById(R.id.likes);
		eventLikes.setText(ec.getEvent_like_count());
		eventLikes.setTag(pos);
		eventLikes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.w("clicked at ", pos+"");
				new PostLike(mContext, ec.getEvent_id()).execute();
				//eventLikes.setText("1");
			}
		});
		NetworkImageView event_image = (NetworkImageView) convertView
				.findViewById(R.id.event_image);
		String imgUrl=ec.getEvent_image_url().replace(" ", "%20");
		event_image.setImageUrl(imgUrl, imageLoader);
		return convertView;
	}
	
	class PostLike extends AsyncTask<String, Void, String> {

		
		String post_id;
		public PostLike(Context context, String post_id) {
			this.post_id=post_id;
		}
		
		@Override
		protected void onPreExecute() {
		}

		public String likePosMethod() {
			
			System.setProperty("http.keepAlive", "true");
			String json = "";
			
			// Create Post Header
			
			HttpPost httppost = new HttpPost(WebServicesLinks.like_post);
			httppost.addHeader("Content-type", "application/json");
			
			try {
			    // Add your data
			    //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			    //nameValuePairs.add(new BasicNameValuePair("post",post_id ));
			    JSONObject dato = new JSONObject();
			    dato.put("post", post_id);
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
			
			String answer = likePosMethod();
			return answer;
		}

		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(String result) {
			Log.w("result1",result);
			try{
			JSONObject data = new JSONObject(result);
			String likes=data.getString("total_likes");
			eventLikes.setText(likes);
			notifyDataSetChanged();
			}
			catch(Exception e){
				
			}
		} 
	}

}