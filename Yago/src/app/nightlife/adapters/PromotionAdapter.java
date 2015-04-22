package app.nightlife.adapters;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import app.nightlife.contents.PromotionContent;
import app.nightlife.yago.R;
import app.nightlife.yago.R.id;
import app.nightlife.yago.R.layout;

public class PromotionAdapter extends BaseAdapter{
	private Activity mContext;
	private List<PromotionContent> mPc;
	public PromotionAdapter(Activity context, List<PromotionContent> pc) {
		mContext = context;
		mPc = pc;
	}

	@Override
	public int getCount() {
		return mPc.size();
	}

	@Override
	public Object getItem(int pos) {
		return mPc.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}


	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		PromotionContent pc = mPc.get(pos);
		if(convertView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(mContext);

			convertView = inflater.inflate(R.layout.promotion_item, null);
		}
		TextView promotionName = (TextView)convertView.findViewById(R.id.promotion_name);
		promotionName.setText(pc.getPromotion_name());

		TextView promotionVenue = (TextView)convertView.findViewById(R.id.promotion_venue);
		promotionVenue.setText(pc.getPromotion_venue());

		TextView promotionValue = (TextView)convertView.findViewById(R.id.promotion_value);
		promotionValue.setText(pc.getPromotion_value());

		return convertView;
	}
}
