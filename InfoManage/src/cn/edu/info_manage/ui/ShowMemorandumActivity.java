package cn.edu.info_manage.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.edu.info_manage.R;
import cn.edu.info_manage.domain.Memorandum;
import cn.edu.info_manage.domain.User;
import cn.edu.info_manage.util.AlamrReceiver;
import cn.edu.info_manage.util.MemorandumService;
import cn.edu.info_manage.util.OrdersService;
import cn.edu.info_manage.util.UserService;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ShowMemorandumActivity extends Activity{

	private static final String TAG = "BusinessManager";
	
	private User user_info;
	private int userid;
	private int id;
	private Boolean isUpdate = false;
	private MemorandumService mService;
	private ListView listView;
	Memorandum upInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		id = intent.getExtras().getInt("id",-1);
		userid = intent.getExtras().getInt("user_id",-1);
		
		setContentView(R.layout.show_memorandum);
		
		UserService userService = new UserService(this);
		user_info = userService.getUserInfoByID(userid);
		userService.closeDB();
		mService = new MemorandumService(this);
		
		tv_date = (TextView) findViewById(R.id.date);
		tv_time = (TextView) findViewById(R.id.time);
		s_category = (Spinner) findViewById(R.id.category);
		ev_summary = (EditText) findViewById(R.id.edit_summary);
		ev_desvription = (EditText) findViewById(R.id.edit_description);
		
		Button bt_mem_del = (Button) findViewById(R.id.memorandum_del);
		Button bt_mem_save = (Button) findViewById(R.id.memorandum_save);
		
		tv_date.setText(timeFormat.format(System.currentTimeMillis()));  
		
		if(id<0){
			bt_mem_del.setText("舍弃");
		}else{
			bt_mem_save.setText("保存修改");
			isUpdate=true;
			initUpdateView(id);
		}
	}
	private String oldDate;
	private void initUpdateView(int id2) {
		upInfo = mService.selectInfoByID(id2);
		oldDate = upInfo.getDate();
		ev_summary.setText(upInfo.getSummary());
		ev_desvription.setText(upInfo.getDescription());
		tv_date.setText(timeFormat.format(new Date(Long.parseLong(upInfo.getDate()))));
		
		switch (upInfo.getCategory()) {
		case 0:
			s_category.setSelection(1);
			break;
		case 1:
			s_category.setSelection(0);
			break;
		case 2:
			s_category.setSelection(2);
			break;
		default:
			break;
		}
		
	}
	TextView tv_date;
	TextView tv_time;
	Spinner s_category;
	EditText ev_summary;
	EditText ev_desvription;
	public void bt_Add_OK(View view){
		String summary = ev_summary.getText().toString();
		if(null==summary || "".equals(summary)){
			Toast.makeText(this, "请填写备忘录", Toast.LENGTH_SHORT).show();
			return;
		}
		String date = dateAndTime.getTime().getTime()+"";
		int temp = 1;
		if(((String)s_category.getSelectedItem()).equals("普通"))
			temp=1;
		else if(((String)s_category.getSelectedItem()).equals("简单"))
			temp=2;
		else
			temp=0;

		if(isUpdate){
			date = upInfo.getDate();
			upInfo.setSummary(summary);
			upInfo.setDescription(ev_desvription.getText().toString());
			upInfo.setCategory(temp);
			mService.updateMemorandum(upInfo);
			
			String teps = oldDate.substring(oldDate.length()-6);
			int rcodes = Integer.parseInt(teps);
			
			Intent intent1 = new Intent(this,AlamrReceiver.class);   
			PendingIntent pis = PendingIntent.getBroadcast(this, rcodes, intent1, Intent.FLAG_ACTIVITY_NEW_TASK);   
			AlarmManager ams = (AlarmManager) this.getSystemService(Activity.ALARM_SERVICE);   
			ams.cancel(pis);
			Log.d(TAG, rcodes+"  取消旧的闹钟提醒");
			
			
			String tep = upInfo.getDate().substring(upInfo.getDate().length()-6);
			int rcode = Integer.parseInt(tep);

			Log.d(TAG, "设置闹钟");
			Intent intent = new Intent(ShowMemorandumActivity.this,AlamrReceiver.class);   
			intent.putExtra("code", rcode);
			intent.putExtra("id", id);
			intent.putExtra("user_id", userid);
			intent.putExtra("summary", upInfo.getSummary());
			intent.putExtra("remark", upInfo.getDescription());
			PendingIntent pi = PendingIntent.getBroadcast(ShowMemorandumActivity.this, rcode, intent, Intent.FLAG_ACTIVITY_NEW_TASK);   
			AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);   
			am.set(AlarmManager.RTC_WAKEUP, Long.parseLong(upInfo.getDate())/**/
						, pi);//设置闹钟   
			am.setRepeating(AlarmManager.RTC_WAKEUP, Long.parseLong(upInfo.getDate())/**/
						, (3*1000), pi);//重复设置   
			
			this.finish();
			return;
		}
		/* Intent intent = new Intent(ShowMemorandumActivity.this,AlamrReceiver.class);   
		 PendingIntent pi = PendingIntent.getBroadcast(ShowMemorandumActivity.this, 0, intent, 0);   
		 AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);   
		 am.cancel(pi);   
		*/
		mService.insertMemorandum(new Memorandum(0, temp, summary, ev_desvription.getText().toString()
						, date
						, user_info.getId()));
		
		String tep = date.substring(date.length()-6);
		int rcode = Integer.parseInt(tep);
		
		Log.d(TAG, "设置闹钟");
		Intent intent = new Intent(ShowMemorandumActivity.this,AlamrReceiver.class);   
		intent.putExtra("code", rcode);
		intent.putExtra("code", rcode);
		intent.putExtra("id", id);
		intent.putExtra("user_id", userid);
		intent.putExtra("summary", summary);
		intent.putExtra("remark", ev_desvription.getText().toString());
		PendingIntent pi = PendingIntent.getBroadcast(ShowMemorandumActivity.this, rcode, intent, Intent.FLAG_ACTIVITY_NEW_TASK);   
		AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);   
		am.set(AlarmManager.RTC_WAKEUP, Long.parseLong(date)/**/
					, pi);//设置闹钟   
		am.setRepeating(AlarmManager.RTC_WAKEUP, Long.parseLong(date)/**/
					, (3*1000), pi);//重复设置   
		
		this.finish();
	}
	public void bt_Mem_Del(View view){
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
				mService.deleteMemorandum(id);
				Toast.makeText(ShowMemorandumActivity.this, "删除信息成功", Toast.LENGTH_SHORT).show();
				ShowMemorandumActivity.this.finish();
				
			}
		})
		.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.show();
	}
	
	
	Dialog dialog = null;
	private DateFormat timeFormat = DateFormat.getDateTimeInstance(); 
	private Calendar dateAndTime = Calendar.getInstance(Locale.CHINA); 
	public void selectDate(View view){
		dialog = new DatePickerDialog(  
				this,                
				new DatePickerDialog.OnDateSetListener() {                    
					public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {  
						dateAndTime.set(Calendar.YEAR, year); 
						dateAndTime.set(Calendar.MONTH, month); 
						dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth); 

						tv_date.setText(timeFormat.format(dateAndTime.getTime()));  
						if(upInfo!=null)
							upInfo.setDate((dateAndTime.getTime().getTime())+"");
						}                
					},                 
					dateAndTime.get(Calendar.YEAR),             
					dateAndTime.get(Calendar.MONTH),        
					dateAndTime.get(Calendar.DAY_OF_MONTH)  
		);
		dialog.show();
	}
	
	public void selectTime(View view){
		dialog=new TimePickerDialog(                
				this,                 
				new TimePickerDialog.OnTimeSetListener(){                    
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) { 
						dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay); 
						dateAndTime.set(Calendar.MINUTE, minute);

						tv_date.setText(timeFormat.format(dateAndTime.getTime()));   
						if(upInfo!=null)
							upInfo.setDate((dateAndTime.getTime().getTime())+"");
						}                
					},                
					dateAndTime.get(Calendar.HOUR_OF_DAY),                
					dateAndTime.get(Calendar.MINUTE),                
					true            
			);
		dialog.show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mService!=null)
			mService.closeDB();
	}

	
}
