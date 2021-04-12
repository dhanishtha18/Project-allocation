package org.projectapp.Notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.RingtoneManager;
import android.os.Build;

import org.projectapp.R;

public class OreoNotification extends ContextWrapper {

    private static final String CHANNEL_ID="projectapp";
    private static final String CHANNEL_NAME="";

    private NotificationManager notificationManager;

    public OreoNotification(Context base) {
        super(base);


        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createChanne();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChanne() {

        NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager(){
        if (notificationManager==null){
            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getOreoNotification(String title, String body, PendingIntent pendingIntent){
        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.logo)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);
    }
}
