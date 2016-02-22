package com.project.squirrelobserver.squirrelObserver;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.util.Utils;

public  class RecordActivity
        extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Bundle params = getIntent().getExtras();

        //Setup timer
        TextView timer = (TextView) findViewById(R.id.textClock);
        boolean startTimer = params.getBoolean("startTimer");
        if(startTimer) {
            long startTimeInMillis = params.getLong("scanInterval");
            int seconds = (int) (startTimeInMillis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timer.setText(String.format("%d:%02d", minutes, seconds));
        }
        // Setup tabs
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

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
        TextView tabView1 = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tabView1.setTextSize(9);
        tabView1.setPadding(0, 0, 0, 0);
        TextView tabView2 = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tabView2.setTextSize(9);
        tabView2.setPadding(0, 0, 0, 0);
        TextView tabView3 = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
        tabView3.setTextSize(9);
        tabView3.setPadding(0, 0, 0, 0);
        TextView tabView4 = (TextView) tabHost.getTabWidget().getChildAt(3).findViewById(android.R.id.title);
        tabView4.setTextSize(9);
        tabView4.setPadding(0, 0, 0, 0);
        TextView tabView5 = (TextView) tabHost.getTabWidget().getChildAt(4).findViewById(android.R.id.title);
        tabView5.setTextSize(9);
        tabView5.setPadding(0, 0, 0, 0);

        tabHost.setCurrentTab(0);
    }

    public void onEndButtonClicked(View view) {

        // Close current activity after verify dialogue
        Utils.endRecordingSessionVerifyMessage(view.getContext(), this);
    }

    public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {

            case R.id.radio_scan:
                if (checked)

                    // Scan radio was clicked

                    break;
            case R.id.radio_all_occurrences:
                if (checked)

                    // AO radio was clicked

                    break;
        }
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
