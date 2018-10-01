package me.youxiong.person.cache;

import me.youxiong.person.R;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class RelateViewItem extends BufCacheItem {

	public String keyName;
	public String query;
	private ViewHolder mViewHolder = null;

	public RelateViewItem(Context context) {
		super(context);
	}

	@Override
	public View getView(View convertView, int position) {
		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.relate_item, null);
			mViewHolder.mTextViewDesc = ((TextView) convertView.findViewById(R.id.keywordsTV));
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = ((ViewHolder) convertView.getTag());
		}
		String source = keyName.replace(query, "<font color='#0297ff'>" + query + "</font>");
		mViewHolder.mTextViewDesc.setText(Html.fromHtml(source));
		return convertView;
	}

	static class ViewHolder {
		public TextView mTextViewDesc;
	}

}
