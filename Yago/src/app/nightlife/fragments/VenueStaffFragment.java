package app.nightlife.fragments;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.StringEntity;
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
import android.widget.ImageView;
import android.widget.TextView;
import app.nightlife.contents.StaticVariables;
import app.nightlife.fragments.MapFragment.LocationFeedAsync;
import app.nightlife.utilities.WebServicesLinks;
import app.nightlife.yago.MainActivity;
import app.nightlife.yago.R;
import app.nightlife.yago.R.id;
import app.nightlife.yago.R.layout;

public class VenueStaffFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_venue_staff, container, false);
		TextView dynamic_text=(TextView)getActivity().findViewById(R.id.dynamic_text);
		dynamic_text.setText("Staff");
		ImageView back=(ImageView)getActivity().findViewById(R.id.back); 
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.fragment = new PromotionDetailFragment();
				MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_load, MainActivity.fragment).commit();
			}
		});
		ImageView yoga_logo_staff=(ImageView) rootView.findViewById(R.id.yoga_logo_staff); 
		yoga_logo_staff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
					StaticVariables.spinnerLayout.setVisibility(View.GONE);
					StaticVariables.headerLayout.setVisibility(View.VISIBLE);
					StaticVariables.footerLayout.setVisibility(View.VISIBLE);
					new PurchaseAndRedeamAsync(getActivity(),StaticVariables.selected_promotion.getPromotion_pk()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				else{
					StaticVariables.spinnerLayout.setVisibility(View.GONE);
					StaticVariables.headerLayout.setVisibility(View.VISIBLE);
					StaticVariables.footerLayout.setVisibility(View.VISIBLE);
					new PurchaseAndRedeamAsync(getActivity(),StaticVariables.selected_promotion.getPromotion_pk()).execute();
				}
	
			}
		});
		return rootView;
	}
	class PurchaseAndRedeamAsync extends AsyncTask<String, Void, String> {


		String promo_id;
		public PurchaseAndRedeamAsync(Context context, String promo_id) {
			this.promo_id=promo_id;
			StaticVariables.spinnerLayout.setVisibility(View.VISIBLE);
			StaticVariables.headerLayout.setVisibility(View.GONE);
			StaticVariables.footerLayout.setVisibility(View.GONE);
		}

		@Override
		protected void onPreExecute() {
		}

		public String redeamAndPurchaseMethod() {

			System.setProperty("http.keepAlive", "true");
			String json = "";

			// Create Post Header

			HttpPost httppost = new HttpPost(WebServicesLinks.purchase_and_redeem);
			httppost.addHeader("Content-type", "application/json");

			try {
				// Add your data
				//List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				//nameValuePairs.add(new BasicNameValuePair("post",post_id ));
				JSONObject dato = new JSONObject();
				dato.put("type", promo_id);
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

			String answer = redeamAndPurchaseMethod();
			return answer;
		}

		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(String result) {
			Log.w("result1",result);
			
			StaticVariables.spinnerLayout.setVisibility(View.GONE);
			StaticVariables.headerLayout.setVisibility(View.VISIBLE);
			StaticVariables.footerLayout.setVisibility(View.VISIBLE);
			try{
				JSONObject data = new JSONObject(result);
				String remaining_points=data.getString("new_points_total");
				Log.w("Remaining Points",remaining_points);
				MainActivity.fragment = new VenueFeedFragment();
				MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_load, MainActivity.fragment).commit();
			}
			catch(Exception e){

			}
		} 
	}
}
