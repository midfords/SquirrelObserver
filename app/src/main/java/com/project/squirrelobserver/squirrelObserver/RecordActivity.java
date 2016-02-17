package com.project.squirrelobserver.squirrelObserver;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TabHost;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.data.Record;
import com.project.squirrelobserver.util.Utils;

import java.util.zip.Inflater;

public  class RecordActivity
        extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // Get Record from Intent
//        Intent intent = getIntent();
//        Record record = (Record) intent.getSerializableExtra("Record");

        // Setup tabs
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        Intent intent1 = new Intent().setClass(this, RecordActorTabActivity.class);
//        intent1.putExtra("Record", record);
        Intent intent2 = new Intent().setClass(this, RecordBehaviorTabActivity.class);
//        intent2.putExtra("Record", record);
        Intent intent3 = new Intent().setClass(this, RecordActeeTabActivity.class);
//        intent3.putExtra("Record", record);
        Intent intent4 = new Intent().setClass(this, RecordModifierTabActivity.class);
//        intent4.putExtra("Record", record);
        Intent intent5 = new Intent().setClass(this, RecordOtherTabActivity.class);
//        intent5.putExtra("Record", record);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("RecordActorTab").setIndicator("Actor").setContent(intent1);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("RecordBehaviorTab").setIndicator("Behaviors").setContent(intent2);
        TabHost.TabSpec tab3 = tabHost.newTabSpec("RecordActeeTab").setIndicator("Actee").setContent(intent3);
        TabHost.TabSpec tab4 = tabHost.newTabSpec("RecordModifierTab").setIndicator("Modifier").setContent(intent4);
        TabHost.TabSpec tab5 = tabHost.newTabSpec("RecordOtherTab").setIndicator("Other").setContent(intent5);

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
