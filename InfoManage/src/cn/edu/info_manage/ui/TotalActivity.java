package cn.edu.info_manage.ui;
import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import cn.edu.info_manage.util.*;
import cn.edu.info_manage.R;
import cn.edu.info_manage.domain.*;
import java.util.*;
public class TotalActivity extends Activity
{
	private static final String TAG = "TotalActivity";

	private User user_info;
	
	private List<Orders> infos;
	private OrdersService oService;
	TextView zhichu,shouru,yue;
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
		setContentView(R.layout.my);
		UserService userService = new UserService(this);
		user_info = userService.getUserInfoByID(id);


		user_info = userService.getUserInfoByID(id);

		if(user_info==null){
			user_info = userService.getUserInfoByName(name);
		}

		userService.closeDB();

		shouru=(TextView)findViewById(R.id.myTextViewshouru);
	    zhichu=(TextView)findViewById(R.id.myTextViewout);
		yue=(TextView)findViewById(R.id.myTextViewyue);
		
		
		oService = new OrdersService(this);
		
		infos = oService.getAllOrders(user_info.getId());
		
		int out=0;
		int in=0;
		
		for(int i=0;i<infos.size();i++){
			
			Orders info = infos.get(i);

			if(info.getIn_out().equals("收入")){
				in+=Integer.parseInt( info.getMoney());
			}
			
			if(info.getIn_out().equals("支出")){
				out+=Integer.parseInt( info.getMoney());
			}
			
			
		}
		
		shouru.setText(in+"元");
		zhichu.setText(out+"元");
		yue.setText((in-out)+"元");
		
		
		
	}
	

	
	
}
