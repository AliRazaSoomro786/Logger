package com.mpm.mylibrary;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;


public class TdmLogger {
    private final Context context;

    private static final String CONTENT_TRANSIT_URL = "content://transit.provider";
    public static final Uri CONTENT_TRANSIT_URI = Uri.parse(CONTENT_TRANSIT_URL);
    public static final String TRANSIT_OBJECT = "transit_object";

    public TdmLogger(Context context) {
        this.context = context;
    }

    public void record(String level, String message, Object object) {
        try {
            String transitObject = getTransitObject();

            JSONObject jsonObject;

            if (transitObject.isEmpty()) jsonObject = new JSONObject();
            else jsonObject = new JSONObject(transitObject);

            JSONObject logsObject = new JSONObject();
            logsObject.put("level", level);
            logsObject.put("message", message);

            logsObject.put("obj", object);
            jsonObject.put("logs", logsObject);

            saveObject(jsonObject);
        } catch (Exception e) {
            Log.e("LogManager", "something went wrong", e);
        }


    }

    private String getTransitObject() {
        try {
            Cursor cursor = context.getContentResolver().query(CONTENT_TRANSIT_URI, null, TRANSIT_OBJECT,
                    null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) cursor.getString(0);
                cursor.close();
            }

            return "";
        } catch (Exception e) {
            return "";
        }

    }

    private void saveObject(JSONObject jsonObject) {
        ContentValues cv = new ContentValues();

        cv.put(TRANSIT_OBJECT, jsonObject.toString());
        context.getContentResolver().insert(CONTENT_TRANSIT_URI, cv);
    }
}
