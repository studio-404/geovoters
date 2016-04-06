package ge.studio404.geovoters;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class make_a_vote extends AppCompatActivity {
    TextView mainText;
    Toolbar toolbar;
    private ArrayAdapter<String> adapter;
    boolean mIsReceiverRegistered = false;
    MyBroadcastReceiver mReceiver = null;
    public String itemidx;
    TextView moreData;
    String itemDate;
    String deviceid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_a_vote);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        itemidx = intent.getStringExtra("itemidx");
        String itemQuestion = intent.getStringExtra("itemQuestion");
        itemDate = intent.getStringExtra("itemDate");
        String itemMember = intent.getStringExtra("itemMember");

        mainText = (TextView)findViewById(R.id.mainText);
        moreData = (TextView)findViewById(R.id.moreData);
        mainText.setText("# "+itemidx+"\n\n"+itemQuestion);
        moreData.setText(itemDate +"\n"+ itemMember);
        new MyTask().execute(Integer.parseInt(itemidx));


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsReceiverRegistered) {
            if (mReceiver == null)
                mReceiver = new MyBroadcastReceiver();
            registerReceiver(mReceiver, new IntentFilter("DATA_CHANGED"));
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

    public void gotoTabActivity(View view){
        Intent i = new Intent(this, tabDemo.class);
        startActivity(i);
    }

    class MyTask extends AsyncTask<Integer, String, Void> {
        BufferedReader reader = null;
        StringBuffer buffer;
        JSONArray arr;
        ProgressDialog psd;
        @Override
        protected void onPreExecute() {
            psd = new ProgressDialog(make_a_vote.this);
            psd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            psd.setMessage("გთხოვთ დაიცადოთ...");
            psd.setIndeterminate(true);
            psd.setCancelable(true);
            psd.show();
            ListView answersListView = (ListView)findViewById(R.id.answersListView);
            answersListView.setDivider(null);
            ListAdapter answersListViewAdapter = new custom_adapter2(make_a_vote.this, new ArrayList<String>());
            answersListView.setAdapter(answersListViewAdapter);
            adapter = (ArrayAdapter<String>) answersListView.getAdapter();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                URL url = new URL("http://geovoters.404.ge/ge/pasuxebi?loadanswers=true&connectidx="+params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                JSONObject jsonObject = new JSONObject(buffer.toString());

                arr = new JSONArray(jsonObject.getString("answerslist"));

                ArrayList<String> listItems = new ArrayList<>();

                for (int i = 0; i < arr.length(); i++) {
                    int idx = arr.getJSONObject(i).getInt("idx");
                    String namelname = arr.getJSONObject(i).getString("namelname");
                    int votes = arr.getJSONObject(i).getInt("votes");
                    int percent = arr.getJSONObject(i).getInt("percent");

                    listItems.add(namelname + "^" + percent + "#" + votes+"%"+idx);

                }

                for(String item : listItems) {
                    Log.i("WEBSITE",item);
                    publishProgress(item);
                }


            }catch (Exception e){
                Log.i("taskBG", e+"USER EXISTS");
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            adapter.add(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            psd.hide();
            Toast.makeText(make_a_vote.this, "ჩატვირთვა დასრულდა!", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateUI(Intent intent) {
        String itemMember = intent.getStringExtra("itemMember");

        moreData.setText(itemDate +"\nმონაწილე: "+ itemMember);
        new MyTask().execute(Integer.parseInt(itemidx));

    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    }

}
