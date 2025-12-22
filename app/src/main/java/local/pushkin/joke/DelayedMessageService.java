package local.pushkin.joke;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
* TODO: Customize class - update intent actions and extra parameters.
 */
public class DelayedMessageService extends Service {

    public static final String TAG = "DelayedMessageService";
    public static final String EXTRA_MESSAGE = "extra_message_end";
    public static final String NOTIFICATION_CHANNEL_ID = "1337";
    private static final long DELAY_TIME = 1000;
    public static final int NOTIFICATION_ID = 5453;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        synchronized (this) { // if no synchronized then: java.lang.IllegalMonitorStateException: object not locked by thread before wait()
            try {
                wait(DELAY_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        showText(intent.getStringExtra(EXTRA_MESSAGE));

        return START_NOT_STICKY;
    }

    private void showText(final String text) {
        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(intent);

        //java.lang.IllegalArgumentException: local.pushkin.joke: Targeting S+ (version 31 and above) requires that one of FLAG_IMMUTABLE or FLAG_MUTABLE be specified when creating a PendingIntent.
        //Strongly consider using FLAG_IMMUTABLE, only use FLAG_MUTABLE if some functionality depends on the PendingIntent being mutable, e.g. if it needs to be used with inline replies or bubbles.
        //WORKS WITH NEXUS 4 API 22
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "MainActivity-DelayedMessageService", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(pendingIntent)
                    .build();
        }
        notificationManager.notify(NOTIFICATION_ID, notification);
        Log.d(TAG, "showText()");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}