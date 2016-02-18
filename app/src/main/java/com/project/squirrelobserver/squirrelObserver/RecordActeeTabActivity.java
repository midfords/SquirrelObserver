package com.project.squirrelobserver.squirrelObserver;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.adapters.ButtonAdapter;
import com.project.squirrelobserver.util.Actor;
import com.project.squirrelobserver.util.GlobalVariables;
import com.project.squirrelobserver.util.Utils;

import java.util.ArrayList;

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
        if (GlobalVariables.actors != null && !GlobalVariables.actors.isEmpty()) {

            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.acteeTabRelativeLayout);
            final GridView gridView = new GridView(RecordActeeTabActivity.this);
            final ArrayList<ToggleButton> list = new ArrayList<ToggleButton>();

            // Loop to run through all buttons
            for (int i = 0; i < GlobalVariables.actors.size(); i++) {

                final Actor actee = GlobalVariables.actors.get(i);

                final ToggleButton button = new ToggleButton(gridView.getContext());
                button.setText(actee.name);
                button.setTextOn(actee.name);
                button.setTextOff(actee.name);
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 15);

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

                            // Set button colour based on actor sex (selected)
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

                            // Set button colour based on actor sex (not selected)
                            if (actee.sex == 1) {
                                button.setBackgroundColor(
                                        getResources().getColor(R.color.actor_button_male_not_selected));
                            } else {
                                button.setBackgroundColor(
                                        getResources().getColor(R.color.actor_button_female_not_selected));
                            }
                        }

                        gridView.invalidateViews();
                    }
                });

                // Set button colour
                if (actee.sex == 1) {
                    button.setBackgroundColor(
                            getResources().getColor(R.color.actor_button_male_not_selected));
                } else {
                    button.setBackgroundColor(
                            getResources().getColor(R.color.actor_button_female_not_selected));
                }

                list.add(button);
            }

            final ButtonAdapter adapter =
                    new ButtonAdapter(
                            this, android.R.layout.simple_dropdown_item_1line, list);

            gridView.setNumColumns(4);
            gridView.setLayoutParams(
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            gridView.setVerticalSpacing(5);
            gridView.setHorizontalSpacing(5);
            gridView.setGravity(Gravity.TOP);
            gridView.setAdapter(adapter);
            relativeLayout.addView(gridView);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                    ToggleButton button = (ToggleButton) adapter.getItem(arg2);
                    button.callOnClick();
                    gridView.invalidateViews();
                }
            });

            // Setup filter field
            final EditText filter = (EditText) findViewById(R.id.acteeFilterText);
            filter.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable arg0) {
                    hideButtons(filter.getText().toString(), GlobalVariables.actors, list);
                    gridView.invalidateViews();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
            });
        }
    }

    private void hideButtons(String filter, ArrayList<Actor> actors, ArrayList<ToggleButton> buttons) {

        for (int i = 0; i < actors.size(); i++) {

            Actor actor = actors.get(i);
            ToggleButton button = buttons.get(i);

            if (!actor.abb.toLowerCase().contains(filter.toLowerCase())
                    && !actor.name.toLowerCase().contains(filter.toLowerCase())) {

                button.setVisibility(View.GONE);
            } else {

                button.setVisibility(View.VISIBLE);
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
