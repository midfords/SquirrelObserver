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
import android.widget.ToggleButton;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.util.Behavior;
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

                if(groupSize.getText().toString().equals(null) || groupSize.getText().toString().equals("")){
                    GlobalVariables.currentRecord.groupSize = 1;
                } else {
                    GlobalVariables.currentRecord.groupSize = Integer.parseInt(groupSize.getText().toString());
                }

                double x, y;

                if(xSpinner.getAdapter().equals(null)) {
                    x = 0;
                } else {
                    if(xMod.getText().toString().equals(null) || xMod.getText().toString().equals("")) {
                        xMod.setText("0");
                    }
                    x = ((LocationPoint) xSpinner.getSelectedItem()).x + (Double.parseDouble(xMod.getText().toString()));
                }
                if(ySpinner.getAdapter().equals(null)) {
                    y = 0;
                } else {
                    if(yMod.getText().toString().equals(null) || yMod.getText().toString().equals("")) {
                        yMod.setText("0");
                    }
                    y = ((LocationPoint) ySpinner.getSelectedItem()).y + (Double.parseDouble(yMod.getText().toString()));
                }

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

                    // Reset button toggles, disable actor button
                    for (int i = 0; i < GlobalVariables.currentRecord.behaviorsSize; i++) {

                        Behavior recordBehavior = GlobalVariables.currentRecord.getBehavior(i);

                        // Update most recent and most frequent lists
                        recordBehavior.numTimeUsed++;

                        // Replace item if numTimeUsed is higher
                        if (GlobalVariables.behaviorButtons != null
                            && GlobalVariables.behaviorButtons.contains(recordBehavior.button)) {

                            boolean placedItem = false;
                            for (int j = 0; !placedItem && j < GlobalVariables.behaviorFrequentButtons.size(); j++) {

                                ToggleButton frequentButtonToCheck = GlobalVariables.behaviorFrequentButtons.get(j);
                                Behavior frequentBehaviorToCheck = (Behavior) frequentButtonToCheck.getTag();

                                // Check if current button count is higher
                                if (frequentBehaviorToCheck != null
                                        && frequentBehaviorToCheck.numTimeUsed < recordBehavior.numTimeUsed) {

                                    ToggleButton oldFrequent =
                                            GlobalVariables.behaviorFrequentButtons.remove(j);

                                    GlobalVariables.behaviorButtons.remove(recordBehavior.button);
                                    GlobalVariables.behaviorFrequentButtons.add(recordBehavior.button);

                                    // Place old frequent back in button list
                                    GlobalVariables.behaviorButtons.add(oldFrequent);

                                    placedItem = true;
                                }
                            }
                        }

                        // Uncheck button
                        if (recordBehavior.button != null
                                && recordBehavior.button.isChecked()) {
                            GlobalVariables.currentRecord.getBehavior(i).button.setChecked(false);
                            GlobalVariables.currentRecord.getBehavior(i).button.callOnClick();
                        }
                    }

                    // Update most recent actor list if item selected was in main list
                    if (GlobalVariables.currentRecord != null
                            && GlobalVariables.currentRecord.actor != null
                            && GlobalVariables.currentRecord.actor.actorButton != null
                            && GlobalVariables.actorRecentButtons != null
                            && GlobalVariables.actorButtons != null
                            && GlobalVariables.actorButtons.contains(
                                GlobalVariables.currentRecord.actor.actorButton)) {

                        // Put old recent back in main button list
                        ToggleButton oldRecent = GlobalVariables.actorRecentButtons.remove(0);
                        GlobalVariables.actorButtons.add(oldRecent);

                        // Remove used button from buttons and add to recent buttons
                        GlobalVariables.actorButtons.remove(
                                GlobalVariables.currentRecord.actor.actorButton);
                        GlobalVariables.actorRecentButtons.add(
                                GlobalVariables.currentRecord.actor.actorButton);
                    }

                    // Uncheck and disable actor button
                    if (GlobalVariables.currentRecord != null
                            && GlobalVariables.currentRecord.actor != null
                            && GlobalVariables.currentRecord.actor.actorButton != null
                            && GlobalVariables.currentRecord.actor.actorButton.isChecked()) {

                        GlobalVariables.currentRecord.actor.actorButton.setChecked(false);
                        GlobalVariables.currentRecord.actor.actorButton.setEnabled(false);
                        GlobalVariables.currentRecord.actor.actorButton.callOnClick();
                    }

                    // Update most recent actee list
                    if (GlobalVariables.currentRecord != null
                            && GlobalVariables.currentRecord.actee != null
                            && GlobalVariables.currentRecord.actee.acteeButton != null
                            && GlobalVariables.acteeRecentButtons != null
                            && GlobalVariables.acteeButtons != null
                            && GlobalVariables.acteeButtons.contains(
                                GlobalVariables.currentRecord.actee.acteeButton)) {

                        // Put old recent back in main button list
                        ToggleButton oldRecent = GlobalVariables.acteeRecentButtons.remove(0);
                        GlobalVariables.acteeButtons.add(oldRecent);

                        // Remove used button from buttons and add to recent buttons
                        GlobalVariables.acteeButtons.remove(
                                GlobalVariables.currentRecord.actee.acteeButton);
                        GlobalVariables.acteeRecentButtons.add(
                                GlobalVariables.currentRecord.actee.acteeButton);
                    }

                    // Uncheck actee button
                    if (GlobalVariables.currentRecord != null
                            && GlobalVariables.currentRecord.actee != null
                            && GlobalVariables.currentRecord.actee.acteeButton != null
                            && GlobalVariables.currentRecord.actee.acteeButton.isChecked()) {

                        GlobalVariables.currentRecord.actee.acteeButton.setChecked(false);
                        GlobalVariables.currentRecord.actee.acteeButton.callOnClick();
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
