package cn.edu.info_manage.ui;

import java.text.DateFormat;
import java.util.Date;

import cn.edu.info_manage.R;
import cn.edu.info_manage.domain.Contacts;
import cn.edu.info_manage.domain.Orders;
import cn.edu.info_manage.util.ContactsService;
import cn.edu.info_manage.util.MemorandumService;
import cn.edu.info_manage.util.OrdersService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ShowOrdersActivity extends Activity {
	private static final String TAG="ShowOrdersActivity";
	
	private EditText et_use;
	private EditText et_remark;
	private EditText et_date;
	private EditText et_money;
	private RadioGroup et_in_out;
	
	private Button bt_save_update;
	private Button bt_orders_del;
	
	private OrdersService oService;
	private Boolean isUpdate = false;
	private int id;
	private int userid;
	private DateFormat timeFormat = DateFormat.getDateTimeInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_orders);
		findView();
		
		Intent intent = getIntent();
		id = intent.getExtras().getInt("id",-1);
		userid = intent.getExtras().getInt("user_id",-1);
		oService = new OrdersService(this);
		
		if(id<0){
			bt_orders_del.setText("舍弃");
		}else{
			bt_save_update.setText("保存修改");
			isUpdate=true;
			initUpdateView(id);
		}
	}
	public void bt_SaveUpdate(View view){
		Orders info = getInfo();
		if(info==null){
			return;
		}
		if(isUpdate){
			info.setUser_id(userid);
			info.setId(id);
			oService.updateOrders(info);
			Toast.makeText(this, "保存修改", Toast.LENGTH_SHORT).show();
			finish();
		}else{
			info.setUser_id(userid);
			oService.insertOrders(info);
			Toast.makeText(this, "保存", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	public void bt_Del(View view){
		if(!isUpdate){
			this.finish();
			return;
		}
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle("删除 ")
		.setMessage("将删除当前收支信息")
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				oService.delOrders(id);
				
				Toast.makeText(ShowOrdersActivity.this, "删除信息成功", Toast.LENGTH_SHORT).show();
				ShowOrdersActivity.this.finish();
			}
		})
		.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		})
		.show();
	}
	private void findView() {
		et_use = (EditText) findViewById(R.id.orders_use);
		et_remark = (EditText) findViewById(R.id.orders_remark);
		et_money = (EditText) findViewById(R.id.orders_money);
		et_date = (EditText) findViewById(R.id.orders_date);
		et_in_out = (RadioGroup) findViewById(R.id.orders_in_out);
		RadioButton w = (RadioButton) findViewById(R.id.out);
		w.setChecked(true);
		
		et_date.setHint(timeFormat.format(new Date(System.currentTimeMillis())));
		bt_save_update = (Button) findViewById(R.id.save_update);
		bt_orders_del = (Button) findViewById(R.id.orders_del);
	}
	private void initUpdateView(int id){
		Orders order = oService.getOrdersByID(id);
		et_use.setText(order.getUse());
		et_remark.setText(order.getRemark());
		et_money.setText(order.getMoney());
		et_date.setText(order.getDate());
		
		if("收入".equals(order.getIn_out())){
			RadioButton n = (RadioButton) findViewById(R.id.in);
			n.setChecked(true);
		}else{
			RadioButton w = (RadioButton) findViewById(R.id.out);
			w.setChecked(true);
		}
	}
	
	private Orders getInfo(){
		if(et_use.getText().toString()!=null && "".equals(et_use.getText().toString())){
			Toast.makeText(this, "用处不能为空", Toast.LENGTH_SHORT).show();
			return null;
		}
		
		String sexstr=((RadioButton)findViewById(et_in_out.getCheckedRadioButtonId())).getText().toString();
		String date = et_date.getText().toString();
		if("".equals(date))
			date = timeFormat.format(new Date(System.currentTimeMillis()));
		
		return new Orders(-1, date,
					et_money.getText().toString(),
					sexstr,
					et_use.getText().toString(),
					et_remark.getText().toString(),
					-1);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(oService!=null)
			oService.closeDB();
	}
}
