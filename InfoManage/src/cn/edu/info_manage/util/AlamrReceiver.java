package cn.edu.info_manage.util;

import cn.edu.info_manage.ui.MemorandumActivity;
import cn.edu.info_manage.ui.ShowMemorandumActivity;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlamrReceiver extends BroadcastReceiver {
	private static final String TAG = "AlamrReceiver";
	
	@Override
	public void onReceive(final Context context, Intent intent) {

		final int rcode = intent.getIntExtra("code", -1);
		final int id = intent.getIntExtra("id", -1);
		final int user_id = intent.getIntExtra("user_id", -1);
		
		final String summary = intent.getStringExtra("summary");
		final String remark = intent.getStringExtra("remark");
		
		new Thread(){
			@Override
			public void run() {
				Intent intent = new Intent();
				if(id<0){
					intent.setClass(context, MemorandumActivity.class);
				}else{
					intent.setClass(context, ShowMemorandumActivity.class);
				}
				intent.putExtra("id", id);
				intent.putExtra("id", user_id);
				NotificationUtil.addNotification(context, 0, summary, summary, remark, rcode, intent);
			}
		}.run();
		
		Log.d(TAG, rcode+"  闹钟提醒");
		Intent intent1 = new Intent(context,AlamrReceiver.class);   
		PendingIntent pi = PendingIntent.getBroadcast(context, rcode, intent1, Intent.FLAG_ACTIVITY_NEW_TASK);   
		AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);   
		am.cancel(pi);
		Log.d(TAG, rcode+"  取消闹钟提醒");
	}

}
