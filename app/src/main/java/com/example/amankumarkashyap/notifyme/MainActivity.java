package com.example.amankumarkashyap.notifyme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button notify,cancel,update;
    private NotificationManager nm;
    private final static String CHANNEL_ID = "simpleNotification";
    private final static int NOTIFICATION_ID = 100;
    private final static int REQUEST_CODE = 101;
    private final static int REQUEST_CODE_LEARN_MORE = 102;
    private final static int REQUEST_CODE_UPDATE = 103;
    private final static int REQUEST_CODE_CANCEL = 104;
    private final static String URL = "https://www.facebook.com";
    private final static String ACTION_UPDATE_NOTIFICATION = "com.example.amankumarkashyap.notifyme.ACTION_UPDATE_NOTIFICATION";
    private final static String ACTION_DELETE_NOTIFICATION = "com.example.amankumarkashyap.notifyme.ACTION_DELETE_NOTIFICATION";
    private NotificationReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();
        nm = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNotification();
            }
        });
        mReceiver = new NotificationReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ACTION_UPDATE_NOTIFICATION);
        mFilter.addAction(ACTION_DELETE_NOTIFICATION);
        registerReceiver(mReceiver,mFilter);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNotification();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotification();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotification();
            }
        });
        notify.setEnabled(true);
        update.setEnabled(false);
        cancel.setEnabled(false);
    }
    private void initialise()
    {
        notify = findViewById(R.id.notify);
        update = findViewById(R.id.update);
        cancel = findViewById(R.id.cancel);
    }
    public void generateNotification()
    {
        NotificationCompat.Builder notification;
        Intent learnIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        PendingIntent learnPending = PendingIntent.getActivity(this,REQUEST_CODE_LEARN_MORE,learnIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePending = PendingIntent.getBroadcast(this,REQUEST_CODE_UPDATE,updateIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Intent cancelIntent = new Intent(ACTION_DELETE_NOTIFICATION);
        PendingIntent cancelPending = PendingIntent.getBroadcast(this,REQUEST_CODE_CANCEL,cancelIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification = new NotificationCompat.Builder(this,CHANNEL_ID);
        notification.setAutoCancel(true);
        notification.setContentTitle("YOu have been notified");
        notification.setContentText("This is the notification text");
        notification.setPriority(NotificationCompat.PRIORITY_HIGH);
        notification.setSmallIcon(R.drawable.ic_stat_name);
        notification.setOngoing(false);
        notification.setDefaults(NotificationCompat.DEFAULT_ALL);
        notification.addAction(R.drawable.ic_learn_more,"Learn More",learnPending);
        notification.addAction(R.drawable.ic_update,"Update",updatePending);
        notification.setDeleteIntent(cancelPending);

        Intent simpleIntent = new Intent(this,MainActivity.class);

        PendingIntent simplePending = PendingIntent.getActivity(this,REQUEST_CODE,simpleIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        notification.setContentIntent(simplePending);
        nm.notify(NOTIFICATION_ID,notification.build());
        notify.setEnabled(false);
        update.setEnabled(true);
        cancel.setEnabled(true);

    }

    /**
     * There are many ways to update the notification.
     * Android notifications come with alternative styles that can help condense information or represent it more efficiently.
     * For example, the Gmail app uses "InboxStyle" notifications if there is more than a single unread message,
     * condensing the information into a single notification.
     */
    public void updateNotification()
    {
        NotificationCompat.Builder notification;
        notification = new NotificationCompat.Builder(this,CHANNEL_ID);
        Intent learnIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        PendingIntent learnPending = PendingIntent.getActivity(this,REQUEST_CODE_LEARN_MORE,learnIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Intent cancelIntent = new Intent(ACTION_DELETE_NOTIFICATION);
        PendingIntent cancelPending = PendingIntent.getBroadcast(this,REQUEST_CODE_CANCEL,cancelIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        notification.setAutoCancel(true);
        notification.setContentTitle("YOu have been notified");
        notification.setContentText("This is the notification text");
        notification.setPriority(NotificationCompat.PRIORITY_HIGH);
        notification.setSmallIcon(R.drawable.ic_stat_name);
        notification.setOngoing(false);
        notification.setDefaults(NotificationCompat.DEFAULT_ALL);
        notification.addAction(R.drawable.ic_learn_more,"Learn More",learnPending);
        notification.setDeleteIntent(cancelPending);
        Bitmap androidImage = BitmapFactory.decodeResource(getResources(),R.drawable.mascot_1);
        notification.setStyle(new NotificationCompat.BigPictureStyle()
        .setBigContentTitle("UPdated")
        .bigPicture(androidImage));


        nm.notify(NOTIFICATION_ID,notification.build());

        notify.setEnabled(false);
        update.setEnabled(false);
        cancel.setEnabled(true);
    }
    public void cancelNotification()
    {
        nm.cancel(NOTIFICATION_ID);
        notify.setEnabled(true);
        update.setEnabled(false);
        cancel.setEnabled(false);
    }
    public class NotificationReceiver extends BroadcastReceiver {
        NotificationReceiver() {}

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action)
            {
                case ACTION_UPDATE_NOTIFICATION:
                    updateNotification();
                    break;
                case ACTION_DELETE_NOTIFICATION:
                    cancelNotification();

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
