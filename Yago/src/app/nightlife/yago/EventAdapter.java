package app.nightlife.yago;
 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class EventAdapter extends BaseAdapter {
    private Activity mContext;
    private List<EventContent> mEc;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public EventAdapter(Activity context, List<EventContent> ec) {
        mContext = context;
        mEc = ec;
    }
 
    @Override
    public int getCount() {
        return mEc.size();
    }
 
    @Override
    public Object getItem(int pos) {
        return mEc.get(pos);
    }
 
    @Override
    public long getItemId(int pos) {
        return pos;
    }
    
 
	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
    public View getView(int pos, View convertView, ViewGroup parent) {
    	EventContent ec = mEc.get(pos);
		if(convertView == null)
		{
	    	LayoutInflater inflater = LayoutInflater.from(mContext);
	    	
	    	convertView = inflater.inflate(R.layout.event_item, null);
		}
    	TextView eventTime = (TextView)convertView.findViewById(R.id.time);
        eventTime.setText(ec.getEvent_time_text());

    	TextView eventLikes = (TextView)convertView.findViewById(R.id.likes);
        eventTime.setText(ec.getEvent_like_count());
        
        NetworkImageView event_image = (NetworkImageView) convertView
				.findViewById(R.id.event_image);
    	event_image.setImageUrl(ec.getEvent_image_url(), imageLoader);
        return convertView;
    }
 
}