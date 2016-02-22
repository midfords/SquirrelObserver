package com.project.squirrelobserver.squirrelObserver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.util.FileParser;
import com.project.squirrelobserver.util.GlobalVariables;
import com.project.squirrelobserver.util.LocationPoint;
import com.project.squirrelobserver.util.Record;
import com.project.squirrelobserver.util.Utils;

import java.util.ArrayList;

/**
 * Created by sean on 2/9/16.
 */
public  class RecordOtherTabActivity
        extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_other_tab);

        Spinner xSpinner = (Spinner) findViewById(R.id.location_x_spinner);

        ArrayAdapter<LocationPoint> x_list = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, GlobalVariables.locationPointsX);
        x_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        xSpinner.setAdapter(x_list);
        xSpinner.setSelection(0);

        Spinner ySpinner = (Spinner) findViewById(R.id.location_y_spinner);

        ArrayAdapter<LocationPoint> y_list = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, GlobalVariables.locationPointsY);
        y_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ySpinner.setAdapter(y_list);
        ySpinner.setSelection(0);

        Button recordButton = (Button) findViewById(R.id.record_button);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get all values for record
                EditText groupSize = (EditText) findViewById(R.id.group_size);
                EditText xMod = (EditText) findViewById(R.id.x_mod_distance);
                EditText yMod = (EditText) findViewById(R.id.y_mod_distance);

                GlobalVariables.currentRecord.groupSize = Integer.parseInt(groupSize.getText().toString());
                Spinner xSpinner = (Spinner) findViewById(R.id.location_x_spinner);
                Spinner ySpinner = (Spinner) findViewById(R.id.location_y_spinner);

                double x, y;

                x = ((LocationPoint) xSpinner.getSelectedItem()).x + (Double.parseDouble(xMod.getText().toString()));
                y = ((LocationPoint) ySpinner.getSelectedItem()).y + (Double.parseDouble(yMod.getText().toString()));

                GlobalVariables.currentRecord.x = x;
                GlobalVariables.currentRecord.y = y;

                // Write record to file
                String csvScanPath =
                        v.getContext().getFilesDir().getPath() + "/" + GlobalVariables.csvScanFileName;
                String csvAOPath =
                        v.getContext().getFilesDir().getPath() + "/" + GlobalVariables.csvAOFileName;

                boolean writeToAOFile = false;

                // Check if we need to also write record to ao file
                for (int i = 0; GlobalVariables.aoBehaviors != null && i < GlobalVariables.aoBehaviors.size(); i++) {

                    if (GlobalVariables.currentRecord != null
                            && GlobalVariables.aoBehaviors != null
                            && GlobalVariables.currentRecord.containsBehavior(GlobalVariables.aoBehaviors.get(i))) {

                        writeToAOFile = true;
                    }
                }

                boolean aoWriteResult = true;
                boolean scanWriteResult =
                        FileParser.writeLineToRecordCSV(csvScanPath, GlobalVariables.currentRecord);

                if (writeToAOFile)
                    aoWriteResult = FileParser.writeLineToRecordCSV(csvAOPath, GlobalVariables.currentRecord);

                // If either write failed, show error message to user
                if (!aoWriteResult || !scanWriteResult) {

                    // Show error dialog
                    Utils.writeRecordErrorMessage(v.getContext());

                } else {

                    // Reset everything for next record
                    Record newRecord =
                            new Record(GlobalVariables.currentRecord.observerID, GlobalVariables.currentRecord.aoOnly);
                    GlobalVariables.currentRecord = newRecord;
                    // Update tab enabled states
                    RecordActivity parentActivity = (RecordActivity) getParent();
                    if (parentActivity != null) {

                        parentActivity.updateTabEnabledState();
                        parentActivity.switchTabView(0);
                    }
                }
            }
        });
    }

    // This is a listener for the physical back button, so we can display the verification dialog here too
    @Override
    public void onBackPressed() {

        Context context = RecordOtherTabActivity.this;

        // Close current activity after verify dialogue
        Utils.endRecordingSessionVerifyMessage(context, this);
    }


    public void groupSizeButtonClick(View v) {
        EditText groupSizeText = (EditText) findViewById(R.id.group_size);
        Button btn = (Button) v;
        groupSizeText.setText(btn.getText());
    }


}
