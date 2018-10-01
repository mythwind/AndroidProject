package me.youxiong.person.adapter;

import me.youxiong.person.R;
import me.youxiong.person.adapter.base.MyBaseAdapter;
import me.youxiong.person.database.TradeDAO;
import me.youxiong.person.database.impl.TradeDAOImpl;
import me.youxiong.person.model.PersionBriefInfo;
import me.youxiong.person.utils.FileUtils;
import me.youxiong.person.utils.LoadImageUtils;
import me.youxiong.person.utils.Logger;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 主界面的适配器，需要有排序的操作
 * @author mythwind
 */
public class AdapterMain extends MyBaseAdapter {

	private TradeDAO mTradeDAO;
	private String mDBPath;
	
	public AdapterMain(Context context) {
		super(context);
		
		mTradeDAO = new TradeDAOImpl();
		mDBPath = FileUtils.getDatabaseTradePath(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler;
		
		if(convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.item_main_listview, null);
			viewHodler = new ViewHodler();
			viewHodler.icon = (ImageView) convertView.findViewById(R.id.main_list_item_icon);
			viewHodler.name = (TextView) convertView.findViewById(R.id.main_list_item_name);
			viewHodler.title = (TextView) convertView.findViewById(R.id.main_list_item_title);
			
			viewHodler.distance = (TextView) convertView.findViewById(R.id.main_list_item_distance);
			viewHodler.facevalue = (TextView) convertView.findViewById(R.id.main_list_item_facevalue);
			viewHodler.trades = (TextView) convertView.findViewById(R.id.main_list_item_trades);
			// viewHodler.cardTitle = (TextView) convertView.findViewById(R.id.main_list_item_card_title);
			convertView.setTag(viewHodler);
			
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		PersionBriefInfo briefInfo = (PersionBriefInfo) getItem(position);
		if(null != briefInfo) {
			viewHodler.name.setText(briefInfo.getReal_name());
			viewHodler.title.setText(briefInfo.getCard_title());
			viewHodler.distance.setText(briefInfo.getDistance());
			String facevalue = getContext().getString(R.string.person_face_value, briefInfo.getFace_value_real());
			viewHodler.facevalue.setText(facevalue);
			viewHodler.trades.setText(briefInfo.getProfession());
			
			Logger.e("photoURL = " + briefInfo.getPhoto());
			
			if(!TextUtils.isEmpty(briefInfo.getPhoto())) {
				LoadImageUtils.displayImage(briefInfo.getPhoto(), viewHodler.icon);
			}
		}
		return convertView;
	}
	
	// 这个数值是 KM
//	private String handleDistance(float distance) {
//		if(distance < 1) {
//			int d = (int) (distance * 1000);
//			if(d < 100)
//				return "100m";
//			return d + "m";
//		} else if(distance < 1000) {
//			DecimalFormat df = new DecimalFormat("#.00");
//			return df.format(distance) + "km";
//		}
//		return (int) distance + "km";
//	}
	// 处理职业身份的功能
//	private String handleTradeId(String trade_id) {
//		String[] tradeIds = trade_id.split(",");
//		List<BaseDaoModel> result = mTradeDAO.getTradeDetailByIds(tradeIds, mDBPath);
//		if(null == result || result.size() == 0) {
//			return "";
//		}
//		StringBuilder sb = new StringBuilder();
//		for (BaseDaoModel baseDaoModel : result) {
//			sb.append(baseDaoModel.getName());
//			sb.append("\t\t");
//		}
//		return sb.toString();
//	}
	
//	public void notifyDataChanged() {
//		SortComparator comparator = new SortComparator();
//		@SuppressWarnings("unchecked")
//		List<PersionBriefInfo> list = (List<PersionBriefInfo>) getList();
//		Logger.d("---------before------list=" + list);
//		if(null != list && 0 < list.size()) {
//			Collections.sort(list, comparator);
//		}
//		setList(list);
//		Logger.d("---------after------list=" + list);
//		
//		notifyDataSetChanged();
//	}
//	
//	public class SortComparator implements Comparator<PersionDetailInfo> {
//		@Override
//		public int compare(PersionDetailInfo lhs, PersionDetailInfo rhs) {
//			if(mSortType == SortType.DISTANCE) {
//				// 从小到大排序
//				float result = lhs.getDistance() - rhs.getDistance();
//				if(result > 0)
//					return 1;
//				if(result < 0)
//					return -1;
//				return 0;
//			} else if(mSortType == SortType.CREDIT_TOTAL) {
//				// 从高到低排序
//				int result = lhs.getCredit_total() - rhs.getCredit_total();
//				if(result > 0)
//					return -1;
//				if(result < 0)
//					return 1;
//				return 0;
//				
//			} else if(mSortType == SortType.PROFESSION_TOTAL) {
//				// 从高到低排序
//				int result = lhs.getProfession_total() - rhs.getProfession_total();
//				if(result > 0)
//					return -1;
//				if(result < 0)
//					return 1;
//				return 0;
//			}
//			return 0;
//		}  
//	}
	
	private static final class ViewHodler {
		ImageView icon;
		TextView name;
		TextView title;
		TextView distance;
		TextView facevalue;
		TextView trades;
		// TextView cardTitle;
	}
}
