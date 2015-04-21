package app.nightlife.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import app.nightlife.contents.EventContent;
import app.nightlife.contents.StaticVariables;
import app.nightlife.utilities.JsonParser;
import app.nightlife.utilities.WebServicesLinks;
import app.nightlife.yago.R;
import app.nightlife.yago.R.id;
import app.nightlife.yago.R.layout;

public class ProfileFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		ImageView back=(ImageView)getActivity().findViewById(R.id.back);
		TextView dynamic_text=(TextView)getActivity().findViewById(R.id.dynamic_text);
		back.setVisibility(View.INVISIBLE);
		dynamic_text.setText("Map");
		ImageView profileImage=(ImageView)getActivity().findViewById(R.id.profile_icon);
		profileImage.setBackgroundResource(android.R.color.background_dark); 
		ImageView yagoImage=(ImageView)getActivity().findViewById(R.id.yago_icon);
		yagoImage.setBackgroundResource(android.R.color.transparent);
		ImageView moneyImage=(ImageView)getActivity().findViewById(R.id.money_bag);
		moneyImage.setBackgroundResource(android.R.color.transparent);
		TextView current_points=(TextView) rootView.findViewById(R.id.present_points);
		Log.w("Points",StaticVariables.current_points);
		current_points.setText(StaticVariables.current_points);
		return rootView;
	}
//	public class GetUserInfoAsync extends AsyncTask<String, Void, String> {
//		private String TAG_APPLICATION="application";
//		Context context;
//		public GetUserInfoAsync(Context context) {
//			this.context=context;
//		}
//		@Override
//		public void onPreExecute() {
//			super.onPreExecute();
//		}
//		@Override
//		public String doInBackground(String... arg0) {
//			/*
//			 * Will make http call here This call will download required data
//			 */
//			Log.i("Response: ", "Start background");
//			
//			
//			String url=WebServicesLinks.users+StaticVariables.user_id;
////			url=url.replace("'", "");
////			url = url.replace(" ", "%20");
////			url=url.replace("\n", "%0A");
//			
//			JsonParser jsonParser = new JsonParser();
//			String json = jsonParser
//					.getJSONFromUrl(url);
//			Log.w("URL ",url);
//			//Log.i("Response: ", "after calling json ");
//			//Log.i("Response: ", "> " + json);
//			if (json != null) {
//				JSONObject data;
//				try {
//					data = new JSONObject(json);
//					StaticVariables.user_id=data.getString("pk");
//					StaticVariables.username=data.getString("username");
//					StaticVariables.current_points=data.getString("current_points");
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//
//			return json;
//		}
//
//		@SuppressLint("ShowToast")
//		@Override
//		public void onPostExecute(String result) {
//			super.onPostExecute(result);
//			
//			//Log.w("result",result);
//
//		}
//	}

	
}

