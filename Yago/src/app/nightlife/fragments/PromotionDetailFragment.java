package app.nightlife.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import app.nightlife.contents.StaticVariables;
import app.nightlife.yago.MainActivity;
import app.nightlife.yago.R;
import app.nightlife.yago.R.id;
import app.nightlife.yago.R.layout;

public class PromotionDetailFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_promotion_detail, container, false);

		TextView dynamic_text=(TextView)getActivity().findViewById(R.id.dynamic_text);
		dynamic_text.setText("Detail");
		ImageView back=(ImageView)getActivity().findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		ImageView promo_infront_of_staff=(ImageView) rootView.findViewById(R.id.promo_infront_of_staff);
		promo_infront_of_staff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.fragment = new VenueStaffFragment();
				MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_load, MainActivity.fragment).commit();
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.fragment = new PromotionFragment();
				MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_load, MainActivity.fragment).commit();
			}
		}); 
		ImageView profileImage=(ImageView)getActivity().findViewById(R.id.profile_icon);
		profileImage.setBackgroundResource(android.R.color.transparent); 
		ImageView yagoImage=(ImageView)getActivity().findViewById(R.id.yago_icon);
		yagoImage.setBackgroundResource(android.R.color.transparent);
		ImageView moneyImage=(ImageView)getActivity().findViewById(R.id.money_bag);
		moneyImage.setBackgroundResource(android.R.color.background_dark);
		
		TextView total_points=(TextView) rootView.findViewById(R.id.total_points);
		TextView promo_venue=(TextView) rootView.findViewById(R.id.promotion_venue);
		TextView promo_name=(TextView) rootView.findViewById(R.id.promotion_name);
		TextView promo_value=(TextView) rootView.findViewById(R.id.promotion_value);
		total_points.setText(Integer.parseInt(StaticVariables.current_points)-Integer.parseInt(StaticVariables.selected_promotion.getPromotion_value())+"");
		promo_venue.setText(StaticVariables.selected_promotion.getPromotion_venue());
		promo_name.setText(StaticVariables.selected_promotion.getPromotion_name());
		promo_value.setText(StaticVariables.selected_promotion.getPromotion_value());
		return rootView;
	}


}