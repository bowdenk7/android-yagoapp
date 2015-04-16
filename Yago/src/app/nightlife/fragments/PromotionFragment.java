package app.nightlife.fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import app.nightlife.adapters.PromotionAdapter;
import app.nightlife.contents.PromotionContent;
import app.nightlife.yago.MainActivity;
import app.nightlife.yago.R;
import app.nightlife.yago.R.id;
import app.nightlife.yago.R.layout;

public class PromotionFragment extends Fragment{
	ListView promotionsListView;
	ArrayList<PromotionContent> list;
	PromotionAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_promotion, container, false);
		
		promotionsListView = (ListView) rootView.findViewById(R.id.promotions_list);
		ImageView back=(ImageView)getActivity().findViewById(R.id.back);
		TextView dynamic_text=(TextView)getActivity().findViewById(R.id.dynamic_text);
		back.setVisibility(View.INVISIBLE);
		dynamic_text.setText("Promotion");
		
		ImageView profileImage=(ImageView)getActivity().findViewById(R.id.profile_icon);
		profileImage.setBackgroundResource(android.R.color.transparent); 
		ImageView yagoImage=(ImageView)getActivity().findViewById(R.id.yago_icon);
		yagoImage.setBackgroundResource(android.R.color.transparent);
		ImageView moneyImage=(ImageView)getActivity().findViewById(R.id.money_bag);
		moneyImage.setBackgroundResource(android.R.color.background_dark);
		
		list = new ArrayList<PromotionContent>();
		PromotionContent c1=new PromotionContent();
		c1.setPromotion_name("Promo 1");
		c1.setPromotion_venue("HAVANA");
		c1.setPromotion_value("20");
		list.add(c1);
		PromotionContent c2=new PromotionContent();
		c2.setPromotion_name("Promo 1");
		c2.setPromotion_venue("HAVANA");
		c2.setPromotion_value("20");
		list.add(c2);
		adapter = new PromotionAdapter(getActivity(), list);
		promotionsListView.setAdapter(adapter);
		
		promotionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			      Object listItem = promotionsListView.getItemAtPosition(position);
			      MainActivity.fragment = new PromotionDetailFragment();
				  MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_load, MainActivity.fragment).commit();
			   } 
		});
		return rootView;
	}

}
