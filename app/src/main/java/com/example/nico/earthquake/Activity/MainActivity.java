package com.example.nico.earthquake.Activity;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.nico.earthquake.GetJSON.GetJSON;
import com.example.nico.earthquake.R;
import com.example.nico.earthquake.Service.AlertService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;


public class MainActivity extends ListActivity {

    private static String TAG = "EARTHQUAKE";

    private static final String TAG_META = "metadata";
    private static final String TAG_COUNT = "count";
    private static final String TAG_STATUS = "status";
    private static final String TAG_FEATURES = "features";
    private static final String TAG_PROPERTY = "properties";
    private static final String TAG_MAG = "mag";
    private static final String TAG_PLACE = "place";
    private static final String TAG_TIME = "time";
    private static final String TAG_DETAIL_URL = "detail";

    public ArrayList<HashMap<String, String>> earthquakeList = null;

    public int time = 1;
    public int magnitude = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get parameters
        String url;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.time = extras.getInt("time");
            this.magnitude = extras.getInt("magnitude");
            url = getUrl(time, magnitude);
            Log.d(TAG, "url : " + url);

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            earthquakeList = new ArrayList<HashMap<String, String>>();

            new WebServiceRequestor(url, param).execute();
        } else {
            url = getUrl(this.time, this.magnitude);
            Log.d(TAG, "url : " + url);

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            earthquakeList = new ArrayList<HashMap<String, String>>();

            new WebServiceRequestor(url, param).execute();
        }
    }

    public String getUrl(int time, int magnitude){
        return GetJSON.getUrl(time, magnitude);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d(TAG, "Action settings");
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("magnitude", this.magnitude);
                startActivity(intent);
                return true;
            case R.id.action_past_hour:
                Log.d(TAG, "Action past hour");
                intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("time", 0);
                intent.putExtra("magnitude", this.magnitude);
                finish();
                startActivity(intent);
                return true;
            case R.id.action_past_day:
                Log.d(TAG, "Action past day");
                intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("time", 1);
                intent.putExtra("magnitude", this.magnitude);
                finish();
                startActivity(intent);
                return true;
            case R.id.action_past_week:
                Log.d(TAG, "Action past week");
                intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("time", 2);
                intent.putExtra("magnitude", this.magnitude);
                finish();
                startActivity(intent);
                return true;
            case R.id.action_past_month:
                Log.d(TAG, "Action past month");
                intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("time", 3);
                intent.putExtra("magnitude", this.magnitude);
                finish();
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class WebServiceRequestor extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;
        String URL;
        List<NameValuePair> parameters;

        public WebServiceRequestor(String url, List<NameValuePair> params)
        {
            this.URL = url;
            this.parameters = params;
        }
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;
                HttpPost httpPost = new HttpPost(URL);
                if (parameters != null)
                {
                    httpPost.setEntity(new UrlEncodedFormEntity(parameters));
                }
                httpResponse = httpClient.execute(httpPost);
                httpEntity = httpResponse.getEntity();
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpEntity));

                try {
                    JSONObject valMeta = jsonObject.getJSONObject(TAG_META);
                    int count = valMeta.getInt(TAG_COUNT);
                    int status = valMeta.getInt(TAG_STATUS);

                    if (count != 0 && status == 200){
                        JSONArray valFeatures = jsonObject.getJSONArray(TAG_FEATURES);
                        for (int i = 0; i< valFeatures.length(); i++){
                            JSONObject jObj = valFeatures.getJSONObject(i);

                            JSONObject valProp = jObj.getJSONObject(TAG_PROPERTY);
                            double mag = valProp.getDouble(TAG_MAG);
                            String place = valProp.getString(TAG_PLACE);
                            long time = valProp.getLong(TAG_TIME);
                            String detail = valProp.getString(TAG_DETAIL_URL);

                            // tmp hashmap for single EQ
                            HashMap<String, String> earthquakeTemp = new HashMap<String, String>();

                            Date timeDate = new Date();
                            timeDate.setTime(time);
                            earthquakeTemp.put(TAG_DETAIL_URL, detail);
                            earthquakeTemp.put(TAG_MAG, "Magnitude : " +  mag);
                            earthquakeTemp.put(TAG_PLACE, place);
                            earthquakeTemp.put(TAG_TIME, "" + timeDate.toString());

                            earthquakeList.add(earthquakeTemp);
                        }

                    }
                    else{
                        Log.d(TAG, "No earthquake found");
                    }

                }
                catch (JSONException e) { Log.d(TAG, "Error" +  e.getMessage()); }
                catch (Exception e) { Log.d(TAG, "Error" +  e.getMessage()); }
                return EntityUtils.toString(httpEntity);
            } catch (Exception e)
            {
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            ListView myListView = getListView();

            final String[] EARTHQUAKES = new String[] { TAG_DETAIL_URL, TAG_MAG, TAG_PLACE, TAG_TIME };

            SimpleAdapter aa = new SimpleAdapter(MainActivity.this, earthquakeList, R.layout.list_item, EARTHQUAKES,new int[] { R.id.url_detail,R.id.magnitude,R.id.place, R.id.time } );
            myListView.setAdapter(aa);
            myListView.setTextFilterEnabled(true);
            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG,"position " + position);
                    TextView txt = (TextView) view.findViewById(R.id.url_detail);
                    String url = (String) txt.getText();
                    Log.d(TAG,"url: " + url);

                    Intent intent = new Intent(MainActivity.this, ShowDetailActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);

                }
            });
            // Launch AlertService
            Log.d(TAG, "Start Notification service");
            startService(new Intent(MainActivity.this, AlertService.class));
            Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(MainActivity.this, AlertService.class);
            PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            // Start service every hour
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 60*60*1000, pendingIntent);
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Seeking...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
