package com.project.squirrelobserver.squirrelObserver;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.util.Actor;
import com.project.squirrelobserver.util.Behavior;
import com.project.squirrelobserver.util.GlobalVariables;
import com.project.squirrelobserver.util.Utils;

import org.w3c.dom.Text;

public  class RecordActivity
        extends TabActivity {

    private TabHost tabHost = null;
    private View tabView1 = null;
    private TextView tabTextView1 = null;
    private View tabView2 = null;
    private TextView tabTextView2 = null;
    private View tabView3 = null;
    private TextView tabTextView3 = null;
    private View tabView4 = null;
    private TextView tabTextView4 = null;
    private View tabView5 = null;
    private TextView tabTextView5 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Bundle params = getIntent().getExtras();

        if(!params.equals(null)) {
            //Setup timer
            TextView timer = (TextView) findViewById(R.id.textClock);
            if (params.getBoolean("startTimer", false)) {
                long startTimeInMillis = params.getLong("scanInterval", 0);
                int seconds = (int) (startTimeInMillis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                timer.setText(String.format("%d:%02d", minutes, seconds));
            }
        }

        // Set text in header label
        TextView header = (TextView) findViewById(R.id.scanAOHeaderLabel);

        // Check for Scan or A-O
        if (startTimer) {
            header.setText(getResources().getString(R.string.record_activity_scan_label));
        } else {
            header.setText(getResources().getString(R.string.record_activity_all_occurrences_label));
        }

        // Setup tabs
        tabHost = (TabHost)findViewById(android.R.id.tabhost);

        Intent intent1 = new Intent().setClass(this, RecordActorTabActivity.class);
        Intent intent2 = new Intent().setClass(this, RecordBehaviorTabActivity.class);
        Intent intent3 = new Intent().setClass(this, RecordActeeTabActivity.class);
        Intent intent4 = new Intent().setClass(this, RecordModifierTabActivity.class);
        Intent intent5 = new Intent().setClass(this, RecordOtherTabActivity.class);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("RecordActorTab")
                .setIndicator(getResources().getString(R.string.actor_tab_indicator))
                .setContent(intent1);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("RecordBehaviorTab")
                .setIndicator(getResources().getString(R.string.behaviors_tab_indicator))
                .setContent(intent2);
        TabHost.TabSpec tab3 = tabHost.newTabSpec("RecordActeeTab")
                .setIndicator(getResources().getString(R.string.actee_tab_indicator))
                .setContent(intent3);
        TabHost.TabSpec tab4 = tabHost.newTabSpec("RecordModifierTab")
                .setIndicator(getResources().getString(R.string.modifier_tab_indicator))
                .setContent(intent4);
        TabHost.TabSpec tab5 = tabHost.newTabSpec("RecordOtherTab")
                .setIndicator(getResources().getString(R.string.other_tab_indicator))
                .setContent(intent5);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
        tabHost.addTab(tab4);
        tabHost.addTab(tab5);

        // Set tab text size
        tabView1 = (View) tabHost.getTabWidget().getChildAt(0);
        tabTextView1 = (TextView) tabView1.findViewById(android.R.id.title);
        tabTextView1.setTextSize(9);
        tabTextView1.setPadding(0, 0, 0, 0);

        tabView2 = (View) tabHost.getTabWidget().getChildAt(1);
        tabTextView2 = (TextView) tabView2.findViewById(android.R.id.title);
        tabTextView2.setTextSize(9);
        tabTextView2.setPadding(0, 0, 0, 0);

        tabView3 = (View) tabHost.getTabWidget().getChildAt(2);
        tabTextView3 = (TextView) tabView3.findViewById(android.R.id.title);
        tabTextView3.setTextSize(9);
        tabTextView3.setPadding(0, 0, 0, 0);
        tabTextView3.setEnabled(false);
        tabView3.setEnabled(false);
        tabView3.setClickable(false);

        tabView4 = (View) tabHost.getTabWidget().getChildAt(3);
        tabTextView4 = (TextView) tabView4.findViewById(android.R.id.title);
        tabTextView4.setTextSize(9);
        tabTextView4.setPadding(0, 0, 0, 0);
        tabTextView4.setEnabled(false);
        tabView4.setEnabled(false);
        tabView4.setClickable(false);

        tabView5 = (View) tabHost.getTabWidget().getChildAt(4);
        tabTextView5 = (TextView) tabView5.findViewById(android.R.id.title);
        tabTextView5.setTextSize(9);
        tabTextView5.setPadding(0, 0, 0, 0);
        tabTextView5.setEnabled(false);
        tabView5.setEnabled(false);
        tabView5.setClickable(false);

        tabHost.setCurrentTab(0);
    }

    public void switchTabView(int position) {

        if (tabHost != null) {
            tabHost.setCurrentTab(position);
        }
    }

    public void updateTabEnabledState() {

        boolean enableActeeTab = false;
        boolean enableModifierTab = false;
        boolean enableOtherTab = false;

        // Check for actee and modifiers tab enabled state
        for (int i = 0; i < GlobalVariables.currentRecord.behaviorsSize; i++) {

            Behavior b = (Behavior) GlobalVariables.currentRecord.getBehavior(i);
            if (b != null && b.requiresActee)
                enableActeeTab = true;

            if (b != null && b.modifiers != null && !b.modifiers.isEmpty())
                enableModifierTab = true;
        }

        // Check for other tab enabled state
        // Activate if all required items are selected
        Actor actor = (Actor) GlobalVariables.currentRecord.actor;
        Actor actee = (Actor) GlobalVariables.currentRecord.actee;
        if (actor != null
                && GlobalVariables.currentRecord.behaviorsSize > 0
                && (enableActeeTab
                    && actee != null
                    || !enableActeeTab))
            enableOtherTab = true;

        // Update tab states
        if (tabView3 != null && tabTextView3 != null) {
            tabTextView3.setEnabled(enableActeeTab);
            tabView3.setEnabled(enableActeeTab);
            tabView3.setClickable(enableActeeTab);
        }

        if (tabView4 != null && tabTextView4 != null) {
            tabTextView4.setEnabled(enableModifierTab);
            tabView4.setEnabled(enableModifierTab);
            tabView4.setClickable(enableModifierTab);
        }

        if (tabView5 != null && tabTextView5 != null) {
            tabTextView5.setEnabled(enableOtherTab);
            tabView5.setEnabled(enableOtherTab);
            tabView5.setClickable(enableOtherTab);
        }
    }

    public void onEndButtonClicked(View view) {

        // Close current activity after verify dialogue
        Utils.endRecordingSessionVerifyMessage(view.getContext(), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_friends_library, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onLibraryListFragmentInteraction(Uri uri) {

        // Do something
//    }
}
