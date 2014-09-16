package com.danyalvarez.primicia.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.URLEntity;

/**
 * Created by danyalvarez on 7/9/14.
 */
public class Util {

    public static void setTwitterAuthToken(String token, String secretToken, Context context) {
        SharedPreferences settings = context.getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("token", token);
        editor.putString("secretToken", secretToken);
        editor.commit();
    }

    public static String getTwitterAuthToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences("preferences", 0);
        return settings.getString("token", "");
    }

    public static String getTwitterAuthTokenSecret(Context context) {
        SharedPreferences settings = context.getSharedPreferences("preferences", 0);
        return settings.getString("secretToken", "");
    }

    public static String getElapsedTime(Date lastDate) {
        Date currentDate = Calendar.getInstance().getTime();

        long diff = currentDate.getTime() - lastDate.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);


        Log.i(Constants.TAG_DEBUG, diffDays + " days, ");
        Log.i(Constants.TAG_DEBUG, diffHours + " hours, ");
        Log.i(Constants.TAG_DEBUG, diffMinutes + " minutes, ");
        Log.i(Constants.TAG_DEBUG, diffSeconds + " seconds.");

        if (diffDays > 1) {
            return "Hace " + diffDays + " días";
        } else if (diffHours > 1) {
            return "Hace " + diffHours + " horas";
        } else if (diffMinutes > 1) {
            return "Hace " + diffMinutes + " minutos";
        } else {
            return "Hace " + diffSeconds + " segundos";
        }
    }


    public static String getText(Status status) {
        String text = status.getText();

//        URLEntity[] urlEntities = status.getURLEntities();
//        if (urlEntities.length > 0) {
//            for (URLEntity entity : urlEntities) {
//                int startLink = entity.getStart();
//                int endLink = entity.getEnd();
//
//                String visibleLink = entity.getDisplayURL();
//
//                try {
//                    text = text.replace(text.substring(startLink, endLink), visibleLink);
//                } catch (Exception ex) {
//                    Log.e(Constants.TAG_DEBUG, "start:" + startLink + " end:"+ endLink);
//                    Log.e(Constants.TAG_DEBUG, text);
//                }
//
//            }
//        }

        MediaEntity[] mediaEntities = status.getMediaEntities();
        if (mediaEntities.length > 0) {
            for (MediaEntity entity : mediaEntities) {
//                entity.getMediaURL();
                String link = entity.getURL();
                Log.i(Constants.TAG_DEBUG, ">> " + link);
                text = text.replace(link, "");
            }
        }
        return text;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return  (networkInfo != null && networkInfo.isConnected());
    }
}
