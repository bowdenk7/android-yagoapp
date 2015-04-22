package app.nightlife.adapters;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import app.nightlife.contents.VenueContent;
import app.nightlife.utilities.AppController;
import app.nightlife.yago.R;
import app.nightlife.yago.R.drawable;
import app.nightlife.yago.R.id;
import app.nightlife.yago.R.layout;

public class GridViewAdapter extends BaseAdapter {
	private List<VenueContent> mItems = new ArrayList<VenueContent>();
	private final LayoutInflater mInflater;
	ImageLoader imageLoader;
	public GridViewAdapter(Context context, List<VenueContent> vList) {
		mInflater = LayoutInflater.from(context);
		mItems=vList;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public VenueContent getItem(int i) {
		return mItems.get(i);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		View v = view;
		NetworkImageView event_image;
		TextView name;

		if (v == null) {
			v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
			v.setTag(R.id.picture, v.findViewById(R.id.picture));
			v.setTag(R.id.text, v.findViewById(R.id.text));
		}
		if (imageLoader == null){
			imageLoader = AppController.getInstance().getImageLoader();
		}
		event_image = (NetworkImageView) v
				.findViewById(R.id.picture);

		name = (TextView) v.getTag(R.id.text);

		VenueContent item = getItem(i);
		event_image.setImageUrl(item.getLogo_url(), imageLoader);
		name.setText(item.getName());

		return v;
	}

	private static class Item {
		public final String name;
		public final int drawableId;

		Item(String name, int drawableId) {
			this.name = name;
			this.drawableId = drawableId;
		}
	}
}