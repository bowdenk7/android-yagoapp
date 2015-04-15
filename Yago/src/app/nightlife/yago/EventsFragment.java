package app.nightlife.yago;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class EventsFragment extends Fragment {
	ListView eventsListView;
	ArrayList<EventContent> list;
	EventAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_events, container, false);
		eventsListView = (ListView) rootView.findViewById(R.id.events_list);

		list = new ArrayList<EventContent>();

		adapter = new EventAdapter(getActivity(), list);
		eventsListView.setAdapter(adapter);
		return rootView;
	}
}
