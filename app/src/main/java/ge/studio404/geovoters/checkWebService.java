package ge.studio404.geovoters;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class checkWebService extends IntentService {
    Intent i;
    HttpURLConnection connection = null;
    BufferedReader reader = null;
    StringBuffer buffer;
    MyDBHandler dbHandler;
    String deviceid;

    public checkWebService() {
        super("checkWebService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        i = new Intent();
        i.setAction("ge.studio404.geovoters");
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        dbHandler = new MyDBHandler(this, "geovote.db", null, 1);

        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            deviceid = telephonyManager.getDeviceId();
        }catch (Exception e){
            Log.i("ERROR_MSG", e.toString());
        }

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    if (dbHandler.checkuser(deviceid)) {
                        Log.i("WOW_MESSAGE", "USER EXISTS");
                    } else {
                        dbHandler.addUser(deviceid);
                    }

                    URL url = new URL("http://geovoters.404.ge/ge/pageinfo?imei=" + deviceid);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream stream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));

                    buffer = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    JSONObject jsonObject = new JSONObject(buffer.toString());

                    i.putExtra("MessageFrom", jsonObject.getString("PeopleRegisterd"));
                    i.putExtra("WelcomeText", jsonObject.getString("WelcomeText"));

                    JSONArray arr = new JSONArray(jsonObject.getString("cataloglist"));

                    for (int i = 0; i < arr.length(); i++) {
                        int idx = arr.getJSONObject(i).getInt("idx");
                        String io = arr.getJSONObject(i).getString("title");
                        dbHandler.insertCatalogList(idx, io);
                    }

                } catch (Exception e) {
                    Log.i("WOW_MESSAGE", "Error: " + e);
                }

                sendBroadcast(i);
                Log.i("WOW_MESSAGE", "checkWebService");
            }
        }, 0, 5, TimeUnit.SECONDS);

    }



}
