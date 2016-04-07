package ge.studio404.geovoters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

public class custom_adapter2 extends ArrayAdapter<String> {
    public Context mcon;
    String singleText;
    String answeridString;
    String answerPercentigeString;
    String secondTokenString;
    String thirdTokenString;
    String votes;
    String itemIdx;
    Button makevoteButton;
    String answer_id;

    public custom_adapter2(Context context, ArrayList<String> questions) {
        super(context, R.layout.custom_row, questions);
        mcon = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView==null) {
            LayoutInflater giosInflate = LayoutInflater.from(getContext());
            convertView = giosInflate.inflate(R.layout.custom_row2, parent, false);
        }

        singleText = getItem(position);
        try{

            StringTokenizer tokens = new StringTokenizer(singleText, "^");

            while (tokens.hasMoreTokens()) {
                answeridString = tokens.nextToken();
                secondTokenString = tokens.nextToken();
                StringTokenizer tokens2 = new StringTokenizer(secondTokenString, "#");
                while (tokens2.hasMoreTokens()) {
                    answerPercentigeString = tokens2.nextToken();
                    thirdTokenString = tokens2.nextToken();
                    StringTokenizer tokens3 = new StringTokenizer(thirdTokenString, "%");
                    votes = tokens3.nextToken();
                    itemIdx = tokens3.nextToken();
                }
            }

        }catch (Exception e){
            Log.i("ERROR_MSG",e.toString());
        }

        TextView answerid = (TextView)convertView.findViewById(R.id.answerid);
//        TextView answerPercentige = (TextView)convertView.findViewById(R.id.answerPercentige);
        TextView superpersentage = (TextView) convertView.findViewById(R.id.superpersentage);
        makevoteButton = (Button) convertView.findViewById(R.id.makevoteButton);
        makevoteButton.setTag(itemIdx);


        makevoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer_id = v.getTag().toString();
                new RetrieveFeedTask().execute(answer_id);
            }
        });


        answerid.setText(answeridString);
//        answerPercentige.setText("");
        superpersentage.setText(answerPercentigeString + "%");

        return convertView;
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            String deviceid = "0";
            try {
                TelephonyManager telephonyManager = (TelephonyManager) mcon.getSystemService(Context.TELEPHONY_SERVICE);
                deviceid = telephonyManager.getDeviceId();
            }catch (Exception e){
                Log.i("ERROR_MSG", e.toString());
            }
            String imeicode = deviceid;
            String member = "0";

            try {
                URL url = new URL("http://geovoters.404.ge/ge/pasuxebi?makevote=true&voteidx="+answer_id+"&imai="+imeicode);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                JSONObject jsonObject = new JSONObject(buffer.toString());

                JSONArray arr = new JSONArray(jsonObject.getString("getanswerdata"));


                for (int i = 0; i < arr.length(); i++) {
                    member = arr.getJSONObject(i).getString("member");
                }

                Log.i("GIOTESTMODE"," "+member);

                Intent i = new Intent();
                i.setAction("DATA_CHANGED");
                i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                i.putExtra("itemMember", member);
                mcon.sendBroadcast(i);

            } catch (Exception e) {
                Log.i("GIOTESTMODE", e.toString());
            }
            return member;
        }

        protected void onPostExecute(String feed) {

        }
    }


}
