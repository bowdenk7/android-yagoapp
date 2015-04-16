package app.nightlife.yago;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AgeConfirmationActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_age_confirm);
		
	}
	public void goToMainActivity(View view){
		Intent intent=new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
