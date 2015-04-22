package app.nightlife.yago;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;


import ch.boye.httpclientandroidlib.Header;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;



import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import app.nightlife.fragments.MapFragment;
import app.nightlife.fragments.ProfileFragment;
import app.nightlife.fragments.PromotionFragment;
import app.nightlife.fragments.VenueFeedFragment;
import app.nightlife.fragments.MapFragment.LocationFeedAsync;
import app.nightlife.utilities.WebServicesLinks;


@SuppressLint("NewApi") public class MainActivity extends FragmentActivity {
	public static Fragment fragment;
	public static android.app.FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	Uri curImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.header_content_footer_layout);
		fragmentManager = getFragmentManager();

		fragment = new MapFragment();
		fragmentManager.beginTransaction().replace(R.id.fragment_load, fragment).commit();

	}
	public void addPost(View view){
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);

	}
	// To handle when an image is selected from the browser, add the following to your Activity
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) { 

		if (resultCode == RESULT_OK) {

			if (requestCode == 1) {

				// currImageURI is the global variable I'm using to hold the content:// URI of the image
				curImg = data.getData();

				Toast.makeText(getApplicationContext(), curImg.toString(), Toast.LENGTH_LONG).show();
				if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
					new AddPostAsync(this,getPath(this, curImg)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				else{
					new AddPostAsync(this,getPath(this, curImg)).execute();
				}
				
			}
		}
	}

	// And to convert the image URI to the direct file system path of the image file
	public String getRealPathFromURI(Uri contentUri) {

		// can post image
		String [] proj={MediaStore.Images.Media.DATA};
		Cursor cursor = getContentResolver().query(contentUri,
				proj, // Which columns to return
				null,       // WHERE clause; which rows to return (all rows)
				null,       // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
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
	class AddPostAsync extends AsyncTask<String, Void, String> {

		private Context context;
		String filename;

		public AddPostAsync(Context context,String filename) {
			this.context = context;
			this.filename=filename;
		}

		@Override
		protected void onPreExecute() {
		}


		@Override
		protected String doInBackground(String... arg0) {

			String result="";
			try {
				result = postFile(filename, "12", "1");
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
			Intent intent=new Intent(context, AgeConfirmationActivity.class);
			startActivity(intent);
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
		builder.addTextBody("position", "23.1234,34.4567");
		builder.addTextBody("venue", "1");
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
	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @author paulburke
	 */
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] {
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
			String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

}