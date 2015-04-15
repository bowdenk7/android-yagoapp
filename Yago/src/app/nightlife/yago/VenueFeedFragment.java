package app.nightlife.yago;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class VenueFeedFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_venue_feed, container, false);
		 GridView gridView = (GridView)rootView.findViewById(R.id.gridview);
		 gridView.setAdapter(new GridViewAdapter(getActivity()));
		return rootView;
	}
	
}
