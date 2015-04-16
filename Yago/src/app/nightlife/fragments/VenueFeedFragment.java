package app.nightlife.fragments;

import android.app.Fragment;
import android.os.Bundle;
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
import app.nightlife.yago.MainActivity;
import app.nightlife.yago.R;
import app.nightlife.yago.R.id;
import app.nightlife.yago.R.layout;

public class VenueFeedFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_venue_feed, container, false);
		GridView gridView = (GridView)rootView.findViewById(R.id.gridview);
		ImageView back=(ImageView)getActivity().findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		TextView dynamic_text=(TextView)getActivity().findViewById(R.id.dynamic_text);
		dynamic_text.setText("Venue");
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
		
		gridView.setAdapter(new GridViewAdapter(getActivity()));
		gridView.setOnItemClickListener(new OnItemClickListener()
		{
		    @Override
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		    {
		    	MainActivity.fragment = new EventsFragment();
				MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_load, MainActivity.fragment).commit();
		    }
		});
		return rootView;
	}
	
}
