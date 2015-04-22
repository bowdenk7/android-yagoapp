package app.nightlife.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import app.nightlife.contents.*;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import app.nightlife.adapters.PromotionAdapter;
import app.nightlife.contents.PromotionContent;
import app.nightlife.contents.StaticVariables;
import app.nightlife.contents.VenueContent;
import app.nightlife.fragments.MapFragment.LocationFeedAsync;
import app.nightlife.utilities.JsonParser;
import app.nightlife.utilities.WebServicesLinks;
import app.nightlife.yago.MainActivity;
import app.nightlife.yago.R;
import app.nightlife.yago.R.id;
import app.nightlife.yago.R.layout;

public class PromotionFragment extends Fragment{
	ListView promotionsListView;
	ArrayList<PromotionContent> list;
	PromotionAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_promotion, container, false);

		promotionsListView = (ListView) rootView.findViewById(R.id.promotions_list);
		ImageView back=(ImageView)getActivity().findViewById(R.id.back);
		TextView dynamic_text=(TextView)getActivity().findViewById(R.id.dynamic_text);
		back.setVisibility(View.INVISIBLE);
		dynamic_text.setText("Promotion");

		ImageView profileImage=(ImageView)getActivity().findViewById(R.id.profile_icon);
		profileImage.setBackgroundResource(android.R.color.transparent); 
		ImageView yagoImage=(ImageView)getActivity().findViewById(R.id.yago_icon);
		yagoImage.setBackgroundResource(android.R.color.transparent);
		ImageView moneyImage=(ImageView)getActivity().findViewById(R.id.money_bag);
		moneyImage.setBackgroundResource(android.R.color.background_dark);

		list = new ArrayList<PromotionContent>();
//		PromotionContent c1=new PromotionContent();
//		c1.setPromotion_name("Promo 1");
//		c1.setPromotion_venue("HAVANA");
//		c1.setPromotion_value("20");
//		list.add(c1);
//		PromotionContent c2=new PromotionContent();
//		c2.setPromotion_name("Promo 1");
//		c2.setPromotion_venue("HAVANA");
//		c2.setPromotion_value("20");
//		list.add(c2);
		adapter = new PromotionAdapter(getActivity(), list);
		promotionsListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			new PromotionTypeFeedAsync(getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else{
			new PromotionTypeFeedAsync(getActivity()).execute();
		}
		promotionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Object listItem = promotionsListView.getItemAtPosition(position);
				StaticVariables.selected_promotion=list.get(position);
				MainActivity.fragment = new PromotionDetailFragment();
				MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_load, MainActivity.fragment).commit();
			} 
		});
		return rootView;
	}
	public class PromotionTypeFeedAsync extends AsyncTask<String, Void, String> {
		private String TAG_APPLICATION="application";
		private Context context;
		public PromotionTypeFeedAsync(Context context) {
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
			String url = WebServicesLinks.promotion_type_feed;
//			url = url.replace(" ", "%20");
//			url=url.replace("\n", "%0A");
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
							PromotionContent promoCon=new PromotionContent();
							promoCon.setPromotion_venue( dataIndex.getString("venue_name"));
							promoCon.setPromotion_name(dataIndex.getString("name"));
							promoCon.setPromotion_value(dataIndex.getString("point_cost"));
							promoCon.setPromotion_pk(dataIndex.getString("pk"));
							
							list.add(promoCon);
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
			adapter.notifyDataSetChanged();
			Log.w("result",result);

		}
	}
}
