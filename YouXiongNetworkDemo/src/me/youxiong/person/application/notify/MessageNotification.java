package me.youxiong.person.application.notify;

import me.youxiong.person.R;
import me.youxiong.person.ui.MainActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

// e
public final class MessageNotification {
	
	public static void a(Context context, boolean paramBoolean1, boolean paramBoolean2) {
		NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
		@SuppressWarnings("deprecation")
		Notification notification = new Notification(R.drawable.head_logo, context.getString(R.string.app_name), System.currentTimeMillis());
		notification.flags = 2;
		Intent intent = new Intent(context, MainActivity.class);
		// 0x14000000 // 335544320
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("from", "notification");
		// PendingIntent.FLAG_ONE_SHOT
		PendingIntent pendingIntent = PendingIntent.getActivity(context, R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_bar);
		remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.ic_launcher);
		remoteViews.setTextViewText(R.id.txt_title, context.getString(R.string.app_name));
		notification.contentView = remoteViews;
		notification.contentIntent = pendingIntent;
//		if (paramBoolean1) {
//			remoteViews.setImageViewResource(R.id.btn_lock_on_off, R.drawable.common_slider2_on);
//		} else {
//			remoteViews.setImageViewResource(R.id.btn_lock_on_off, R.drawable.common_slider2_off);
//		}
//		if (paramBoolean2) {
//			remoteViews.setImageViewResource(R.id.btn_wakeup_on_off, R.drawable.common_slider2_on);
//		} else {
//			remoteViews.setImageViewResource(R.id.btn_wakeup_on_off, R.drawable.common_slider2_off);
//		}
		// v2  notification_app_turn_on_off
		
//		remoteViews.setOnClickPendingIntent(
//				R.id.btn_lock_on_off,
//				PendingIntent.getBroadcast(context, R.string.notification_app_turn_on_off,
//						new Intent(context.getPackageName() + "_switch_setting_receiver"), PendingIntent.FLAG_UPDATE_CURRENT));
//		remoteViews.setOnClickPendingIntent(
//				R.id.btn_wakeup_on_off,
//				PendingIntent.getBroadcast(context, R.string.notification_swipe_wakeup,
//						new Intent(context.getPackageName() + "_switch_wakeup_setting_receiver"), PendingIntent.FLAG_UPDATE_CURRENT));
		remoteViews.setOnClickPendingIntent(R.id.img_go_settings, PendingIntent.getActivity(context,
				R.string.app_name, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
		
		notificationManager.notify(android.R.drawable.ic_notification_clear_all, notification);
		
	}
}
