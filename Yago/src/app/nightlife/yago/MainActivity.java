package app.nightlife.yago;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.view.WindowManager;

public class MainActivity extends Activity {
		EventsFragment events_fragment;
		MapFragment map_fragment;
		PromotionFragment promotion_fragment;
		PromotionDetailFragment promotion_detail_fragment;
		VenueFeedFragment venue_feed_fragment;
		VenueStaffFragment venue_staff_fragment;
	   @Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.header_content_footer_layout);
//	      FragmentManager fragmentManager = getFragmentManager();
//	      FragmentTransaction fragmentTransaction = 
//	      fragmentManager.beginTransaction();
//	      MapFragment ls_fragment = new MapFragment();
//	      fragmentTransaction.replace(R.id.fragment_load, ls_fragment);
//	      
//	      fragmentTransaction.commit();
	   }
	    
}