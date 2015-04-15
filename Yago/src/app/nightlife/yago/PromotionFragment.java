package app.nightlife.yago;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PromotionFragment extends Fragment{
	ListView promotionsListView;
	ArrayList<PromotionContent> list;
	PromotionAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_promotion, container, false);
		promotionsListView = (ListView) rootView.findViewById(R.id.promotions_list);

		list = new ArrayList<PromotionContent>();

		adapter = new PromotionAdapter(getActivity(), list);
		promotionsListView.setAdapter(adapter);
		return rootView;
	}

}
