package app.nightlife.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import android.widget.ImageView;
import android.widget.TextView;
import app.nightlife.contents.MapContent;
import app.nightlife.utilities.JsonParser;
import app.nightlife.utilities.WebServicesLinks;
import app.nightlife.yago.MainActivity;
import app.nightlife.yago.R;
import app.nightlife.utilities.GPSTracker;
import app.nightlife.contents.StaticVariables;

public class MapFragment extends Fragment {
	private MapView mMapView;
	List<MapContent> mapList;
	private GoogleMap googleMap;
	private HashMap<Marker, MapContent> districtMarkerMap;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_map, container, false);
		StaticVariables.activatedFragment=StaticVariables.fragments[0];
		ImageView back=(ImageView)getActivity().findViewById(R.id.back);
		TextView dynamic_text=(TextView)getActivity().findViewById(R.id.dynamic_text);
		back.setVisibility(View.INVISIBLE);
		dynamic_text.setText("Map");

		ImageView profileImage=(ImageView)getActivity().findViewById(R.id.profile_icon);
		profileImage.setBackgroundResource(android.R.color.transparent); 
		ImageView yagoImage=(ImageView)getActivity().findViewById(R.id.yago_icon);
		yagoImage.setBackgroundResource(android.R.color.background_dark);
		ImageView moneyImage=(ImageView)getActivity().findViewById(R.id.money_bag);
		moneyImage.setBackgroundResource(android.R.color.transparent);

		mMapView = (MapView) rootView.findViewById(R.id.map_district);
		mMapView.onCreate(savedInstanceState);
		mapList=new ArrayList<MapContent>();
		mMapView.onResume(); 
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			StaticVariables.spinnerLayout.setVisibility(View.GONE);
			StaticVariables.headerLayout.setVisibility(View.VISIBLE);
			StaticVariables.footerLayout.setVisibility(View.VISIBLE);
			new LocationFeedAsync(getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else{
			StaticVariables.spinnerLayout.setVisibility(View.GONE);
			StaticVariables.headerLayout.setVisibility(View.VISIBLE);
			StaticVariables.footerLayout.setVisibility(View.VISIBLE);
			new LocationFeedAsync(getActivity()).execute();
		}
		
		return rootView;
	}

	public void markersOnMap(){
		try {
			MapsInitializer.initialize(getActivity().getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		GPSTracker gps = new GPSTracker(getActivity());
//
//		// check if GPS enabled
//		if (gps.canGetLocation()) {
//
//			curLat = gps.getLatitude();
//			curLong = gps.getLongitude();
//		}
//		else {
//			// can't get location
//			// GPS or Network is not enabled
//			// Ask user to enable GPS/network in settings
//			gps.showSettingsAlert();
//		}
		mMapView.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(GoogleMap arg0) {
				// TODO Auto-generated method stub
				googleMap = arg0;
				Log.w("map list size", mapList.size()+"");
				if(mapList.size()>0){

					MarkerOptions[] marker = new MarkerOptions[mapList.size()];
					 districtMarkerMap = new HashMap<Marker, MapContent>();
					for(int i=0;i<mapList.size();i++){
						Marker place_marker = placeMarker(mapList.get(i));
						districtMarkerMap.put(place_marker, mapList.get(i));
//						String pk=mapList.get(i).getPk();
//						String name=mapList.get(i).getName();
//						String pos=mapList.get(i).getPosition();
//						String distance=mapList.get(i).getDistance();
//
//						double lati=Double.parseDouble(pos.split(",")[0]);
//						double longi=Double.parseDouble(pos.split(",")[1]);
//						// create marker
//						marker[i] = new MarkerOptions().position(
//								new LatLng(lati, longi)).title(name).snippet(pk);
//						
//						// Changing marker icon
//						//marker[i].icon(BitmapDescriptorFactory.fromResource(R.drawable.local_1));
//
//
//						// adding marker
//						googleMap.addMarker(marker[i]);
						
					}
				}
				CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(StaticVariables.curLatitude, StaticVariables.curLongitude)).zoom(6).build();
				googleMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));

				// For showing a move to my loction button
				//googleMap.setMyLocationEnabled(true);

				mMapView.setClickable(true);
				mMapView.setFocusable(true);
				mMapView.setDuplicateParentStateEnabled(false);
				googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

					@Override
					public void onInfoWindowClick(Marker arg0) {
						// TODO Auto-generated method stub
						 MapContent mapInfo = districtMarkerMap.get(arg0);
//						String snippet=arg0.getSnippet();
//						String title=arg0.getTitle();
						//Log.w("Snippet + Title",snippet+"-"+title);
						MainActivity.fragment = new VenueFeedFragment();
						StaticVariables.map_district_id=mapInfo.getPk();
						MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_load, MainActivity.fragment).commit();

					}
				});


			}
		});
	}
	public Marker placeMarker(MapContent mapInfo) {
		String pk=mapInfo.getPk();
		String name=mapInfo.getName();
		String pos=mapInfo.getPosition();
		String distance=mapInfo.getDistance();
		double lati=Double.parseDouble(pos.split(",")[0]);
		double longi=Double.parseDouble(pos.split(",")[1]);
		
		  Marker m  = googleMap.addMarker(new MarkerOptions()

		   .position(new LatLng(lati, longi))

		   .title(mapInfo.getName()));

		  

		  return m;

		 }
	public class LocationFeedAsync extends AsyncTask<String, Void, String> {
		private Context context;
		public LocationFeedAsync(Context context) {
			this.context = context;
			StaticVariables.spinnerLayout.setVisibility(View.VISIBLE);
			StaticVariables.headerLayout.setVisibility(View.GONE);
			StaticVariables.footerLayout.setVisibility(View.GONE);
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
			String url = WebServicesLinks.location_feed+"33.3333,35.2345";
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
							MapContent mapCon=new MapContent();
							mapCon.setPk( dataIndex.getString("pk"));
							mapCon.setName(dataIndex.getString("name"));
							mapCon.setPosition(dataIndex.getString("position"));
							mapCon.setDistance(dataIndex.getString("distance"));
							mapList.add(mapCon);
						}
						Log.w("position name",mapList.get(0).getName()+"--"+mapList.get(1).getName());
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
			StaticVariables.spinnerLayout.setVisibility(View.GONE);
			StaticVariables.headerLayout.setVisibility(View.VISIBLE);
			StaticVariables.footerLayout.setVisibility(View.VISIBLE);
			markersOnMap();
			Log.w("result",result);

		}
	}

}
