package local.pushkin.joke;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
* TODO: Customize class - update intent actions and extra parameters.
 */
public class DelayedMessageService extends IntentService {

    public static final String EXTRA_MESSAGE = "extra_message_end";
    private static final long DELAY_TIME = 5000;
    private Handler handler;


    public DelayedMessageService() {
        //Creates an IntentService. Invoked by your subclass's constructor.
        //Params:
        //name – Used to name the worker thread, important only for debugging.
        super("DelayedMessageService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        handler = new Handler();
        return super.onStartCommand(intent, flags, startId);
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
        handler.post(() -> Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show());
    }
}