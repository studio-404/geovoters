package ge.studio404.geovoters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class profile_activity extends AppCompatActivity {
    Toolbar mActionBarToolbar;
    MyDBHandler dbHandler;
    EditText imeiText;
    EditText saxeligvariText;
    EditText elfostaText;
    RadioButton mamrobitiRadio;
    RadioButton mdedrobitiRadio;
    ProgressDialog psd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(R.string.profileActivityTitle);
        setSupportActionBar(mActionBarToolbar);
        dbHandler = new MyDBHandler(this, "geovote.db", null, 1);
        imeiText = (EditText)findViewById(R.id.imeicodeText);
        saxeligvariText = (EditText)findViewById(R.id.saxeligvariText);
        elfostaText = (EditText)findViewById(R.id.elfostaText);
        mamrobitiRadio = (RadioButton)findViewById(R.id.mamrobitiRadio);
        mdedrobitiRadio = (RadioButton)findViewById(R.id.mdedrobitiRadio);
        String[] d = dbHandler.selectUserData();
        imeiText.setText(d[0]);
        saxeligvariText.setText(d[1]);
        elfostaText.setText(d[2]);
        if(d[3]!=null) {
            switch (d[3]) {
                case "mamrobiti":
                    mamrobitiRadio.setChecked(true);
                    mdedrobitiRadio.setChecked(false);
                    break;
                case "mdedrobiti":
                    mamrobitiRadio.setChecked(false);
                    mdedrobitiRadio.setChecked(true);
                    break;
                default:
                    mamrobitiRadio.setChecked(false);
                    mdedrobitiRadio.setChecked(false);
                    break;
            }
        }else{
            mamrobitiRadio.setChecked(false);
            mdedrobitiRadio.setChecked(false);
        }

    }

    public void updateProfile(View view){
        waitMe();

        String imei = imeiText.getText().toString();
        String saxeli = saxeligvariText.getText().toString();
        String email = elfostaText.getText().toString();
        String gender;
        if(mamrobitiRadio.isChecked()){
            gender = "mamrobiti";
        }else{
            gender = "mdedrobiti";
        }
        dbHandler.updateProfile(saxeli, email, gender);
        try {
            String sx = URLEncoder.encode(saxeli,"UTF-8");
            String em = URLEncoder.encode(email,"UTF-8");
            String ge = URLEncoder.encode(gender,"UTF-8");
            String url = "http://geovoters.404.ge/ge/pageinfo?updateprofile=true&i="+imei+"&s="+sx+"&e="+em+"&g="+ge;

            new RetrieveFeedTask().execute(url);
        } catch (Exception e) {
            Log.i("ERROR_MSG", e.toString());
        }
        Toast.makeText(this, "განახლება წარმატებით დასრულდა!", Toast.LENGTH_LONG).show();
        waitClose();
    }

    public void waitMe(){
        psd = new ProgressDialog(this);
        psd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        psd.setMessage("გთხოვთ დაიცადოთ...");
        psd.setIndeterminate(true);
        psd.setCancelable(true);
        psd.show();
    }

    public void waitClose(){
        psd.hide();
    }


    public void gotoTabDemo(View view){
        waitMe();
        Intent i = new Intent(profile_activity.this, tabDemo.class);
        finish();
        startActivity(i);
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, Boolean> {

        HttpURLConnection connection;
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                URL url= new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                connection.getInputStream();
            } catch (Exception e) {
                Log.i("ERROR_MSG",e.toString());
            }
            return null;
        }
    }

}
