package app.nightlife.fragments;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
				MainActivity.fragment = new VenueFeedFragment();
				MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_load, MainActivity.fragment).commit();
			}
		});
		return rootView;
	}
	
}
