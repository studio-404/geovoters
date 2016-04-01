package ge.studio404.geovoters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class tabDemo extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    HomeFragment home;
    Bundle bundle;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_demo);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.mainContent);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        dbHandler = new MyDBHandler(this, "geovote.db", null, 1);

        try {
            String[] selectCatalogLists = dbHandler.selectCatalogList();
            for (String catalogname : selectCatalogLists) {
                bundle = new Bundle();
                bundle.putString("edttext", catalogname);
                home = new HomeFragment();
                home.setArguments(bundle);
                viewPagerAdapter.addFragments(home, catalogname);

                String[] questions = {"How Are you Bitch?","I am fine suker","how is things?"};
                ListAdapter giosListAdapter = new custom_adapter(this, questions);
                ListView giosListView = (ListView)findViewById(R.id.myListView);
                giosListView.setAdapter(giosListAdapter);
            }
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setSelectedTabIndicatorColor(Color.rgb(242, 242, 242));
        tabLayout.setupWithViewPager(viewPager);

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
