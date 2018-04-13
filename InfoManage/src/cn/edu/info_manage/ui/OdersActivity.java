package cn.edu.info_manage.ui;
import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import cn.edu.info_manage.util.*;
import cn.edu.info_manage.domain.*;
import cn.edu.info_manage.R;
import android.view.*;
import android.view.View.*;

public class OdersActivity extends Activity implements View.OnClickListener
{
	private static final String TAG = "OrdersActivity";

	private User user_info;
	
	private Button shouru,zhichu,yue,add;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		int id = intent.getExtras().getInt("id",-1);
		String name = intent.getExtras().getString("name");
		if(id<0 && name==null){
			Toast.makeText(this, "请登录", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		setContentView(R.layout.oders);
		UserService userService = new UserService(this);
		user_info = userService.getUserInfoByID(id);
		
		
		user_info = userService.getUserInfoByID(id);

		if(user_info==null){
			user_info = userService.getUserInfoByName(name);
		}
		
		userService.closeDB();
		
		shouru=(Button)findViewById(R.id.odersButtonshouru);
	    zhichu=(Button)findViewById(R.id.odersButtonzhichu);
		yue=(Button)findViewById(R.id.odersButtonyue);
		add=(Button)findViewById(R.id.odersButtonadd);
		
		
		shouru.setOnClickListener(this);
		zhichu.setOnClickListener(this);
		zhichu.setVisibility(View.GONE);
		yue.setOnClickListener(this);
		add.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View p1)
	{
		
		Intent intent;
		Bundle bundle;
		
		
		switch(p1.getId()){
			
			case R.id.odersButtonshouru:
				
				intent= new Intent();
				intent.setClass(this, MyActivity.class);
			    bundle = new Bundle();
				bundle.putInt("id", user_info.getId());
				bundle.putInt("isF",0);
				intent.putExtras(bundle);
				startActivity(intent);
				
				
			break;
			
			case R.id.odersButtonzhichu:



				break;
				
			case R.id.odersButtonyue:


				intent= new Intent();
				intent.setClass(this, TotalActivity.class);
			    bundle = new Bundle();
				bundle.putInt("id", user_info.getId());
				intent.putExtras(bundle);
				startActivity(intent);
				

				break;
				
				
			case R.id.odersButtonadd:

				 intent = new Intent();
				intent.setClass(this, ShowOrdersActivity.class);
			    bundle = new Bundle();
				bundle.putInt("user_id", user_info.getId());

				intent.putExtras(bundle);
				startActivity(intent);

				break;
			
		}
	}

	
	
	
}
