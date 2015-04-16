package app.nightlife.yago;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;
import app.nightlife.fragments.MapFragment;
import app.nightlife.fragments.ProfileFragment;
import app.nightlife.fragments.PromotionFragment;
import app.nightlife.fragments.VenueFeedFragment;

public class MainActivity extends FragmentActivity {
	   public static Fragment fragment;
	   public static android.app.FragmentManager fragmentManager;
	   FragmentTransaction fragmentTransaction;
	   @Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.header_content_footer_layout);
	      fragmentManager = getFragmentManager();
	      
	      fragment = new MapFragment();
	      fragmentManager.beginTransaction().replace(R.id.fragment_load, fragment).commit();
	      
	   }
	   public void goToProfileFragment(View view){
		   fragment = new ProfileFragment();
		   fragmentManager.beginTransaction().replace(R.id.fragment_load, fragment).commit();
		   
	   }
	   public void goToVenueFragment(View view){
		   fragment = new VenueFeedFragment();
		   fragmentManager.beginTransaction().replace(R.id.fragment_load, fragment).commit();
		   
	   }
	   public void goToPromotionFragment(View view){
		   fragment = new PromotionFragment();
		   fragmentManager.beginTransaction().replace(R.id.fragment_load, fragment).commit();
		   
	   }
}