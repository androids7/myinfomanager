package cn.edu.info_manage.ui;

import java.text.DateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.edu.info_manage.R;
import cn.edu.info_manage.domain.Contacts;
import cn.edu.info_manage.domain.Orders;

public class OrdersAdapter extends BaseAdapter{

	private Context mContext;
	private List<Orders> infos;
	private LayoutInflater inflater;
	private DateFormat timeFormat = DateFormat.getDateTimeInstance(); 
	int isF;
	public OrdersAdapter(Context context, List<Orders> infos,int isF) {
		this.inflater = LayoutInflater.from(context);
		this.mContext = context;
		this.infos = infos;
		this.isF=isF;
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
		
		/*
		if(isF==0)
		{
			Orders info = infos.get(position);
			if(info.getIn_out()=="收入"){
				
				
				
				View itemv = inflater.inflate(R.layout.lv_order_item, null);
				TextView tv_use = (TextView) itemv.findViewById(R.id.order_use);
				TextView tv_money = (TextView) itemv.findViewById(R.id.order_money);
				TextView tv_in_out = (TextView) itemv.findViewById(R.id.order_in_out);
				TextView tv_date = (TextView) itemv.findViewById(R.id.order_date);
				
				tv_use.setText(info.getUse());
				tv_money.setText(info.getMoney());


				tv_in_out.setText(info.getIn_out());
				tv_date.setText(info.getDate());

				return itemv;
			}
			else{
				return null;
			}
		}
		else{
			
			
			Orders info = infos.get(position);
			if(info.getIn_out()=="支出"){



				View itemv = inflater.inflate(R.layout.lv_order_item, null);
				TextView tv_use = (TextView) itemv.findViewById(R.id.order_use);
				TextView tv_money = (TextView) itemv.findViewById(R.id.order_money);
				TextView tv_in_out = (TextView) itemv.findViewById(R.id.order_in_out);
				TextView tv_date = (TextView) itemv.findViewById(R.id.order_date);

				tv_use.setText(info.getUse());
				tv_money.setText(info.getMoney());


				tv_in_out.setText(info.getIn_out());
				tv_date.setText(info.getDate());

				return itemv;
			}
			else{
				return null;
			}
			
			
			}
			*/
			
			
		Orders info = infos.get(position);
	



			View itemv = inflater.inflate(R.layout.lv_order_item, null);
			TextView tv_use = (TextView) itemv.findViewById(R.id.order_use);
			TextView tv_money = (TextView) itemv.findViewById(R.id.order_money);
			TextView tv_in_out = (TextView) itemv.findViewById(R.id.order_in_out);
			TextView tv_date = (TextView) itemv.findViewById(R.id.order_date);

			tv_use.setText(info.getUse());
			tv_money.setText(info.getMoney());


			tv_in_out.setText(info.getIn_out());
			tv_date.setText(info.getDate());

			return itemv;
			
		
		
		
		
	}

}
