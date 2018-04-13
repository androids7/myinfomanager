package cn.edu.info_manage.ui;
import java.util.List;

import cn.edu.info_manage.R;
import cn.edu.info_manage.domain.Contacts;
import cn.edu.info_manage.domain.Orders;
import cn.edu.info_manage.domain.User;
import cn.edu.info_manage.util.ContactsService;
import cn.edu.info_manage.util.OrdersService;
import cn.edu.info_manage.util.UserService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MyActivity extends Activity implements OnItemClickListener{
	private static final String TAG = "MyActivity";

	private User user_info;
	private ListView listView;
	private List<Orders> infos;
	private OrdersService oService;
	
	int isF;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		int id = intent.getExtras().getInt("id",-1);
		String name = intent.getExtras().getString("name");
		
		isF=intent.getExtras().getInt("isF",-1);
		/*
		if(id<0 && name==null){
			Toast.makeText(this, "请登录", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		*/
		setContentView(R.layout.my_orders);
		UserService userService = new UserService(this);
		user_info = userService.getUserInfoByID(id);
		userService.closeDB();

		listView = (ListView) findViewById(R.id.my_contacts);
	}
	@Override
	protected void onResume() {
		super.onResume();
		oService = new OrdersService(this);
		infos = oService.getAllOrders(user_info.getId());
		OrdersAdapter adapter = new OrdersAdapter(this, infos,isF);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	/*
	public void btAdd(View view){
		Intent intent = new Intent();
		intent.setClass(this, ShowOrdersActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("user_id", user_info.getId());

		intent.putExtras(bundle);
		startActivity(intent);
	}
	*/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		Intent intent = new Intent();
		intent.setClass(this, ShowOrdersActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("user_id", user_info.getId());
		bundle.putInt("id", infos.get(position).getId());
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(oService!=null)
			oService.closeDB();
	}
}
