package com.project.squirrelobserver.squirrelObserver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.data.Actor;
import com.project.squirrelobserver.data.Behavior;
import com.project.squirrelobserver.data.DataAccessor;
import com.project.squirrelobserver.data.Record;
import com.project.squirrelobserver.util.GlobalVariables;
import com.project.squirrelobserver.util.Utils;

/**
 * Created by sean on 2/9/16.
 */
public  class RecordBehaviorTabActivity
        extends Activity {

//    private Record record = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_behaviors_tab);

        // Get Record from Intent
//        Intent intent = getIntent();
//        final Record intentRecord = (Record) intent.getSerializableExtra("Record");
//        record = intentRecord;

        // Create buttons for every behavior and place on activity
        if (DataAccessor.behaviors != null && !DataAccessor.behaviors.isEmpty()) {

            // Layout for grid
            TableLayout tableLayout = (TableLayout) findViewById(R.id.behaviorsTableLayout);
            TableRow tableRow = (TableRow) new TableRow(tableLayout.getContext());

            // Loop to run through all buttons
            for (int i = 0; i < DataAccessor.behaviors.size(); i++) {

                final Behavior behavior = DataAccessor.behaviors.get(i);

                final ToggleButton button = new ToggleButton(tableLayout.getContext());
                button.setText(behavior.desc);
                button.setTextOn(behavior.desc);
                button.setTextOff(behavior.desc);
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 15);

                // Keep a pointer to the button in the behavior
                behavior.button = button;

                // Attach the behavior to the button
                button.setTag(behavior);

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        if (button.isChecked()) {

                            // Button is ON
                            Behavior removedBehavior =
                                    GlobalVariables.currentRecord
                                            .addBehavior((Behavior) button.getTag());
//                            Behavior removedBehavior =
//                                    intentRecord.addBehavior((Behavior) button.getTag());

                            if (removedBehavior != null && removedBehavior.button != null) {

                                removedBehavior.button.setChecked(false);
                                removedBehavior.button.callOnClick();
                            }
                        } else {

                            // Button is OFF
                            GlobalVariables.currentRecord.removeBehavior(behavior);
//                            intentRecord.removeBehavior(behavior);
                        }
                    }
                });

                // If we have run through 4 elements, or if we're on the last button
                if ((i != 0 && i % 4 == 0) || i == DataAccessor.behaviors.size() - 1) {

                    tableLayout.addView(tableRow);
                    tableRow = new TableRow(tableLayout.getContext());
                }

                tableRow.addView(button);
            }
        }
    }

    // This is a listener for the physical back button, so we can display the verification dialog here too
    @Override
    public void onBackPressed() {

        Context context = RecordBehaviorTabActivity.this;

        // Close current activity after verify dialogue
        Utils.endRecordingSessionVerifyMessage(context, this);
    }

}
