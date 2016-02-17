package com.project.squirrelobserver.squirrelObserver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.data.Actor;
import com.project.squirrelobserver.data.DataAccessor;
import com.project.squirrelobserver.util.Utils;

/**
 * Created by sean on 2/9/16.
 */
public  class RecordActeeTabActivity
        extends Activity {

    private ToggleButton previousButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_actee_tab);

        // Create buttons for every behavior and place on activity
        if (DataAccessor.actors != null && !DataAccessor.actors.isEmpty()) {

            // Layout for grid
            TableLayout tableLayout = (TableLayout) findViewById(R.id.acteeTableLayout);
            TableRow tableRow = (TableRow) new TableRow(tableLayout.getContext());

            TableRow.LayoutParams params = new TableRow.LayoutParams();
            params.setMargins(5, 5, 5, 5);

            // Loop to run through all buttons
            for (int i = 0; i < DataAccessor.actors.size(); i++) {

                final Actor actor = DataAccessor.actors.get(i);

                final ToggleButton button = new ToggleButton(tableLayout.getContext());
                button.setText(actor.name);
                button.setTextOn(actor.name);
                button.setTextOff(actor.name);
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 15);
                button.setLayoutParams(params);

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        if (button.isChecked()) {

                            if (previousButton != null && button != previousButton) {

                                previousButton.setChecked(false);
                                previousButton.callOnClick();
                            }
                            previousButton = button;

                            // Button is ON
                            if (actor.sex == 1) {

                                button.setBackgroundColor(Color.parseColor("#99ccff"));
                            } else {

                                button.setBackgroundColor(Color.parseColor("#ffb3ff"));
                            }
                        } else {

                            previousButton = null;

                            // Button is OFF
                            if (actor.sex == 1) {

                                button.setBackgroundColor(Color.parseColor("#e6f2ff"));
                            } else {

                                button.setBackgroundColor(Color.parseColor("#ffe6ff"));
                            }
                        }
                    }
                });

                if (actor.sex == 1) {

                    button.setBackgroundColor(Color.parseColor("#e6f2ff"));
                } else {

                    button.setBackgroundColor(Color.parseColor("#ffe6ff"));
                }

                // If we have run through 4 elements, or if we're on the last button
                if ((i != 0 && i % 4 == 0) || i == DataAccessor.actors.size() - 1) {

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
