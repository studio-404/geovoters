package ge.studio404.geovoters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class tabDemo extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    MyDBHandler dbHandler;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_tab_demo);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        new MyTask().execute(0);

        dbHandler = new MyDBHandler(this, "geovote.db", null, 1);
        String[] selectCatalogLists = dbHandler.selectCatalogList();

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setSelectedTabIndicatorColor(Color.rgb(242, 242, 242));
        for (String title : selectCatalogLists) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adapter.clear();
                new MyTask().execute(tabLayout.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    class MyTask extends AsyncTask<Integer, String, Void>{
        BufferedReader reader = null;
        StringBuffer buffer;
        private int count;
        JSONArray arr;
        @Override
        protected void onPreExecute() {
            ListView giosListView = (ListView)findViewById(R.id.myListView);
            ListAdapter giosListAdapter = new custom_adapter(tabDemo.this, new ArrayList<String>());
            giosListView.setAdapter(giosListAdapter);
            adapter = (ArrayAdapter<String>)giosListView.getAdapter();
            setProgressBarIndeterminate(false);
            setProgressBarVisibility(true);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                URL url = new URL("http://geovoters.404.ge/ge/pageinfo");
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

                arr = new JSONArray(jsonObject.getString("cataloglistitems"));
                int catidxFromDb = dbHandler.selectCataloglistIdx(params[0]);
                ArrayList<String> listItems = new ArrayList<>();

                for (int i = 0; i < arr.length(); i++) {
                    int catidx = arr.getJSONObject(i).getInt("catidx");
                    //int idx = arr.getJSONObject(i).getInt("idx");
                    String datex = arr.getJSONObject(i).getString("datex");
                    String question = arr.getJSONObject(i).getString("question");
                    int usersin = arr.getJSONObject(i).getInt("usersin");
                    Log.i("taskBG", catidxFromDb + " catalogidx "+catidx+" "+arr.length());

                    if(catidxFromDb==catidx){
                        listItems.add(question + "^თარიღი: " + datex + "#მონაწილე " + usersin);
                    }
                }

                for(String item : listItems) {
                    try {
                        Thread.sleep(200);
                    }catch (Exception e){
                        Log.i("taskBG", e.toString());
                    }
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
            Log.i("taskBG", " on progress ");
            count++;
            setProgress((int) (((double) count / arr.length()) * 10000));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setProgressBarVisibility(false);
            Toast.makeText(tabDemo.this, "ჩატვირთვა დასრულდა!", Toast.LENGTH_LONG).show();
        }
    }


    public void gotoeditprofile(View view){
        waitMe();
        Intent i = new Intent(tabDemo.this, profile_activity.class);
        finish();
        startActivity(i);
    }

    public void gotoMainActivity(View view){
        waitMe();
        Intent i = new Intent(tabDemo.this, MainActivity.class);
        finish();
        startActivity(i);
    }

    public void waitMe(){
        ProgressDialog psd = new ProgressDialog(this);
        psd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        psd.setMessage("გთხოვთ დაიცადოთ...");
        psd.setIndeterminate(true);
        psd.setCancelable(true);
        psd.show();
    }
}