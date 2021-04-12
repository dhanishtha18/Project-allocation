package org.projectapp.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.projectapp.HomeActivity;
import org.projectapp.R;

public class MyFirebaseMessaging extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sented=remoteMessage.getData().get("sented");

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser!=null && sented.equals(firebaseUser.getUid())){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOreoNotification(remoteMessage);
                } else {
                    sendNotofication(remoteMessage);
                }

        }
    }

    private  void sendOreoNotification(RemoteMessage remoteMessage){
        String user=remoteMessage.getData().get("user");
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");

        RemoteMessage.Notification notification=remoteMessage.getNotification();
        int j=Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent=new Intent(this, HomeActivity.class);
        Bundle bundle=new Bundle();
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        OreoNotification oreoNotification=new OreoNotification(this);
        Notification.Builder builder=oreoNotification.getOreoNotification(title, body, pendingIntent);

        int i=0;
        if (j > 0) {
            i=j;
        }

        oreoNotification.getManager().notify(i, builder.build());
    }

    private void sendNotofication(RemoteMessage remoteMessage) {

        String user=remoteMessage.getData().get("user");
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");

        RemoteMessage.Notification notification=remoteMessage.getNotification();
        int j=Integer.parseInt(user.replaceAll("[\\D]",""));

        Intent intent=new Intent(this, HomeActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this, "org.rait")
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);
        NotificationManager noti=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int i=0;
        if (j > 0) {
            i=j;
        }

        noti.notify(i, builder.build());
    }


}
