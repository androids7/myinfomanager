package cn.edu.info_manage.util;

import cn.edu.info_manage.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationUtil {
	
	public static void addNotification(Context context,int number, String tickerText, String title, String content, int tag,Intent in){
    	Intent intent = new Intent(in);
    	
    	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    	
    	Notification notification = new Notification(R.drawable.icon, tickerText, System.currentTimeMillis());
	//	notification.setLatestEventInfo(context, title, content, pendingIntent);
		notification.number = number;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_SOUND;
		NotificationManager manager = (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(tag, notification);
    }
    
   public static void cancelNotification(Context context, int tag){
	   NotificationManager notificationService = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	   notificationService.cancel(tag);
   }

}
