package com.example.nico.earthquake.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.example.nico.earthquake.Activity.MainActivity;
import com.example.nico.earthquake.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nico on 17/03/2015.
 */

public class AlertService extends Service {

    private static String TAG = "EARTHQUAKE";
    public int count;

    private static String urlHourSignificante = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_hour.geojson";

    private static final String TAG_META = "metadata";
    private static final String TAG_COUNT = "count";
    private static final String TAG_STATUS = "status";

    public AlertService(){}

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Service created 2");
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        new WebServiceRequestor(urlHourSignificante, param).execute();
    }

    private class WebServiceRequestor extends AsyncTask<String, Void, String> {
        String URL;
        List<NameValuePair> parameters;

        public WebServiceRequestor(String url, List<NameValuePair> params) {
            this.URL = url;
            this.parameters = params;
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "Doing in background");
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;
                HttpPost httpPost = new HttpPost(URL);
                if (parameters != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(parameters));
                }
                httpResponse = httpClient.execute(httpPost);
                httpEntity = httpResponse.getEntity();
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpEntity));

                try {
                    JSONObject valMeta = jsonObject.getJSONObject(TAG_META);
                    int countTemp = valMeta.getInt(TAG_COUNT);
                    int status = valMeta.getInt(TAG_STATUS);

                    if (countTemp != 0 && status == 200) {
                        count = countTemp;
                        Log.d(TAG, "found : "+count);
                        createNotification(count);
                    } else {
                        Log.d(TAG, "No earthquake matching criteria");
                    }

                } catch (JSONException e) {
                    Log.d(TAG, "Error : " + e.getMessage());
                } catch (Exception e) {
                    Log.d(TAG, "Error : " + e.getMessage());
                }
                return EntityUtils.toString(httpEntity);
            } catch (Exception e) {
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "Done");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }

    public void createNotification(int count) {
        Log.d(TAG, "Creating notification");
        Intent intent = new Intent(this, MainActivity.class);
        //Testing with this values to see anything...
        intent.putExtra("earthquake", 1);
        intent.putExtra("time", 0);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Build notification
        Notification notification = new Notification.Builder(this)
                .setContentTitle("New EarthQuake")
                .setContentText(count + " new earthquakes have been detected")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent).getNotification();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);
        Log.d(TAG, "Notified");
    }
}
