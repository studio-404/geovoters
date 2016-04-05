package ge.studio404.geovoters;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

import java.util.ArrayList;

public class make_a_vote extends AppCompatActivity {
    TextView mainText;
    Toolbar toolbar;
    ArrayList<String> answersArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_a_vote);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String itemidx = intent.getStringExtra("itemidx");
        String itemQuestion = intent.getStringExtra("itemQuestion");
        String itemDate = intent.getStringExtra("itemDate");
        String itemMember = intent.getStringExtra("itemMember");

        mainText = (TextView)findViewById(R.id.mainText);
        TextView moreData = (TextView)findViewById(R.id.moreData);
        mainText.setText("# "+itemidx+" \n"+itemQuestion);
        moreData.setText(itemDate +"\n"+ itemMember);

        try {
            answersArrayList = new ArrayList<>();

            answersArrayList.add("გაიმარჯვებს ნაციონალური მოძრაობა^35");
            answersArrayList.add("გაიმარჯვებს ოცნება^65");

            ListView answersListView = (ListView) findViewById(R.id.answersListView);
            //
            answersListView.setDivider(null);
            ListAdapter answersListAdapter = new custom_adapter2(make_a_vote.this, answersArrayList);
            answersListView.setAdapter(answersListAdapter);
        }catch (Exception e){
            Log.i("vai", e.toString());
        }

        //ArrayAdapter<String> adapter = (ArrayAdapter<String>) answersListView.getAdapter();
    }

    public void gotoTabActivity(View view){
        Intent i = new Intent(this, tabDemo.class);
        startActivity(i);
    }

}
