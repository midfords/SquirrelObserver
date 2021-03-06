package com.project.squirrelobserver.squirrelObserver;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Switch;
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
public  class RecordBehaviorTabActivity
        extends Activity {

    private final ArrayList<ToggleButton> list = new ArrayList<ToggleButton>();
    private final ArrayList<ToggleButton> listFrequent = new ArrayList<ToggleButton>();
    private GridView gridView = null;
    private GridView gridViewFrequent = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_behaviors_tab);

        // Set reference to self in parent activity
        final RecordActivity recordActivity = (RecordActivity) this.getParent();
        recordActivity.behaviorTabActivity = RecordBehaviorTabActivity.this;

        // Create buttons for every behavior and place on activity
        if (GlobalVariables.behaviors != null && !GlobalVariables.behaviors.isEmpty()) {

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.behaviorsTabLinearLayout);

            gridView = new GridView(RecordBehaviorTabActivity.this);
            gridViewFrequent = new GridView(RecordBehaviorTabActivity.this);

            // Loop to run through all buttons
            for (int i = 0; i < GlobalVariables.behaviors.size(); i++) {

                final Behavior behavior = GlobalVariables.behaviors.get(i);
                final ToggleButton button = new ToggleButton(linearLayout.getContext());

                button.setText(behavior.desc);
                button.setTextOn(behavior.desc);
                button.setTextOff(behavior.desc);
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

                // Keep pointer to button in behavior
                behavior.button = button;

                // Attach the behavior to the button
                button.setTag(behavior);

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        RecordActivity parentActivity = (RecordActivity) getParent();

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

                            // Check if we still need an actee
                            if (GlobalVariables.currentRecord != null
                                    && !GlobalVariables.currentRecord.requiresActee()) {

                                if (recordActivity != null
                                        && recordActivity.acteeTabActivity != null
                                        && GlobalVariables.currentRecord != null
                                        && GlobalVariables.currentRecord.actee != null
                                        && GlobalVariables.currentRecord.actee.acteeButton != null) {

                                    parentActivity.acteeTabActivity.uncheckButton(
                                            GlobalVariables.currentRecord.actee.acteeButton);
                                    GlobalVariables.currentRecord.actee = null;
                                }
                            }
                        }

                        // Update tab enabled states
                        if (parentActivity != null)
                            parentActivity.updateTabEnabledState();

                        gridViewFrequent.invalidateViews();
                        gridView.invalidateViews();
                    }
                });
                if (!behavior.isAO
                        && !recordActivity.scanMode) {
                    button.setVisibility(View.GONE);
                }
                // Add the first four buttons to the frequentLayout
                if (i < 4) {
                    listFrequent.add(button);
                } else {
                    list.add(button);
                }
            }

            final ButtonAdapter adapter =
                    new ButtonAdapter(
                            this, android.R.layout.simple_dropdown_item_1line, list);

            final ButtonAdapter adapterFrequent =
                    new ButtonAdapter(
                            this, android.R.layout.simple_dropdown_item_1line, listFrequent);

            gridView.setId(R.id.behavior_grid_id);
            gridView.setNumColumns(4);
            gridView.setLayoutParams(
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            gridView.setGravity(1);
            gridView.setVerticalSpacing(5);
            gridView.setHorizontalSpacing(5);
            gridView.setAdapter(adapter);

            gridView.setId(R.id.behavior_grid_recent_id);
            gridViewFrequent.setNumColumns(4);
            gridViewFrequent.setGravity(Gravity.TOP);
            gridViewFrequent.setVerticalSpacing(5);
            gridViewFrequent.setHorizontalSpacing(5);
            gridViewFrequent.setPadding(5, 5, 5, 5);
            gridViewFrequent.setBackgroundColor(
                    getResources().getColor(R.color.record_activity_recent_layout));
            gridViewFrequent.setLayoutParams(
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            gridViewFrequent.setAdapter(adapterFrequent);

            linearLayout.addView(gridViewFrequent);
            linearLayout.addView(gridView);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                    ToggleButton button = (ToggleButton) adapter.getItem(arg2);
                    button.callOnClick();
                }
            });

            // Setup filter field
            final EditText filter = (EditText) findViewById(R.id.behaviorsFilterText);
            filter.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable arg0) {
                    hideButtons(filter.getText().toString(), list);
                    gridView.invalidateViews();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (gridView != null && gridViewFrequent != null) {
            gridView.invalidateViews();
            gridViewFrequent.invalidateViews();
        }
    }

    public void updateFrequentButtons(Behavior behavior) {

        if (!GlobalVariables.activateFrequentRecentButtonLists)
            return;

        if (behavior == null || behavior.button == null)
            return;

        ToggleButton button = behavior.button;

        // Update most recent and most frequent lists
        behavior.numTimeUsed++;

        // Replace item if numTimeUsed is higher
        if (list != null
                && listFrequent != null
                && list.contains(button)) {

            for (int i = 0; i < 4; i++) {

                ToggleButton frequentButtonToCheck = listFrequent.get(i);
                Behavior frequentBehaviorToCheck = (Behavior) frequentButtonToCheck.getTag();

                // Check if current button count is higher
                if (frequentBehaviorToCheck != null
                        && frequentBehaviorToCheck.numTimeUsed < behavior.numTimeUsed) {

                    // Place old frequent back in button list
                    ToggleButton oldFrequent = listFrequent.remove(i);

                    if (oldFrequent != null)
                        list.add(oldFrequent);

                    if (list.remove(button))
                        listFrequent.add(button);

                    break;
                }
            }
        }
    }

    public void uncheckButton(ToggleButton button) {

        if (button != null) {
            button.setChecked(false);
            button.callOnClick();
        }
    }

    private void hideButtons(String filter, ArrayList<ToggleButton> buttons) {

        if (buttons == null)
            return;

        boolean isScanMode = ((Switch)getParent().findViewById(R.id.modeToggle)).isChecked();

        for (int i = 0; i < buttons.size(); i++) {

            ToggleButton button = buttons.get(i);
            if (button == null)
                return;

            Behavior behavior = (Behavior) button.getTag();
            if (behavior == null)
                return;

            if (!behavior.desc.toLowerCase().contains(filter.toLowerCase())) {

                button.setVisibility(View.GONE);
            } else if (isScanMode || (!isScanMode && behavior.isAO)) {
                button.setVisibility(View.VISIBLE);
            }
        }
    }

    public void toggleMode(boolean scanMode) {
        toggleHideNonAOBehaviors(scanMode, list);
        toggleHideNonAOBehaviors(scanMode, listFrequent);

        if (gridView != null && gridViewFrequent != null) {
            gridView.invalidateViews();
            gridViewFrequent.invalidateViews();
        }
    }

    private void toggleHideNonAOBehaviors(boolean scanMode, ArrayList<ToggleButton> buttons) {
        if(buttons == null)
            return;

        for(int i=0; i < buttons.size(); i++) {
            ToggleButton button = buttons.get(i);
            if (button == null)
                return;

            Behavior behavior = (Behavior) button.getTag();
            if(behavior == null)
                return;

            if (!scanMode && !behavior.isAO) {
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
