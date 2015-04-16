package app.nightlife.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
		return rootView;
	}
	
}

