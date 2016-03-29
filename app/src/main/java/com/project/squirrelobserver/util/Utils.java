package com.project.squirrelobserver.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.project.squirrelobserver.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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

    public static void writeRecordErrorMessage(Context context) {

        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.record_error_dialog_title))
                .setMessage(context.getResources().getString(R.string.record_error_dialog_message))
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

    public static void clearButtonErrorMessage(Context context) {

        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.clear_error_title))
                .setMessage(context.getResources().getString(R.string.clear_error_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                    }
                })
                .show();
    }

    public static void clearButtonWarningMessage(final Context context, final String csvFilPath) {

        // Close current activity after verify dialogue
        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.clear_warning_title))
                .setMessage(context.getResources().getString(R.string.clear_warning_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Clear file
                        if (FileParser.clearRecordCSV(csvFilPath)) {

                            Utils.clearButtonSuccessMessage(context);
                        } else {

                            Utils.clearButtonErrorMessage(context);
                        }
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
            String[] projection = {MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DISPLAY_NAME, MediaStore.Files.FileColumns.MIME_TYPE};//{ "_data" };
            Cursor cursor = null;
            String displayName;
            String destination;
            try {
                cursor = context.getContentResolver().query(uri, null, null, null, null);

                if (cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    destination = GlobalVariables.exportDownloadPath + displayName;//context.getFilesDir().getPath() + "/" + displayName;
                    String extension = displayName.substring(displayName.lastIndexOf(".")+1);

                    if("csv".equalsIgnoreCase(extension)) {
                        File file = new File(destination);
                        file.mkdirs();
                        InputStream in = context.getContentResolver().openInputStream(uri);

                        if(!file.exists()) {
                            file.createNewFile();
                        } else {
                            file.delete();
                            file.createNewFile();
                        }
                        OutputStream out = new FileOutputStream(destination);

                        // Transfer bytes from in to out
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        in.close();
                        out.close();
                    } else {
                        destination = "";
                    }
                    return destination;
                }

            } catch (Exception e) {
                String error = e.toString();
                e.printStackTrace();
            }

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {

            return uri.getPath();
        }

        return null;
    }
}
