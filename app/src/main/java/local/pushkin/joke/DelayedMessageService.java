package local.pushkin.joke;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
* TODO: Customize class - update intent actions and extra parameters.
 */
public class DelayedMessageService extends IntentService {

    public static final String EXTRA_MESSAGE = "extra_message_end";
    private static final long DELAY_TIME = 5000;
    public static final int NOTIFICATION_ID = 5453;


    public DelayedMessageService() {
        //Creates an IntentService. Invoked by your subclass's constructor.
        //Params:
        //name – Used to name the worker thread, important only for debugging.
        super("DelayedMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        synchronized (this) { // if no synchronized then: java.lang.IllegalMonitorStateException: object not locked by thread before wait()
            try {
                wait(DELAY_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        showText(intent.getStringExtra(EXTRA_MESSAGE));
    }

    private void showText(final String text) {
        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(intent);

        //java.lang.IllegalArgumentException: local.pushkin.joke: Targeting S+ (version 31 and above) requires that one of FLAG_IMMUTABLE or FLAG_MUTABLE be specified when creating a PendingIntent.
        //Strongly consider using FLAG_IMMUTABLE, only use FLAG_MUTABLE if some functionality depends on the PendingIntent being mutable, e.g. if it needs to be used with inline replies or bubbles.
        //WORKS WITH NEXUS 4 API 22
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setContentIntent(pendingIntent)
                .setContentText(text)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}