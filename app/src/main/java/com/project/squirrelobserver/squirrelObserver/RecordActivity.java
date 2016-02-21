package com.project.squirrelobserver.squirrelObserver;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TabHost;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.util.Utils;

public  class RecordActivity
        extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

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
