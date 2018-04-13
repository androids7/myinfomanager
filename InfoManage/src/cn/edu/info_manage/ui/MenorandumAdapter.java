/**
 * 
 */
package cn.edu.info_manage.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.info_manage.R;
import cn.edu.info_manage.domain.Memorandum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @date 2012-5-2
 * @time 下午04:39:22
 */
public class MenorandumAdapter extends BaseAdapter{

	private Context mContext;
	private List<Memorandum> infos;
	private LayoutInflater inflater;
	private DateFormat timeFormat = DateFormat.getDateTimeInstance(); 
	
	public MenorandumAdapter(Context context, List<Memorandum> infos) {
		this.inflater = LayoutInflater.from(context);
		this.mContext = context;
		this.infos = infos;
	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		
		View itemv = inflater.inflate(R.layout.lv_meme_item, null);
		TextView tv_summary = (TextView) itemv.findViewById(R.id.item_summary);
		TextView tv_discreption = (TextView) itemv.findViewById(R.id.item_discreption);
		TextView tv_date = (TextView) itemv.findViewById(R.id.item_date);
		TextView tv_category = (TextView) itemv.findViewById(R.id.item_categray);
		
		Memorandum info = infos.get(position);
		tv_summary.setText(info.getSummary());
		tv_discreption.setText(info.getDescription());
		long datel = Long.parseLong(info.getDate());
		tv_date.setText(timeFormat.format(new Date(datel)));
		String c = "普通";
		switch (info.getCategory()) {
		case 0:
			c="困难";
			break;
		case 1:
			c="普通";
			break;
		case 2:
			c="简单";
			break;
		default:
			break;
		}
		tv_category.setText(c);
		return itemv;
	}

}
