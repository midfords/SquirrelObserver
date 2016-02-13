package com.project.squirrelobserver.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by sean on 2/13/16.
 */
public class Utils {

    public static void endRecordingSessionVerifyMessage(Context context, final Activity activity) {

        // Close current activity after verify dialogue
        new AlertDialog.Builder(context)
                .setTitle("End Recording Session")
                .setMessage("Are you sure you want to end the recording session? All observations are already saved.")
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
}
