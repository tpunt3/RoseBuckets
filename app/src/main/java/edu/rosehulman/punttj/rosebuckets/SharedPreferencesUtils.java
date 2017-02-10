package edu.rosehulman.punttj.rosebuckets;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by punttj on 2/1/2017.
 */

public class SharedPreferencesUtils {

    public static String getCurrentUserName(Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        return prefs.getString(Constants.NAME_KEY, "");
    }


    public static void setCurrentUserName(Context context, String name) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.NAME_KEY, name);
        editor.commit();
    }


    public static String getCurrentUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        return prefs.getString(Constants.UID_KEY, "");
    }

    public static void setCurrentUser(Context context, String uid) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.UID_KEY, uid);
        editor.commit();
    }

    public static String getCurrentBucketList(Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        return prefs.getString(Constants.BL_UID, "");
    }

    public static void setCurrentBucketList(Context context, String uid){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.BL_UID, uid);
        editor.commit();
    }

    public static String getCurrentBucketListItem(Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        return prefs.getString(Constants.ITEM_UID, "");
    }

    public static void setCurrentBucketListItem(Context context, String uid){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.ITEM_UID, uid);
        editor.commit();
    }

    public static void removeCurrentUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constants.UID_KEY);
        editor.apply();
    }

    public static void setCurrentSubItem(Context context, String uid){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.SUBITEM_UID, uid);
        editor.commit();
    }

    public static String getCurrentSubItem(Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        return prefs.getString(Constants.SUBITEM_UID, "");
    }
}
