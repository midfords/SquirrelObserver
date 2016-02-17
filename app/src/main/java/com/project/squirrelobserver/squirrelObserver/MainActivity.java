package com.project.squirrelobserver.squirrelObserver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.project.squirrelobserver.data.Behavior;
import com.project.squirrelobserver.data.DataAccessor;
import com.project.squirrelobserver.data.Record;
import com.project.squirrelobserver.fragments.ImportExportFragment;
import com.project.squirrelobserver.fragments.SetupFragment;
import com.project.squirrelobserver.fragments.NavigationDrawerFragment;
import com.project.squirrelobserver.R;
import com.project.squirrelobserver.util.GlobalVariables;
import com.project.squirrelobserver.util.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        SetupFragment.SetupFragmentCallbacks,
        ImportExportFragment.ImportExportFragmentCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch(position) {
            case 0: // Setup

                SetupFragment setupFragment = SetupFragment.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, setupFragment)
                        .commit();
                break;
            case 1: // Import Export

                ImportExportFragment importExportFragment = ImportExportFragment.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, importExportFragment)
                        .commit();
                break;
        }
    }

    private void showFileChooser(int selectCode) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent,
                            getResources().getString(R.string.file_import_dialog_title)),
                    selectCode);

        } catch (android.content.ActivityNotFoundException ex) { }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {

            // Get the Uri of the selected file
            Uri uri = data.getData();

            // Get the path
            String path = Utils.getPath(this, uri);

            switch (requestCode) {
                case GlobalVariables.LOCATION_CSV_SELECT_CODE:
                    if (resultCode == RESULT_OK && path != null) {

                        // Set the path as a variable
                        GlobalVariables.locationCSVPath = path;

                        if (!GlobalVariables.locationCSVPath.isEmpty()) {

                            // Import File Contents
                            DataAccessor.generateListOfLocationPoints(GlobalVariables.locationCSVPath);

                            // Set Label
                            TextView locationLabel = (TextView) findViewById(R.id.locationFileName);
                            locationLabel.setText(
                                    GlobalVariables.locationCSVPath.substring(
                                            GlobalVariables.locationCSVPath.lastIndexOf("/")+1));
                        }

                    }
                    break;

                case GlobalVariables.ACTORS_CSV_SELECT_CODE:
                    if (resultCode == RESULT_OK && path != null) {

                        // Set the path as a variable
                        GlobalVariables.actorsCSVPath = path;

                        if (!GlobalVariables.actorsCSVPath.isEmpty()) {

                            // Import File Contents
                            DataAccessor.generateListOfActors(GlobalVariables.actorsCSVPath);

                            // Set Label
                            TextView actorsLabel = (TextView) findViewById(R.id.actorsFileName);
                            actorsLabel.setText(
                                    GlobalVariables.actorsCSVPath.substring(
                                            GlobalVariables.actorsCSVPath.lastIndexOf("/")+1));
                        }
                    }
                    break;

                case GlobalVariables.BEHAVIORS_CSV_SELECT_CODE:
                    if (resultCode == RESULT_OK && path != null) {

                        // Set the path as a variable
                        GlobalVariables.behaviorsCSVPath = path;

                        if (!GlobalVariables.behaviorsCSVPath.isEmpty()) {

                            // Import File Contents
                            DataAccessor.generateListOfBehaviors(GlobalVariables.behaviorsCSVPath);

                            // Set Label
                            TextView behaviorsLabel = (TextView) findViewById(R.id.behaviorsFileName);
                            behaviorsLabel.setText(
                                    GlobalVariables.behaviorsCSVPath.substring(
                                            GlobalVariables.behaviorsCSVPath.lastIndexOf("/")+1));
                        }
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onLocationImportButtonClicked (final View view) {

        showFileChooser(GlobalVariables.LOCATION_CSV_SELECT_CODE);
    }

    public void onActorsImportButtonClicked (final View view) {

        showFileChooser(GlobalVariables.ACTORS_CSV_SELECT_CODE);
    }

    public void onBehaviorsImportButtonClicked (final View view) {

        showFileChooser(GlobalVariables.BEHAVIORS_CSV_SELECT_CODE);
    }

    public void onScanExportButtonClicked(View view) {

    }

    public void onAOExportButtonClicked(View view) {

    }

    public void onScanIntervalFieldClicked (final View view) {

        View intervalPickerView = View.inflate(view.getContext(), R.layout.dialog_interval_picker, null);
        final EditText minuteText = (EditText) intervalPickerView.findViewById(R.id.minuteEditText);
        final EditText secondText = (EditText) intervalPickerView.findViewById(R.id.secondEditText);

        minuteText.setText("" + GlobalVariables.scanIntervalMinutes);
        secondText.setText("" + GlobalVariables.scanIntervalSeconds);

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(getResources().getString(R.string.interval_picker_dialog_title));
        builder.setView(intervalPickerView)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.interval_picker_dialog_confirm),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                int minute = GlobalVariables.scanIntervalMinutes;
                                int second = GlobalVariables.scanIntervalSeconds;

                                if (!minuteText.getText().toString().isEmpty()
                                        && !secondText.getText().toString().isEmpty()) {

                                    minute = Integer.parseInt(minuteText.getText().toString());
                                    second = Integer.parseInt(secondText.getText().toString());

                                    if (minute >= 60)
                                        minute = 59;
                                    if (second >= 60)
                                        second = 59;
                                }

                                // Put values in text field
                                EditText scanIntervalEditText =
                                        (EditText) findViewById(R.id.scanIntervalInput);

                                GlobalVariables.scanIntervalMinutes = minute;
                                GlobalVariables.scanIntervalSeconds = second;

                                String secondsString = (second < 10 ? "0" : "") + second;
                                String minutesString = (minute < 10 ? "0" : "") + minute;
                                String timeString = minutesString + ":" + secondsString;

                                scanIntervalEditText.setText(timeString);
                            }
                        })
                        .show();
    }

    public void onAOBehaviorsFieldClicked (final View view) {

        if (GlobalVariables.aoBehaviors == null) {

            GlobalVariables.aoBehaviors = new ArrayList<Behavior>();
        }

        View aoBehaviorPickerView =
                View.inflate(view.getContext(), R.layout.dialog_ao_behavior_picker, null);
        final LinearLayout linearLayout =
                (LinearLayout) aoBehaviorPickerView.findViewById(R.id.behaviorListLayout);

        // Add all behaviors to list
        if (DataAccessor.behaviors != null && DataAccessor.behaviors.size() > 0) {

            for (int i = 0; i < DataAccessor.behaviors.size(); i++) {

                Behavior behavior = DataAccessor.behaviors.get(i);

                final CheckBox checkBox = new CheckBox(linearLayout.getContext());
                checkBox.setText(behavior.desc);

                // Recheck previously checked elements
                if (GlobalVariables.aoBehaviors != null
                        && GlobalVariables.aoBehaviors.contains(behavior)) {

                    checkBox.setChecked(true);
                }

                checkBox.setTag(behavior);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {

                            Behavior behavior = (Behavior) checkBox.getTag();
                            GlobalVariables.aoBehaviors.add(behavior);
                        } else {

                            Behavior behavior = (Behavior) checkBox.getTag();
                            GlobalVariables.aoBehaviors.remove(behavior);
                        }
                    }
                });

                linearLayout.addView(checkBox);
            }

            // Generate dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle(getResources().getString(R.string.ao_behavior_picker_dialog_title));
            builder.setView(aoBehaviorPickerView)
                    .setCancelable(true)
                    .setPositiveButton(
                            getResources().getString(R.string.ao_behavior_picker_dialog_confirm),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    // Set text in text field
                                    if (GlobalVariables.aoBehaviors != null) {

                                        String list = "";

                                        for (int i = 0; i < GlobalVariables.aoBehaviors.size(); i++) {

                                            list += GlobalVariables.aoBehaviors.get(i).desc
                                                    + ((i == GlobalVariables.aoBehaviors.size() - 1)
                                                    ? "" : ", ");
                                        }

                                        EditText aoBehaviorsEditText =
                                                (EditText) findViewById(R.id.aoBehaviorsInput);

                                        aoBehaviorsEditText.setText(list);
                                    }
                                }
                            })
                    .show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_all_occurrences:
                // Start Record Activity
                Intent intent = new Intent(this, RecordActivity.class);
                Record record = new Record("", false);

                GlobalVariables.currentRecord = record;
//                intent.putExtra("Record", record);

                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section_setup);
                break;
            case 2:
                mTitle = getString(R.string.title_section_importexport);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onSetupFragmentInteraction(Uri uri) {

        // Do something
    }

    @Override
    public void onImportExportFragmentInteraction(Uri uri) {

        // Do something
    }

}
