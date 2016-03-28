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
import android.widget.TextView;
import android.widget.ToggleButton;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.util.Behavior;
import com.project.squirrelobserver.util.FileParser;
import com.project.squirrelobserver.util.GlobalVariables;
import com.project.squirrelobserver.util.LocationPoint;
import com.project.squirrelobserver.util.Record;
import com.project.squirrelobserver.util.Utils;

import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

/**
 * Created by sean on 2/9/16.
 */
public  class RecordOtherTabActivity
        extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_other_tab);

        // Set reference to self in parent activity
        RecordActivity recordActivity = (RecordActivity) this.getParent();
        recordActivity.otherTabActivity = RecordOtherTabActivity.this;

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

        EditText xModEditText = (EditText) findViewById(R.id.x_mod_distance);
        EditText yModEditText = (EditText) findViewById(R.id.y_mod_distance);
        xModEditText.setNextFocusDownId(R.id.y_mod_distance);
        yModEditText.setNextFocusDownId(R.id.group_size);

        final Button recordButton = (Button) findViewById(R.id.record_button);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get all values for record
                EditText groupSize = (EditText) findViewById(R.id.group_size);
                EditText xMod = (EditText) findViewById(R.id.x_mod_distance);
                EditText yMod = (EditText) findViewById(R.id.y_mod_distance);
                Spinner xSpinner = (Spinner) findViewById(R.id.location_x_spinner);
                Spinner ySpinner = (Spinner) findViewById(R.id.location_y_spinner);

                if(groupSize.getText().toString() == null || groupSize.getText().toString().equals("")){
                    GlobalVariables.currentRecord.groupSize = 1;
                } else {
                    GlobalVariables.currentRecord.groupSize = Integer.parseInt(groupSize.getText().toString());
                }

                int x, y;

                if(xSpinner.getAdapter() == null) {
                    x = 0;
                } else {
                    if(xMod.getText().toString() == null || xMod.getText().toString().equals("")) {
                        xMod.setText("0");
                    }
                    x = GlobalVariables.originX + ((LocationPoint) xSpinner.getSelectedItem()).x + (Integer.parseInt(xMod.getText().toString()));
                }
                if(ySpinner.getAdapter() == null) {
                    y = 0;
                } else {
                    if(yMod.getText().toString() == null || yMod.getText().toString().equals("")) {
                        yMod.setText("0");
                    }
                    y = GlobalVariables.originY + ((LocationPoint) ySpinner.getSelectedItem()).y + (Integer.parseInt(yMod.getText().toString()));
                }

                GlobalVariables.currentRecord.x = x;
                GlobalVariables.currentRecord.y = y;

                GlobalVariables.currentRecord.scanInterval = GlobalVariables.scanInterval;

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
                boolean scanWriteResult = true;

                if (!GlobalVariables.currentRecord.aoOnly)
                    scanWriteResult = FileParser.writeLineToRecordCSV(csvScanPath, GlobalVariables.currentRecord);

                if (writeToAOFile || GlobalVariables.currentRecord.aoOnly)
                    aoWriteResult = FileParser.writeLineToRecordCSV(csvAOPath, GlobalVariables.currentRecord);

                // If either write failed, show error message to user
                if (!aoWriteResult || !scanWriteResult) {

                    // Show error dialog
                    Utils.writeRecordErrorMessage(v.getContext());

                } else {

                    RecordActivity parentActivity = (RecordActivity) getParent();

                    // Update behavior frequent buttons
                    if (parentActivity != null && parentActivity.behaviorTabActivity != null) {

                        switch (GlobalVariables.currentRecord.behaviorsSize) {

                            case 3:
                                Behavior b2 = GlobalVariables.currentRecord.getBehavior(2);
                                parentActivity.behaviorTabActivity.uncheckButton(b2.button);
                                parentActivity.behaviorTabActivity.updateFrequentButtons(b2);

                            case 2:
                                Behavior b1 = GlobalVariables.currentRecord.getBehavior(1);
                                parentActivity.behaviorTabActivity.uncheckButton(b1.button);
                                parentActivity.behaviorTabActivity.updateFrequentButtons(b1);

                            case 1:
                                Behavior b0 = GlobalVariables.currentRecord.getBehavior(0);
                                parentActivity.behaviorTabActivity.uncheckButton(b0.button);
                                parentActivity.behaviorTabActivity.updateFrequentButtons(b0);
                        }
                    }

                    // Update actor tab button
                    if (parentActivity != null
                            && parentActivity.actorTabActivity != null
                            && GlobalVariables.currentRecord != null
                            && GlobalVariables.currentRecord.actor != null
                            && GlobalVariables.currentRecord.actor.actorButton != null) {

                        ToggleButton actorButton = GlobalVariables.currentRecord.actor.actorButton;

                        parentActivity.actorTabActivity.uncheckButton(actorButton);

                        if(parentActivity.scanMode) {
                            parentActivity.actorTabActivity.disableButton(actorButton);
                        }
                        parentActivity.actorTabActivity.updateRecentButtons(actorButton);
                    }

                    // Update actee tab button
                    if (parentActivity != null
                            && parentActivity.acteeTabActivity != null
                            && GlobalVariables.currentRecord != null
                            && GlobalVariables.currentRecord.actee != null
                            && GlobalVariables.currentRecord.actee.acteeButton!= null) {

                        ToggleButton acteeButton = GlobalVariables.currentRecord.actee.acteeButton;

                        parentActivity.acteeTabActivity.uncheckButton(acteeButton);
                        parentActivity.acteeTabActivity.updateRecentButtons(acteeButton);
                    }

                    // Reset everything for next record
                    GlobalVariables.currentRecord =
                            new Record(GlobalVariables.currentRecord.observerID, GlobalVariables.currentRecord.aoOnly);

                    // Reset Other tab fields
                    xSpinner.setSelection(0);
                    ySpinner.setSelection(0);
                    groupSize.setText("1");
                    xMod.setText("");
                    yMod.setText("");

                    // Update tab enabled states
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
