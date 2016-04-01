package ge.studio404.geovoters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class tabDemo extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_demo);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        dbHandler = new MyDBHandler(this, "geovote.db", null, 1);
        String[] selectCatalogLists = dbHandler.selectCatalogList();


        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setSelectedTabIndicatorColor(Color.rgb(242, 242, 242));
        for (String title : selectCatalogLists) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
        listviewChange(0);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                listviewChange(tabLayout.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    public void listviewChange(int tid){
        String[] datax = new String[6];
        datax[0] = "არსებობს სტრუქტურა ჯანდაცვის სამინისტროში, რომელიც დადგენილი წესით შეისწავლის საკითხს "+tid;
        datax[1] = "არსებობს სტრუქტურა ჯანდაცვის სამინისტროში, რომელიც დადგენილი წესით შეისწავლის საკითხს";
        datax[2] = "არსებობს სტრუქტურა ჯანდაცვის სამინისტროში, რომელიც დადგენილი წესით შეისწავლის საკითხს";
        datax[3] = "არსებობს სტრუქტურა ჯანდაცვის სამინისტროში, რომელიც დადგენილი წესით შეისწავლის საკითხს";
        datax[4] = "არსებობს სტრუქტურა ჯანდაცვის სამინისტროში, რომელიც დადგენილი წესით შეისწავლის საკითხს";
        datax[5] = "არსებობს სტრუქტურა ჯანდაცვის სამინისტროში, რომელიც დადგენილი წესით შეისწავლის საკითხს";
        ListAdapter giosListAdapter = new custom_adapter(this, datax);
        ListView giosListView = (ListView)findViewById(R.id.myListView);
        giosListView.setAdapter(giosListAdapter);
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
