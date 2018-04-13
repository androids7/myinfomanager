package cn.edu.info_manage.ui;

import java.util.List;

import cn.edu.info_manage.R;
import cn.edu.info_manage.domain.Contacts;
import cn.edu.info_manage.domain.Orders;
import cn.edu.info_manage.domain.User;
import cn.edu.info_manage.domain.WorkPlan;
import cn.edu.info_manage.util.ContactsService;
import cn.edu.info_manage.util.OrdersService;
import cn.edu.info_manage.util.UserService;
import cn.edu.info_manage.util.WorkplanService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class WorkplanActivity extends Activity implements OnItemClickListener{
	private static final String TAG = "OrderActivity";
	
	private User user_info;
	private ListView listView;
	private List<WorkPlan> infos;
	private WorkplanService wService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		int id = intent.getExtras().getInt("id",-1);
		String name = intent.getExtras().getString("name");
		if(id<0 && name==null){
			Toast.makeText(this, "请登录", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		setContentView(R.layout.ac_workplan);
		UserService userService = new UserService(this);
		user_info = userService.getUserInfoByID(id);
		userService.closeDB();
		
		listView = (ListView) findViewById(R.id.lv_workplan);
	}
	@Override
	protected void onResume() {
		super.onResume();
		wService = new WorkplanService(this);
		infos = wService.getAllWorkPlan(user_info.getId());
		WorkplanAdapter adapter = new WorkplanAdapter(this, infos);
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	public void btAdd(View view){
		Intent intent = new Intent();
		intent.setClass(this, ShowWorkplanActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("user_id", user_info.getId());
		
		intent.putExtras(bundle);
		startActivity(intent);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		intent.setClass(this, ShowWorkplanActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("user_id", user_info.getId());
		bundle.putInt("id", infos.get(position).getId());
		intent.putExtras(bundle);
		startActivity(intent);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(wService!=null)
			wService.closeDB();
	}
}
