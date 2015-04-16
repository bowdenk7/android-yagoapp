package app.nightlife.fragments;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import app.nightlife.yago.MainActivity;
import app.nightlife.yago.R;
import app.nightlife.yago.R.id;
import app.nightlife.yago.R.layout;

public class MapFragment extends Fragment {
	private MapView mMapView;

	private GoogleMap googleMap;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_map, container, false);
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

		mMapView.onResume(); 
		return rootView;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		try {
			MapsInitializer.initialize(getActivity().getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}

		mMapView.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(GoogleMap arg0) {
				// TODO Auto-generated method stub
				googleMap = arg0;

				// create marker
				MarkerOptions marker1 = new MarkerOptions().position(
						new LatLng(52.5167, 13.3833)).title("Berlin");

				// Changing marker icon
				//marker1.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));

				// adding marker
				googleMap.addMarker(marker1);
				CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(52.5167, 13.3833)).zoom(6).build();
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
						MainActivity.fragment = new VenueFeedFragment();
						MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_load, MainActivity.fragment).commit();

					}
				});


			}
		});
	}

}
