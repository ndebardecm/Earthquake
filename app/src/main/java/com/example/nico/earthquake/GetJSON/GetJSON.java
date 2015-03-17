package com.example.nico.earthquake.GetJSON;

import android.util.Log;

/**
 * Created by nico on 17/03/2015.
 */
public class GetJSON {
    private static String TAG = "EARTHQUAKE";
    /**
     * On récupère les infos directement aux bonnes URL
     */

    //LAST HOUR
    private static String urlHourSignificante = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_hour.geojson";
    private static String urlHourAll = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";
    private static String urlHourM4 = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_hour.geojson";
    private static String urlHourM2 = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_hour.geojson";
    private static String urlHourM1 = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/1.0_hour.geojson";

    //LAST DAY
    private static String urlDaySignificante = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_day.geojson";
    private static String urlDayAll = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";
    private static String urlDayM4 = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_day.geojson";
    private static String urlDayM2 = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_day.geojson";
    private static String urlDayM1 = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/1.0_day.geojson";

    //LAST WEEK
    private static String urlWeekSignificante = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_week.geojson";
    private static String urlWeekAll = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.geojson";
    private static String urlWeekM4 = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.geojson";
    private static String urlWeekM2 = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.geojson";
    private static String urlWeekM1 = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/1.0_week.geojson";

    //LAST MONTH
    private static String urlMonthSignificante = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_month.geojson";
    private static String urlMonthAll = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson";
    private static String urlMonthM4 = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_month.geojson";
    private static String urlMonthM2 = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_month.geojson";
    private static String urlMonthM1 = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/1.0_month.geojson";


    public static String getUrl(int time, int magnitude){
        switch (time){
            case 0:
                Log.d(TAG, "hour");
                switch (magnitude){
                    case 0:
                        Log.d(TAG, "Significant");
                        return urlHourSignificante;
                    case 1:
                        Log.d(TAG, "All");
                        return urlHourAll;
                    case 2:
                        Log.d(TAG, "M4");
                        return urlHourM4;
                    case 3:
                        Log.d(TAG, "M2");
                        return urlHourM2;
                    case 4:
                        Log.d(TAG, "M1");
                        return urlHourM1;
                }
            case 1:
                Log.d(TAG, "day");
                switch (magnitude){
                    case 0:
                        Log.d(TAG, "Significant");
                        return urlDaySignificante;
                    case 1:
                        Log.d(TAG, "All");
                        return urlDayAll;
                    case 2:
                        Log.d(TAG, "M4");
                        return urlDayM4;
                    case 3:
                        Log.d(TAG, "M2");
                        return urlDayM2;
                    case 4:
                        Log.d(TAG, "M1");
                        return urlDayM1;
                }
            case 2:
                Log.d(TAG, "7");
                switch (magnitude){
                    case 0:
                        Log.d(TAG, "Significant");
                        return urlWeekSignificante;
                    case 1:
                        Log.d(TAG, "All");
                        return urlWeekAll;
                    case 2:
                        Log.d(TAG, "M4");
                        return urlWeekM4;
                    case 3:
                        Log.d(TAG, "M2");
                        return urlWeekM2;
                    case 4:
                        Log.d(TAG, "M1");
                        return urlWeekM1;
                }
            case 3:
                Log.d(TAG, "30");
                switch (magnitude){
                    case 0:
                        Log.d(TAG, "Significant");
                        return urlMonthSignificante;
                    case 1:
                        Log.d(TAG, "All");
                        return urlMonthAll;
                    case 2:
                        Log.d(TAG, "M4");
                        return urlMonthM4;
                    case 3:
                        Log.d(TAG, "M2");
                        return urlMonthM2;
                    case 4:
                        Log.d(TAG, "M1");
                        return urlMonthM1;
                }
        }
        return null;
    }
}
