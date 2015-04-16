package app.nightlife.fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import app.nightlife.adapters.EventAdapter;
import app.nightlife.contents.EventContent;
import app.nightlife.yago.MainActivity;
import app.nightlife.yago.R;
import app.nightlife.yago.R.id;
import app.nightlife.yago.R.layout;

public class EventsFragment extends Fragment {
	ListView eventsListView;
	ArrayList<EventContent> list;
	EventAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_events, container, false);
		eventsListView = (ListView) rootView.findViewById(R.id.events_list);
		TextView no_event=(TextView) rootView.findViewById(R.id.no_event);
		
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
		EventContent e1=new EventContent();
		e1.setEvent_id("1");
		e1.setEvent_image_url("https://s3.amazonaws.com/yago-bucket/uploads/venues/1.png");
		e1.setEvent_like_count("3");
		e1.setEvent_position("abc");
		e1.setEvent_thumbnail_url("");
		e1.setEvent_time_text("8hr");
		e1.setEvent_timestamp("08:08:2014");
		e1.setEvent_user("1");
		e1.setEvent_venue("HAVANA");
		list.add(e1);
		list.add(e1);
		list.add(e1);
		if(list.isEmpty()){
			no_event.setVisibility(View.VISIBLE);
			eventsListView.setVisibility(View.GONE);
		}
		adapter = new EventAdapter(getActivity(), list);
		eventsListView.setAdapter(adapter);
		
		return rootView;
	}
}
