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
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.adapters.ButtonAdapter;
import com.project.squirrelobserver.util.Behavior;
import com.project.squirrelobserver.util.GlobalVariables;
import com.project.squirrelobserver.util.Utils;

import java.util.ArrayList;

/**
 * Created by sean on 2/9/16.
 */
public  class RecordModifierTabActivity
        extends Activity {

    final private ArrayList<ToggleButton> list = new ArrayList<ToggleButton>();
    LinearLayout linearLayout = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_modifiers_tab);

        // Set reference to self in parent activity
        RecordActivity recordActivity = (RecordActivity) this.getParent();
        recordActivity.modifierTabActivity = RecordModifierTabActivity.this;

        // Create buttons for every behavior and place on activity
        if (GlobalVariables.behaviors != null && !GlobalVariables.behaviors.isEmpty()) {

            linearLayout = (LinearLayout) findViewById(R.id.modifiersTabLinearLayout);
            final GridView gridView = new GridView(RecordModifierTabActivity.this);

            final ButtonAdapter adapter =
                    new ButtonAdapter(
                            this, android.R.layout.simple_dropdown_item_1line, list);

            gridView.setId(R.id.modifier_grid_id);
            gridView.setNumColumns(4);
            gridView.setLayoutParams(
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            gridView.setGravity(1);
            gridView.setVerticalSpacing(5);
            gridView.setHorizontalSpacing(5);
            gridView.setAdapter(adapter);

            linearLayout.addView(gridView);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                    ToggleButton button = (ToggleButton) adapter.getItem(arg2);
                    button.callOnClick();
                }
            });

            // Setup filter field
            final EditText filter = (EditText) findViewById(R.id.modifiersFilterText);
            filter.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable arg0) {
                    hideButtons(filter.getText().toString(), list);
                    gridView.invalidateViews();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        final GridView gridView = (GridView) findViewById(R.id.modifier_grid_id);

        // Add buttons to list
        list.clear();

        // Generate a list of modifier buttons for every behavior
        for (int i = 0; i < GlobalVariables.currentRecord.behaviorsSize; i++) {

            Behavior behavior = GlobalVariables.currentRecord.getBehavior(i);

            // Loop to run through all buttons
            for (int j = 0; j < behavior.modifiers.size(); j++) {

                final String modifier = behavior.modifiers.get(j);
                final ToggleButton button = new ToggleButton(linearLayout.getContext());

                button.setText(modifier);
                button.setTextOn(modifier);
                button.setTextOff(modifier);
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

                // Keep pointer to button in behavior
                behavior.modifierButtons.add(button);

                // Attach the behavior to the button
                button.setTag(behavior);

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        if (button.isChecked()) {

                            // Button is ON
                            GlobalVariables.currentRecord.modifiers.add(modifier);

                        } else {

                            // Button is OFF
                            GlobalVariables.currentRecord.modifiers.remove(modifier);
                        }

                        gridView.invalidateViews();
                    }
                });

                list.add(button);
            }
        }

        gridView.invalidateViews();
    }

    private void hideButtons(String filter, ArrayList<ToggleButton> buttons) {

        if (buttons == null)
            return;

        for (int i = 0; i < buttons.size(); i++) {

            ToggleButton button = buttons.get(i);
            if (button == null)
                return;

            String modifier = (String) button.getText().toString();
            if (modifier == null || modifier.isEmpty())
                return;

            if (!modifier.toLowerCase().contains(filter.toLowerCase())) {

                button.setVisibility(View.GONE);
            } else {

                button.setVisibility(View.VISIBLE);
            }
        }
    }

    // This is a listener for the physical back button, so we can display the verification dialog here too
    @Override
    public void onBackPressed() {

        Context context = RecordModifierTabActivity.this;

        // Close current activity after verify dialogue
        Utils.endRecordingSessionVerifyMessage(context, this);
    }

}
