package cn.edu.info_manage.ui;

import java.util.List;

import cn.edu.info_manage.R;
import cn.edu.info_manage.domain.Memorandum;
import cn.edu.info_manage.domain.User;
import cn.edu.info_manage.util.MemorandumService;
import cn.edu.info_manage.util.UserService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MemorandumActivity extends Activity implements OnItemClickListener{

	private static final String TAG = "BusinessManager";
	
	private User user_info;
	private MemorandumService mService;
	private ListView listView;
	private List<Memorandum> infos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		int id = intent.getExtras().getInt("id",-1);
		if(id<0){
			Toast.makeText(this, "请登录", Toast.LENGTH_LONG).show();
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		setContentView(R.layout.ac_memorandum);
		
		UserService userService = new UserService(this);
		user_info = userService.getUserInfoByID(id);
		userService.closeDB();
		
		listView = (ListView) findViewById(R.id.lv_infos);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mService = new MemorandumService(this);
		infos = mService.slectAllInfo(user_info.getId());
		MenorandumAdapter adapter = new MenorandumAdapter(this, infos);
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	
	View iview;
	public void btAdd(View view){
		Intent intent = new Intent();
		intent.setClass(this, ShowMemorandumActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt("user_id", user_info.getId());
		
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mService!=null)
			mService.closeDB();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		intent.setClass(this, ShowMemorandumActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt("user_id", user_info.getId());
		bundle.putInt("id", infos.get(position).getId());
		
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
}
