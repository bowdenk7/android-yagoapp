package app.nightlife.utilities;

import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;



@SuppressWarnings("deprecation")
public class WebServicesLinks {
	//server root
	public static String domain = "https://yago-stage.herokuapp.com";
	
	//post call
	public static String authenticate_with_token = domain + "/account/authenticate_with_token/";
	
	//get call
	public static String location_feed=domain+"/feed/location_feed/";
	
	//get call
	public static String top_district_feed=domain+"/feed/top_district_feed/";
	
	//get call
	public static String recent_district_posts=domain+"/post/recent_district_posts/";
	
	//post call
	public static String like_post=domain+"/post/like_post/";
			
	//get call
	public static String closest_venues=domain+"/post/closest_venues/";
	
	//post call
	public static String post=domain+"/post/";
		
	//get call
	public static String users=domain+"/users/";
	
	//get call
	public static String promotion_type_feed=domain+"/promotion/promotion_type_feed/";
	
	//post call
	public static String purchase_and_redeem=domain+"/promotion/purchase_and_redeem/";
	
	public static DefaultHttpClient httpClient;
	
			
}
