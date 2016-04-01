package ge.studio404.geovoters;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    public TextView peopleCount;
    public TextView WelcomeText;
    public Intent i;
    boolean mIsReceiverRegistered = false;
    MyBroadcastReceiver mReceiver = null;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        peopleCount = (TextView)findViewById(R.id.peopleCount);
        WelcomeText = (TextView)findViewById(R.id.WelcomeText);
        dbHandler = new MyDBHandler(this, "geovote.db", null, 1);

        i = new Intent(this, checkWebService.class);
        startService(i);
        Log.i("ERROR_START_MSG","Oncreate DONE");
    }

    public void signinsystem(View view){
        ProgressDialog psd = new ProgressDialog(this);
        psd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        psd.setMessage("გთხოვთ დაიცადოთ...");
        psd.setIndeterminate(true);
        psd.setCancelable(true);
        psd.show();
        Intent i = new Intent(MainActivity.this, tabDemo.class);
        finish();
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsReceiverRegistered) {
            if (mReceiver == null)
                mReceiver = new MyBroadcastReceiver();
            registerReceiver(mReceiver, new IntentFilter("ge.studio404.geovoters"));
            mIsReceiverRegistered = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIsReceiverRegistered) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
            mIsReceiverRegistered = false;
        }
    }

    private void updateUI(Intent intent) {
        String s = intent.getStringExtra("MessageFrom");
        String w = intent.getStringExtra("WelcomeText");
        WelcomeText.setText(w);
        peopleCount.setText(s);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHandler.clearCatalogList();
    }
}