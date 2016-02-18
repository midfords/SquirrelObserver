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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.adapters.ButtonAdapter;
import com.project.squirrelobserver.util.Actor;
import com.project.squirrelobserver.util.Behavior;
import com.project.squirrelobserver.util.FileParser;
import com.project.squirrelobserver.util.GlobalVariables;
import com.project.squirrelobserver.util.Utils;

import java.util.ArrayList;

/**
 * Created by sean on 2/9/16.
 */
public  class RecordBehaviorTabActivity
        extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_behaviors_tab);

        // Create buttons for every behavior and place on activity
        if (GlobalVariables.behaviors != null && !GlobalVariables.behaviors.isEmpty()) {

            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.behaviorsTabRelativeLayout);
            final GridView gridView = new GridView(RecordBehaviorTabActivity.this);
            final ArrayList<ToggleButton> list = new ArrayList<ToggleButton>();

            // Loop to run through all buttons
            for (int i = 0; i < GlobalVariables.behaviors.size(); i++) {

                final Behavior behavior = GlobalVariables.behaviors.get(i);

                final ToggleButton button = new ToggleButton(gridView.getContext());
                button.setText(behavior.desc);
                button.setTextOn(behavior.desc);
                button.setTextOff(behavior.desc);
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 15);

                // Keep pointer to button in behavior
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

                            if (removedBehavior != null && removedBehavior.button != null) {

                                removedBehavior.button.setChecked(false);
                                removedBehavior.button.callOnClick();
                            }
                        } else {

                            // Button is OFF
                            GlobalVariables.currentRecord.removeBehavior(behavior);
                        }

                        gridView.invalidateViews();
                    }
                });

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
            final EditText filter = (EditText) findViewById(R.id.behaviorsFilterText);
            filter.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable arg0) {
                    hideButtons(filter.getText().toString(), GlobalVariables.behaviors, list);
                    gridView.invalidateViews();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
            });
        }
    }

    private void hideButtons(String filter, ArrayList<Behavior> behaviors, ArrayList<ToggleButton> buttons) {

        for (int i = 0; i < behaviors.size(); i++) {

            Behavior behavior = behaviors.get(i);
            ToggleButton button = buttons.get(i);

            if (!behavior.desc.toLowerCase().contains(filter.toLowerCase())) {

                button.setVisibility(View.GONE);
            } else {

                button.setVisibility(View.VISIBLE);
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
