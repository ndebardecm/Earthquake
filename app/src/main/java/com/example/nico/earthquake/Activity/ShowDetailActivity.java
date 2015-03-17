package com.example.nico.earthquake.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.nico.earthquake.R;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nico on 17/03/2015.
 */
public class ShowDetailActivity extends ActionBarActivity {

    private static String TAG = "EARTHQUAKE";

    private static final String TAG_PROPERTY = "properties";
    private static final String TAG_MAG = "mag";
    private static final String TAG_PLACE = "place";
    private static final String TAG_TIME = "time";
    private static final String TAG_ALERT = "alert";
    private static final String TAG_TSUNAMI = "tsunami";
    private static final String TAG_TITLE = "title";
    private static final String TAG_GEO = "geometry";
    private static final String TAG_COORDINATES = "coordinates";
    private static final String TAG_LONG = "longitude";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_DEP = "depth";

    HashMap<String, String> detail = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        String url;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("url");
            Log.d(TAG, "url : " + url);

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            new WebServiceRequestor(url, param).execute();
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
            try {
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
                    JSONObject valProp = jsonObject.getJSONObject(TAG_PROPERTY);
                    String mag = valProp.getString(TAG_MAG);
                    String place = valProp.getString(TAG_PLACE);
                    long time = valProp.getLong(TAG_TIME);

                    Date timeDate = new Date();
                    timeDate.setTime(time);

                    String alert = valProp.getString(TAG_ALERT);
                    if(alert == "null"){
                        alert = "None";
                    }
                    int tsu = valProp.getInt(TAG_TSUNAMI);
                    String tsunami = "NON";
                    if (tsu == 1){
                        tsunami = "OUI";
                    }
                    String title = valProp.getString(TAG_TITLE);

                    JSONObject valGeo = jsonObject.getJSONObject(TAG_GEO);
                    JSONArray valCoord = valGeo.getJSONArray(TAG_COORDINATES);

                    double longitude = (double) valCoord.getDouble(0);
                    double latitude = (double) valCoord.getDouble(1);
                    double depth = (double) valCoord.getDouble(2);

                    detail.put(TAG_TITLE, title);
                    detail.put(TAG_MAG, "Magnitude : " +  mag);
                    detail.put(TAG_PLACE, "Place : " + place);
                    detail.put(TAG_TIME, "Time : " + timeDate.toString());
                    detail.put(TAG_ALERT, "Alert : " + alert);
                    detail.put(TAG_TSUNAMI, "Tsunami : " + tsunami);
                    detail.put(TAG_LONG, "Longitude : " + longitude);
                    detail.put(TAG_LAT, "Latitude : " + latitude);
                    detail.put(TAG_DEP, "Depth : " + depth);
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

            Log.d(TAG, "Set text detail");
            TextView txt = (TextView) findViewById(R.id.title);
            txt.setText(detail.get(TAG_TITLE));
            txt = (TextView) findViewById(R.id.magnitude);
            txt.setText(detail.get(TAG_MAG));
            txt = (TextView) findViewById(R.id.place);
            txt.setText(detail.get(TAG_PLACE));
            txt = (TextView) findViewById(R.id.time);
            txt.setText(detail.get(TAG_TIME));
            txt = (TextView) findViewById(R.id.alert);
            txt.setText(detail.get(TAG_ALERT));
            txt = (TextView) findViewById(R.id.tsunami);
            txt.setText(detail.get(TAG_TSUNAMI));
            txt = (TextView) findViewById(R.id.longitude);
            txt.setText(detail.get(TAG_LONG));
            txt = (TextView) findViewById(R.id.latitude);
            txt.setText(detail.get(TAG_LAT));
            txt = (TextView) findViewById(R.id.depth);
            txt.setText(detail.get(TAG_DEP));
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ShowDetailActivity.this);
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
