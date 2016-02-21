package com.project.squirrelobserver.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;

import com.project.squirrelobserver.R;

import java.net.URISyntaxException;

/**
 * Created by sean on 2/13/16.
 */
public class Utils {

    public static void importButtonErrorMessage(Context context) {

        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.import_error_dialog_title))
                .setMessage(context.getResources().getString(R.string.import_error_dialog_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                    }
                })
                .show();
    }

    public static void exportButtonErrorMessage(Context context) {

        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.export_error_dialog_title))
                .setMessage(context.getResources().getString(R.string.export_error_dialog_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                    }
                })
                .show();
    }

    public static void clearButtonSuccessMessage(Context context) {

        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.clear_successful_title))
                .setMessage(context.getResources().getString(R.string.clear_successful_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                    }
                })
                .show();
    }

    public static void clearButtonWarningMessage(Context context, String csvPath) {

        // Close current activity after verify dialogue
        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.clear_warning_title))
                .setMessage(context.getResources().getString(R.string.clear_warning_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Clear file
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .show();

    }

    public static void endRecordingSessionVerifyMessage(Context context, final Activity activity) {

        // Close current activity after verify dialogue
        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.end_session_dialog_title))
                .setMessage(context.getResources().getString(R.string.end_session_dialog_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Close current record activity
                        activity.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .show();
    }

    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");

                if (cursor.moveToFirst()) {

                    return cursor.getString(column_index);
                }

            } catch (Exception e) { }

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {

            return uri.getPath();
        }

        return null;
    }
}
