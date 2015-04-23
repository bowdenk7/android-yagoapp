package app.nightlife.yago;

import static app.nightlife.utilities.CameraHelper.cameraAvailable;
import static app.nightlife.utilities.CameraHelper.getCameraInstance;
import static app.nightlife.utilities.MediaHelper.getOutputMediaFile;
import static app.nightlife.utilities.MediaHelper.saveToFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.boye.httpclientandroidlib.Header;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import app.nightlife.views.FromXML;

import app.nightlife.views.CameraPreview;
import app.nightlife.yago.MainActivity.AddPostAsync;
import app.nightlife.contents.EventContent;
import app.nightlife.contents.StaticVariables;

import app.nightlife.utilities.GPSTracker;
import app.nightlife.utilities.JsonParser;
import app.nightlife.utilities.WebServicesLinks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Takes a photo saves it to the SD card and returns the path of this photo to the calling Activity
 * @author paul.blundell
 *
 */
@SuppressWarnings("deprecation")
public class CameraActivity extends Activity implements PictureCallback {
	double curLat, curLong;
	protected static final String EXTRA_IMAGE_PATH = "com.blundell.tut.cameraoverlay.ui.CameraActivity.EXTRA_IMAGE_PATH";
	public static ArrayAdapter<String> adp1;
	private Camera camera;
	private CameraPreview cameraPreview;
	ArrayList<String> strings;
	ArrayList<String> keys;
	String selected_venue_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		GPSTracker gps = new GPSTracker(this);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			curLat = gps.getLatitude();
			curLong = gps.getLongitude();
		}
		else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}
		
		strings = new ArrayList<String>();
		keys= new ArrayList<String>();
		Spinner spinner = (Spinner)findViewById(R.id.event_names);
		adp1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strings);
		adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adp1);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				selected_venue_id=keys.get(position);
				Log.w("selected", selected_venue_id);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		setResult(RESULT_CANCELED);
		// Camera may be in use by another activity or the system or not available at all
		camera = getCameraInstance();
		if(cameraAvailable(camera)){
			initCameraPreview();
		} else {
			finish();
		}
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			
			new ClosestDistrictPostsAsync(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else{
			
			new ClosestDistrictPostsAsync(this).execute();
		}
	}

	// Show the camera view on the activity
	private void initCameraPreview() {
		cameraPreview = (CameraPreview) findViewById(R.id.camera_preview);
		cameraPreview.init(camera);
	}

	@FromXML
	public void onCaptureClick(View button){
		// Take a picture with a callback when the photo has been created
		// Here you can add callbacks if you want to give feedback when the picture is being taken
		camera.takePicture(null, null, this);
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		Log.w("Picture taken","Picture taken");
		String path = savePictureToFileSystem(data);
//		setResult(path);
//		finish();
		String position=curLat+","+curLong;
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			
			new AddPostAsync(this,path,selected_venue_id,position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else{
			new AddPostAsync(this,path,selected_venue_id,position).execute();
		}
		
	}

	private static String savePictureToFileSystem(byte[] data) {
		File file = getOutputMediaFile();
		saveToFile(data, file);
		return file.getAbsolutePath();
	}

	private void setResult(String path) {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_IMAGE_PATH, path);
		setResult(RESULT_OK, intent);
	}

	// ALWAYS remember to release the camera when you are finished
	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera();
	}

	private void releaseCamera() {
		if(camera != null){
			camera.release();
			camera = null;
		}
	}
	class AddPostAsync extends AsyncTask<String, Void, String> {

		private Context context;
		String filename;
		String venue_id;
		String position;
		public AddPostAsync(Context context,String filename, String venue_id, String position) {
			this.context = context;
			this.filename=filename;
			this.venue_id=venue_id;
			this.position=position;
			
		}

		@Override
		protected void onPreExecute() {
		}


		@Override
		protected String doInBackground(String... arg0) {

			String result="";
			try {
				result = postFile(filename, position, venue_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(String result) {
			Log.w("result1",result);
			
			finish();
		} 
	}
	public static String postFile(String fileName, String position, String venue) throws Exception {

		// HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(WebServicesLinks.post);
		Log.w("before Multipart","Yes");
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();        
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		Log.w("before File","Yes");
		final File file = new File(fileName);
		FileBody fb = new FileBody(file);

		Log.w("Afetr File",fb.toString());
		builder.addTextBody("position", position);
		builder.addTextBody("venue", venue);
		builder.addPart("image", fb);  


		Log.w("After Adding parameters","Yes");
		final HttpEntity yourEntity = builder.build();
		Log.w("After Building","Yes");
		class ProgressiveEntity implements HttpEntity {
			@Override
			public void consumeContent() throws IOException {
				yourEntity.consumeContent();                
			}
			@Override
			public InputStream getContent() throws IOException,
			IllegalStateException {
				return yourEntity.getContent();
			}
			@Override
			public Header getContentEncoding() {             
				return yourEntity.getContentEncoding();
			}
			@Override
			public long getContentLength() {
				return yourEntity.getContentLength();
			}
			@Override
			public Header getContentType() {
				return yourEntity.getContentType();
			}
			@Override
			public boolean isChunked() {             
				return yourEntity.isChunked();
			}
			@Override
			public boolean isRepeatable() {
				return yourEntity.isRepeatable();
			}
			@Override
			public boolean isStreaming() {             
				return yourEntity.isStreaming();
			} // CONSIDER put a _real_ delegator into here!

			@Override
			public void writeTo(OutputStream outstream) throws IOException {

				class ProxyOutputStream extends FilterOutputStream {
					/**
					 * @author Stephen Colebourne
					 */

					public ProxyOutputStream(OutputStream proxy) {
						super(proxy);    
					}
					public void write(int idx) throws IOException {
						out.write(idx);
					}
					public void write(byte[] bts) throws IOException {
						out.write(bts);
					}
					public void write(byte[] bts, int st, int end) throws IOException {
						out.write(bts, st, end);
					}
					public void flush() throws IOException {
						out.flush();
					}
					public void close() throws IOException {
						out.close();
					}
				} // CONSIDER import this class (and risk more Jar File Hell)

				class ProgressiveOutputStream extends ProxyOutputStream {
					public ProgressiveOutputStream(OutputStream proxy) {
						super(proxy);
					}
					public void write(byte[] bts, int st, int end) throws IOException {

						// FIXME  Put your progress bar stuff here!

						out.write(bts, st, end);
					}
				}

				yourEntity.writeTo(new ProgressiveOutputStream(outstream));
			}

		};
		ProgressiveEntity myEntity = new ProgressiveEntity();
		Log.w("before Set ENtity","Yes");
		post.setEntity(myEntity);
		Log.w("before Set Http","Yes");
		HttpResponse response = WebServicesLinks.httpClient.execute(post);        

		HttpEntity entity1 = response.getEntity();


		InputStream is = entity1.getContent();

		String json = IOUtils.toString(is, "UTF-8");
		return json;

	} 

	public static String getContent(HttpResponse response) throws IOException {

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String body = "";
		String content = "";

		while ((body = rd.readLine()) != null) 
		{
			content += body + "\n";
		}
		Log.w("content", content.trim());
		return content;
	}
	
	//********************Get Closest Venues***********************//
	public class ClosestDistrictPostsAsync extends AsyncTask<String, Void, String> {
		private String TAG_APPLICATION="application";
		Context context;
		public ClosestDistrictPostsAsync(Context context) {
			this.context=context;
		}
		@Override
		public void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		public String doInBackground(String... arg0) {
			/*
			 * Will make http call here This call will download required data
			 */
			Log.i("Response: ", "Start background");

//			String url = WebServicesLinks.closest_venues+curLat+","+curLong;
			String url = WebServicesLinks.closest_venues+"23.1234,34.2345";
			//			url=url.replace("'", "");
			//			url = url.replace(" ", "%20");
			//			url=url.replace("\n", "%0A");

			JsonParser jsonParser = new JsonParser();
			String json = jsonParser
					.getJSONFromUrl(url);
			Log.w("URL ",url);
			//Log.i("Response: ", "after calling json ");
			//Log.i("Response: ", "> " + json);
			if (json != null) {
				try {
					//Log.w("Here","1");
					JSONArray dataArray = new JSONArray(json);
					Log.w("length",dataArray.length()+"");
					if(dataArray.length()>0){
						strings.clear();
						keys.clear();
						for(int i=0; i<dataArray.length();i++){
							JSONObject dataIndex = dataArray.getJSONObject(i);
							strings.add(dataIndex.getString("name"));
							keys.add(dataIndex.getString("pk"));
						}

					}
					else{

					}
				}
				catch(JSONException e){
					e.getStackTrace();
				}
			}

			return json;
		}

		@SuppressLint("ShowToast")
		@Override
		public void onPostExecute(String result) {
			super.onPostExecute(result);
			adp1.notifyDataSetChanged();
		}
	}
}
