package com.project.squirrelobserver.squirrelObserver;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.adapters.ButtonAdapter;
import com.project.squirrelobserver.util.Actor;
import com.project.squirrelobserver.util.GlobalVariables;
import com.project.squirrelobserver.util.Record;
import com.project.squirrelobserver.util.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by sean on 2/9/16.
 */
public  class RecordActorTabActivity
        extends Activity {

    private ToggleButton previousButton = null;
    private final ArrayList<ToggleButton> list = new ArrayList<ToggleButton>();
    private final ArrayList<ToggleButton> listRecent = new ArrayList<ToggleButton>();
    private ArrayList<ToggleButton> disabledList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_actor_tab);

        // Set reference to self in parent activity
        RecordActivity recordActivity = (RecordActivity) this.getParent();
        recordActivity.actorTabActivity = RecordActorTabActivity.this;

        // Create buttons for every behavior and place on activity
        if (GlobalVariables.actors != null && !GlobalVariables.actors.isEmpty()) {

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.actorTabLinearLayout);

            final GridView gridView = new GridView(RecordActorTabActivity.this);
            final GridView gridViewRecent = new GridView(RecordActorTabActivity.this);

            // Loop to run through all buttons
            for (int i = 0; i < GlobalVariables.actors.size(); i++) {

                final Actor actor = GlobalVariables.actors.get(i);
                final ToggleButton button = new ToggleButton(linearLayout.getContext());

                button.setText(actor.name);
                button.setTextOn(actor.name);
                button.setTextOff(actor.name);
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

                // Attach the actor to the button
                button.setTag(actor);
                actor.actorButton = button;

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

                            GlobalVariables.currentRecord.actor = (Actor) button.getTag();

                            // Set button colour based on actor sex (selected)
                            if (actor.sex == 1) {
                                button.setBackgroundColor(
                                        getResources().getColor(R.color.actor_button_male_selected));
                            } else {
                                button.setBackgroundColor(
                                        getResources().getColor(R.color.actor_button_female_selected));
                            }
                        } else {

                            // Button is OFF
                            previousButton = null;
                            GlobalVariables.currentRecord.actor = null;

                            // Set button colour based on actor sex (not selected)
                            if (actor.sex == 1) {
                                button.setBackgroundColor(
                                        getResources().getColor(R.color.actor_button_male_not_selected));
                            } else {
                                button.setBackgroundColor(
                                        getResources().getColor(R.color.actor_button_female_not_selected));
                            }
                        }

                        // Update tab enabled states
                        RecordActivity parentActivity = (RecordActivity) getParent();
                        if (parentActivity != null)
                            parentActivity.updateTabEnabledState();

                        gridViewRecent.invalidateViews();
                        gridView.invalidateViews();
                    }
                });

                // Set button colour
                if (actor.sex == 1) {
                    button.setBackgroundColor(
                            getResources().getColor(R.color.actor_button_male_not_selected));
                } else {
                    button.setBackgroundColor(
                            getResources().getColor(R.color.actor_button_female_not_selected));
                }

                // Add the first four buttons to the recentLayout
                if (i < 4) {
                    listRecent.add(button);
                } else {
                    list.add(button);
                }
            }

            final ButtonAdapter adapter =
                    new ButtonAdapter(
                            this, android.R.layout.simple_dropdown_item_1line, list);

            final ButtonAdapter adapterRecent =
                    new ButtonAdapter(
                            this, android.R.layout.simple_dropdown_item_1line, listRecent);

            gridView.setId(R.id.actor_grid_id);
            gridView.setNumColumns(4);
            gridView.setLayoutParams(
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            gridView.setGravity(1);
            gridView.setVerticalSpacing(5);
            gridView.setHorizontalSpacing(5);
            gridView.setAdapter(adapter);

            gridView.setId(R.id.actor_grid_recent_id);
            gridViewRecent.setNumColumns(4);
            gridViewRecent.setGravity(Gravity.TOP);
            gridViewRecent.setVerticalSpacing(5);
            gridViewRecent.setHorizontalSpacing(5);
            gridViewRecent.setPadding(5, 5, 5, 5);
            gridViewRecent.setBackgroundColor(
                    getResources().getColor(R.color.record_activity_recent_layout));
            gridViewRecent.setLayoutParams(
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            gridViewRecent.setAdapter(adapterRecent);

            linearLayout.addView(gridViewRecent);
            linearLayout.addView(gridView);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                    ToggleButton button = (ToggleButton) adapter.getItem(arg2);
                    if (button != null)
                        button.callOnClick();
                }
            });

            // Setup filter field
            final EditText filter = (EditText) findViewById(R.id.actorFilterText);
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

        final GridView gridView = (GridView) findViewById(R.id.actor_grid_id);
        final GridView gridViewRecent = (GridView) findViewById(R.id.actor_grid_recent_id);

        if (gridView != null && gridViewRecent != null) {
            gridView.invalidateViews();
            gridViewRecent.invalidateViews();
        }
    }

    public void switchToAllOccurences() {
        for (int i = 0; i < listRecent.size(); i++) {
            if(!listRecent.get(i).isEnabled()) {
                disabledList.add(listRecent.get(i));
                listRecent.get(i).setEnabled(true);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if(!list.get(i).isEnabled()) {
                disabledList.add(list.get(i));
                list.get(i).setEnabled(true);
            }
        }
    }

    public void switchFromAllOccurences() {
        for (int i = 0; i < disabledList.size(); i++) {
            disabledList.get(i).setEnabled(false);
        }
        disabledList.clear();
    }

    public void reenableAllButtons() {

        for (int i = 0; i < listRecent.size(); i++) {

            listRecent.get(i).setEnabled(true);
        }

        for (int i = 0; i < list.size(); i++) {

            list.get(i).setEnabled(true);
        }

        for (int i=0; i < disabledList.size(); i++) {

            disabledList.get(i).setEnabled(true);
        }
        disabledList.clear();

        final GridView gridView = (GridView) findViewById(R.id.actor_grid_id);
        final GridView gridViewRecent = (GridView) findViewById(R.id.actor_grid_recent_id);

        if (gridView != null && gridViewRecent != null) {
            gridView.invalidateViews();
            gridViewRecent.invalidateViews();
        }
    }

    public void uncheckButton(ToggleButton button) {

        if (button != null) {

            button.setChecked(false);
            button.callOnClick();
        }
    }

    public void disableButton(ToggleButton button) {

        if (button != null) {

            button.setChecked(false);
            button.setEnabled(false);
            button.callOnClick();
        }
    }

    public void updateRecentButtons(ToggleButton button) {

        if (!GlobalVariables.activateFrequentRecentButtonLists)
            return;

        // Update most recent actor list
        if (button != null && list.contains(button)) {

            // Put old recent back in main button list
            ToggleButton oldRecent = listRecent.remove(listRecent.size()-1);
            if (oldRecent != null)
                list.add(0, oldRecent);

            // Remove used button from buttons and add to recent buttons
            if (list.remove(button))
                listRecent.add(0, button);
        }
    }

    private void hideButtons(String filter, ArrayList<ToggleButton> buttons) {

        if (buttons == null)
            return;

        for (int i = 0; i < buttons.size(); i++) {

            ToggleButton button = buttons.get(i);
            if (button == null)
                return;

            Actor actor = (Actor) button.getTag();
            if (actor == null)
                return;

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

        Context context = RecordActorTabActivity.this;

        // Close current activity after verify dialogue
        Utils.endRecordingSessionVerifyMessage(context, this);
    }
}
