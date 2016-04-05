package ge.studio404.geovoters;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

public class make_a_vote extends AppCompatActivity {
    TextView mainText;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_a_vote);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String itemidx = intent.getStringExtra("itemidx");
        mainText = (TextView)findViewById(R.id.mainText);
        mainText.setText(itemidx);
    }

    public void gotoTabActivity(View view){
        Intent i = new Intent(this, tabDemo.class);
        startActivity(i);
    }

}
