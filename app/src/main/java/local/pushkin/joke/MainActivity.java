package local.pushkin.joke;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, DelayedMessageService.class);
        intent.putExtra(DelayedMessageService.EXTRA_MESSAGE_START, getResources().getString(R.string.button_response1));
        intent.putExtra(DelayedMessageService.EXTRA_MESSAGE_END, getResources().getString(R.string.button_response2));
        startService(intent);
    }
}