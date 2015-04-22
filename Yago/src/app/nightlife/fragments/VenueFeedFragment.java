package app.nightlife.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import app.nightlife.adapters.GridViewAdapter;

import app.nightlife.utilities.JsonParser;
import app.nightlife.utilities.WebServicesLinks;
import app.nightlife.yago.MainActivity;
import app.nightlife.yago.R;
import app.nightlife.yago.R.id;
import app.nightlife.yago.R.layout;
import app.nightlife.contents.MapContent;
import app.nightlife.contents.StaticVariables;
import app.nightlife.contents.VenueContent;

public class VenueFeedFragment extends Fragment {
	List<VenueContent> venuesList;
	GridViewAdapter gvAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_venue_feed, container, false);
		GridView gridView = (GridView)rootView.findViewById(R.id.gridview);
		ImageView back=(ImageView)getActivity().findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		TextView dynamic_text=(TextView)getActivity().findViewById(R.id.dynamic_text);
		dynamic_text.setText("Venue");
		venuesList=new ArrayList<VenueContent>();
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.fragment = new MapFragment();
				MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_load, MainActivity.fragment).commit();
			}
		});
		ImageView profileImage=(ImageView)getActivity().findViewById(R.id.profile_icon);
		profileImage.setBackgroundResource(android.R.color.transparent); 
		ImageView yagoImage=(ImageView)getActivity().findViewById(R.id.yago_icon);
		yagoImage.setBackgroundResource(android.R.color.background_dark);
		ImageView moneyImage=(ImageView)getActivity().findViewById(R.id.money_bag);
		moneyImage.setBackgroundResource(android.R.color.transparent);
		gvAdapter=new GridViewAdapter(getActivity(),venuesList);
		gridView.setAdapter(gvAdapter);
		gvAdapter.notifyDataSetChanged();
		gridView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				MainActivity.fragment = new EventsFragment();
				StaticVariables.district_venue_id=venuesList.get(position).getPk();
				MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_load, MainActivity.fragment).commit();
			}
		});
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			new TopDistrictFeedAsync(getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else{
			new TopDistrictFeedAsync(getActivity()).execute();
		}
		
		return rootView;
	}
	public class TopDistrictFeedAsync extends AsyncTask<String, Void, String> {
		private String TAG_APPLICATION="application";
		private Context context;
		public TopDistrictFeedAsync(Context context) {
			this.context = context;
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
			String url = WebServicesLinks.top_district_feed+StaticVariables.map_district_id;
			url = url.replace(" ", "%20");
			url=url.replace("\n", "%0A");
			JsonParser jsonParser = new JsonParser();
			String json = jsonParser
					.getJSONFromUrl(url);

			Log.i("Response: ", "after calling json ");
			Log.i("Response: ", "> " + json);
			if (json != null) {
				try {
					JSONArray dataArray = new JSONArray(json);
					if(dataArray.length()>0){

						for(int i=0; i<dataArray.length();i++){
							JSONObject dataIndex = dataArray.getJSONObject(i);
							VenueContent venueCon=new VenueContent();
							venueCon.setPk( dataIndex.getString("pk"));
							venueCon.setName(dataIndex.getString("name"));
							venueCon.setPosition(dataIndex.getString("position"));
							venueCon.setLogo_url(dataIndex.getString("logo_url"));
							venueCon.setDistrict(dataIndex.getString("district"));
							venuesList.add(venueCon);
						}
						Log.w("position name",venuesList.get(0).getName()+"--"+venuesList.get(1).getName());
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
			gvAdapter.notifyDataSetChanged();
			Log.w("result",result);

		}
	}

}
