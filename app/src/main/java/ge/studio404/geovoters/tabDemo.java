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
    ListView giosListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_demo);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        giosListView = (ListView)findViewById(R.id.myListView);
        ListAdapter giosListAdapter = new custom_adapter(this, new ArrayList<String>());
        giosListView.setAdapter(giosListAdapter);
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

        private ArrayAdapter<String> adapter;

        BufferedReader reader = null;
        StringBuffer buffer;

        @Override
        protected void onPreExecute() {
            adapter = (ArrayAdapter<String>)giosListView.getAdapter();
        }

        @Override
        protected Void doInBackground(Integer... params) {

            ArrayList<String> datax = new ArrayList<>();

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

                JSONArray arr = new JSONArray(jsonObject.getString("cataloglistitems"));
                Log.i("taskBG", params[0]+" tab param");
                int catidxFromDb = dbHandler.selectCataloglistIdx(params[0]);

                for (int i = 0; i < arr.length(); i++) {
                    int catidx = arr.getJSONObject(i).getInt("catidx");
//                    int idx = arr.getJSONObject(i).getInt("idx");
                    int datex = arr.getJSONObject(i).getInt("datex");
                    String question = arr.getJSONObject(i).getString("question");
                    int usersin = arr.getJSONObject(i).getInt("usersin");
                    Log.i("taskBG", catidxFromDb+" catalogidx");

                    if(catidxFromDb==catidx){
                        datax.add(question + "^თარიღი: "+datex+"#მონ." + usersin);
                    }
                }

                for(String item : datax) {
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