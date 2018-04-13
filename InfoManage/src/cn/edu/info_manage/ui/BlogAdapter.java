package cn.edu.info_manage.ui;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.info_manage.R;
import cn.edu.info_manage.domain.Blog;
import cn.edu.info_manage.domain.Contacts;
import cn.edu.info_manage.domain.Orders;

public class BlogAdapter extends BaseAdapter{

	private Context mContext;
	private List<Blog> infos;
	private LayoutInflater inflater;
	private DateFormat timeFormat = DateFormat.getDateTimeInstance(); 
	
	public BlogAdapter(Context context, List<Blog> infos) {
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
		
		View itemv = inflater.inflate(R.layout.lv_blog_item, null);
		TextView tv_use = (TextView) itemv.findViewById(R.id.order_use);
		TextView tv_money = (TextView) itemv.findViewById(R.id.order_money);
		TextView tv_in_out = (TextView) itemv.findViewById(R.id.order_in_out);
		ImageView iv_image = (ImageView) itemv.findViewById(R.id.blog_image);
		
		Blog info = infos.get(position);
		
		String date = info.getDate();
		if(null==date||"".equals(date))
			date=0+"";
		
		if(!"".equals(info.getWorld())){
			iv_image.setVisibility(View.GONE);
			tv_money.setVisibility(View.VISIBLE);
			tv_use.setText(info.getSummary());
			tv_money.setText(info.getWorld());
		}else if(!"".equals(info.getPoto())){
			iv_image.setBackgroundResource(R.drawable.ic_camera);
			iv_image.setVisibility(View.VISIBLE);
			tv_money.setVisibility(View.GONE);
		}else if(!"".equals(info.getVideo())){
			iv_image.setBackgroundResource(R.drawable.ic_video_camera);
			tv_money.setVisibility(View.GONE);
			iv_image.setVisibility(View.VISIBLE);
		}else if(!"".equals(info.getAudio())){
			iv_image.setBackgroundResource(R.drawable.ic_soundrecorder);
			tv_money.setVisibility(View.GONE);
			iv_image.setVisibility(View.VISIBLE);
		}
		
		tv_in_out.setText(timeFormat.format(new Date(Long.parseLong(date))));
		
		return itemv;
	}

}