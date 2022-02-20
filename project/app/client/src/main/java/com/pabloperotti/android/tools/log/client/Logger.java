package com.pabloperotti.android.tools.log.client;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Add entries to the log database through the content provider
 */
public class Logger {

    public static final String TAG = Logger.class.getSimpleName();

    //Constants used by the logger
    interface DatabaseContract {
        String COLUMN_TIMESTAMP = "timestamp";
        String COLUMN_TAG = "tag";
        String COLUMN_VALUE = "value";
    }

    /**
     * Defines the contract for this provider
     */
    interface DataContract {
        String SCHEME = "content://";
        String AUTHORITY = "com.pabloperotti.android.tools.log.data.provider";
        String PATH_ITEMS = "/items";
        Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_ITEMS);
    }

    private static ContentResolver sContentResolver;

    private Logger() {
    }

    /**
     * Obtain the reference to the content resolver to be used for logging events
     *
     * @param context Application Context
     */
    public static void init(Context context) {
        sContentResolver = context.getContentResolver();
    }

    /**
     * Remove all the information available in the DB
     */
    public static void removeAll() {
        if (sContentResolver != null) {
            try {
                sContentResolver.delete(
                        DataContract.CONTENT_URI, null, null);
            } catch (Exception e) {
                Log.d(TAG, "" + e.toString());
            }
        }
    }

    /**
     * Add an entry to the log
     *
     * @param tag     Tag as used in Android's log utility
     * @param message String used as in Android's log utility
     */
    public static void add(String tag, String message) {
        if (tag != null) {
            Log.d(tag, "" + message);
        }
        if (sContentResolver != null && tag != null && message != null) {

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

            ContentValues values = new ContentValues();
            values.put(DatabaseContract.COLUMN_TIMESTAMP, sdf.format(c.getTime()));
            values.put(DatabaseContract.COLUMN_TAG, tag);
            values.put(DatabaseContract.COLUMN_VALUE, message);

            try {
                Uri newUri = sContentResolver.insert(
                        DataContract.CONTENT_URI, values);
            } catch (Exception e) {
                Log.d(tag, "" + e.toString());
            }
        }
    }
}
