package com.project.squirrelobserver.squirrelObserver;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.util.Actor;
import com.project.squirrelobserver.util.FileParser;
import com.project.squirrelobserver.util.GlobalVariables;
import com.project.squirrelobserver.util.Utils;

/**
 * Created by sean on 2/9/16.
 */
public  class RecordActeeTabActivity
        extends Activity {

    private ToggleButton previousButton = null;
//    private Record record = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_actee_tab);

        // Get Record from Intent
//        Intent intent = getIntent();
//        final Record intentRecord = (Record) intent.getSerializableExtra("Record");
//        record = intentRecord;

        // Create buttons for every behavior and place on activity
        if (GlobalVariables.actors != null && !GlobalVariables.actors.isEmpty()) {

            // Layout for grid
            TableLayout tableLayout = (TableLayout) findViewById(R.id.acteeTableLayout);
            TableRow tableRow = (TableRow) new TableRow(tableLayout.getContext());

            TableRow.LayoutParams params = new TableRow.LayoutParams();
            params.setMargins(5, 5, 5, 5);

            // Loop to run through all buttons
            for (int i = 0; i < GlobalVariables.actors.size(); i++) {

                final Actor actee = GlobalVariables.actors.get(i);

                final ToggleButton button = new ToggleButton(tableLayout.getContext());
                button.setText(actee.name);
                button.setTextOn(actee.name);
                button.setTextOff(actee.name);
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 15);
                button.setLayoutParams(params);

                // Attach the actee to the button
                button.setTag(actee);

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        if (button.isChecked()) {

                            // Button is ON
                            if (previousButton != null && button != previousButton) {

                                previousButton.setChecked(false);
                                previousButton.callOnClick();
                            }
                            previousButton = button;

                            GlobalVariables.currentRecord.actee = (Actor) button.getTag();
//                            intentRecord.actee = (Actor) button.getTag();

                            if (actee.sex == 1) {

                                button.setBackgroundColor(
                                        getResources().getColor(R.color.actor_button_male_selected));
                            } else {

                                button.setBackgroundColor(
                                        getResources().getColor(R.color.actor_button_female_selected));
                            }
                        } else {

                            // Button is OFF
                            previousButton = null;
                            GlobalVariables.currentRecord.actee = null;
//                            intentRecord.actee = null;

                            if (actee.sex == 1) {

                                button.setBackgroundColor(
                                        getResources().getColor(R.color.actor_button_male_not_selected));
                            } else {

                                button.setBackgroundColor(
                                        getResources().getColor(R.color.actor_button_female_not_selected));
                            }
                        }
                    }
                });

                if (actee.sex == 1) {

                    button.setBackgroundColor(
                            getResources().getColor(R.color.actor_button_male_not_selected));
                } else {

                    button.setBackgroundColor(
                            getResources().getColor(R.color.actor_button_female_not_selected));
                }

                // If we have run through 4 elements, or if we're on the last button
                if ((i != 0 && i % 4 == 0) || i == GlobalVariables.actors.size() - 1) {

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

        Context context = RecordActeeTabActivity.this;

        // Close current activity after verify dialogue
        Utils.endRecordingSessionVerifyMessage(context, this);
    }

}
