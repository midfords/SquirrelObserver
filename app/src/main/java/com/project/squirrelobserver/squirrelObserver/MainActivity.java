package com.project.squirrelobserver.squirrelObserver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.squirrelobserver.util.Behavior;
import com.project.squirrelobserver.util.FileParser;
import com.project.squirrelobserver.util.Record;
import com.project.squirrelobserver.fragments.ImportExportFragment;
import com.project.squirrelobserver.fragments.SetupFragment;
import com.project.squirrelobserver.fragments.NavigationDrawerFragment;
import com.project.squirrelobserver.R;
import com.project.squirrelobserver.util.GlobalVariables;
import com.project.squirrelobserver.util.Utils;

import java.io.File;
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

        // Import app settings
        String path = this.getFilesDir().getPath() + "/" + GlobalVariables.settingsFileName;
        FileParser.importAppSettings(path);

        // Read in contents of files. If there is a problem reading in the file, set the settings file
        //   to the empty string.
        if (GlobalVariables.locationCSVPath != null
                && !GlobalVariables.locationCSVPath.isEmpty()
                && !FileParser.generateListOfLocationPoints(GlobalVariables.locationCSVPath)) {
            GlobalVariables.locationCSVPath = "";
            FileParser.removeValueFromAppSettings(
                    path, GlobalVariables.settingsLocationTag);
        }

        if (GlobalVariables.actorsCSVPath != null
                && !GlobalVariables.actorsCSVPath.isEmpty()
                && !FileParser.generateListOfActors(GlobalVariables.actorsCSVPath)) {
            GlobalVariables.actorsCSVPath = "";
            FileParser.removeValueFromAppSettings(
                    path, GlobalVariables.settingsActorTag);
        }

        if (GlobalVariables.behaviorsCSVPath != null
                && !GlobalVariables.behaviorsCSVPath.isEmpty()
                && !FileParser.generateListOfBehaviors(GlobalVariables.behaviorsCSVPath)) {
            GlobalVariables.behaviorsCSVPath = "";
            FileParser.removeValueFromAppSettings(
                    path, GlobalVariables.settingsBehaviorTag);
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Fill list of AO behaviors
        fillAllOccurrenceBehaviorsList();
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
        intent.setType("text/csv");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent,
                            getResources().getString(R.string.file_import_dialog_title)),
                    selectCode);

        } catch (Exception ex) { }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {

    //        String documentId = DocumentsContract.getDocumentId(data.getData());

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

                            TextView locationLabel = (TextView) findViewById(R.id.locationFileName);

                            // Import File Contents
                            if (FileParser.generateListOfLocationPoints(GlobalVariables.locationCSVPath)) {

                                // Update settings with new file
                                String settingsPath =
                                        this.getFilesDir().getPath() + "/" + GlobalVariables.settingsFileName;
                                FileParser.writeValueToAppSettings(
                                        settingsPath, GlobalVariables.settingsLocationTag, GlobalVariables.locationCSVPath);

                                // Set Label
                                locationLabel.setText(
                                        GlobalVariables.locationCSVPath.substring(
                                                GlobalVariables.locationCSVPath.lastIndexOf("/") + 1));
                            } else {

                                Utils.importButtonErrorMessage(this);
                                locationLabel.setText(
                                        getResources().getString(R.string.import_export_no_file_label));
                            }
                        }

                    }
                    break;

                case GlobalVariables.ACTORS_CSV_SELECT_CODE:
                    if (resultCode == RESULT_OK && path != null) {

                        // Set the path as a variable
                        GlobalVariables.actorsCSVPath = path;

                        if (!GlobalVariables.actorsCSVPath.isEmpty()) {

                            TextView actorsLabel = (TextView) findViewById(R.id.actorsFileName);

                            // Import File Contents
                            if (FileParser.generateListOfActors(GlobalVariables.actorsCSVPath)) {

                                // Update settings with new file
                                String settingsPath =
                                        this.getFilesDir().getPath() + "/" + GlobalVariables.settingsFileName;
                                FileParser.writeValueToAppSettings(
                                        settingsPath, GlobalVariables.settingsActorTag, GlobalVariables.actorsCSVPath);

                                // Set Label
                                actorsLabel.setText(
                                        GlobalVariables.actorsCSVPath.substring(
                                                GlobalVariables.actorsCSVPath.lastIndexOf("/") + 1));
                            } else {

                                Utils.importButtonErrorMessage(this);
                                actorsLabel.setText(
                                        getResources().getString(R.string.import_export_no_file_label));
                            }
                        }
                    }
                    break;

                case GlobalVariables.BEHAVIORS_CSV_SELECT_CODE:
                    if (resultCode == RESULT_OK && path != null) {

                        // Set the path as a variable
                        GlobalVariables.behaviorsCSVPath = path;

                        if (!GlobalVariables.behaviorsCSVPath.isEmpty()) {

                            TextView behaviorsLabel = (TextView) findViewById(R.id.behaviorsFileName);

                            // Import File Contents
                            if (FileParser.generateListOfBehaviors(GlobalVariables.behaviorsCSVPath)) {

                                // Update settings with new file
                                String settingsPath =
                                        this.getFilesDir().getPath() + "/" + GlobalVariables.settingsFileName;
                                FileParser.writeValueToAppSettings(
                                        settingsPath, GlobalVariables.settingsBehaviorTag, GlobalVariables.behaviorsCSVPath);

                                // Set Label
                                behaviorsLabel.setText(
                                        GlobalVariables.behaviorsCSVPath.substring(
                                                GlobalVariables.behaviorsCSVPath.lastIndexOf("/") + 1));
                            } else {

                                Utils.importButtonErrorMessage(this);
                                behaviorsLabel.setText(
                                        getResources().getString(R.string.import_export_no_file_label));
                            }
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

    public void onScanExportButtonClicked(final View view) {

        String csvScanPath =
                view.getContext().getFilesDir().getPath() + "/" + GlobalVariables.csvScanFileName;

        if (!FileParser.exportRecordCSV(
                view.getContext(), csvScanPath)) {

            Utils.exportButtonErrorMessage(view.getContext());
        }
    }

    public void onAOExportButtonClicked(final View view) {

        String csvAOPath =
                view.getContext().getFilesDir().getPath() + "/" + GlobalVariables.csvAOFileName;

        if (!FileParser.exportRecordCSV(
                view.getContext(), csvAOPath)) {

            Utils.exportButtonErrorMessage(view.getContext());
        }
    }

    public void onScanClearButtonClicked(View view) {

        String csvScanPath =
                view.getContext().getFilesDir().getPath() + "/" + GlobalVariables.csvScanFileName;

        Utils.clearButtonWarningMessage(view.getContext(), csvScanPath);
    }

    public void onAOClearButtonClicked(View view) {

        String csvAOPath =
                view.getContext().getFilesDir().getPath() + "/" + GlobalVariables.csvAOFileName;

        Utils.clearButtonWarningMessage(view.getContext(), csvAOPath);
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
                .setCancelable(true)
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
        if (GlobalVariables.behaviors != null && GlobalVariables.behaviors.size() > 0) {

            for (int i = 0; i < GlobalVariables.behaviors.size(); i++) {

                Behavior behavior = GlobalVariables.behaviors.get(i);

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
    public void onResume() {
        super.onResume();
        if(GlobalVariables.aoBehaviors == null) {
            fillAllOccurrenceBehaviorsList();
        }
    }

    public void fillAllOccurrenceBehaviorsList() {
        if (GlobalVariables.aoBehaviors == null) {

            GlobalVariables.aoBehaviors = new ArrayList<Behavior>();
        }

        if (GlobalVariables.behaviors != null && GlobalVariables.behaviors.size() > 0) {
            for (int i = 0; i < GlobalVariables.behaviors.size(); i++) {

                Behavior behavior = GlobalVariables.behaviors.get(i);

                // Automatically check behaviours flagged as AO
                if (behavior.isAO
                        && GlobalVariables.aoBehaviors != null
                        && !GlobalVariables.aoBehaviors.contains(behavior)) {
                    GlobalVariables.aoBehaviors.add(behavior);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_all_occurrences:

                final ActionBarActivity activity = this;

                // Prompt user for observer ID
                View aoObserverPromptView =
                        View.inflate(this, R.layout.dialog_ao_observer_id_prompt, null);
                final EditText observerIDEditText =
                        (EditText) aoObserverPromptView.findViewById(R.id.observerIDEditText);

                // Generate dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.ao_observer_id_picker_dialog_title));
                builder.setView(aoObserverPromptView)
                        .setCancelable(true)
                        .setPositiveButton(
                                getResources().getString(R.string.ao_observer_id_picker_dialog_confirm),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String observerId = observerIDEditText.getText().toString();
                                        Toast toast;
                                        if (observerId.isEmpty()) {
                                            observerIDEditText.requestFocus();
                                            toast = Toast.makeText(getApplicationContext(), "Observer ID is mandatory", Toast.LENGTH_SHORT);
                                            toast.show();
                                        } else {
                                            boolean isIDFieldReady = true;
                                            for (int i = 0; i < observerId.length(); i++) {
                                                if (!Character.isLetterOrDigit(observerId.charAt(i))) {
                                                    isIDFieldReady = false;
                                                }
                                            }
                                            if (isIDFieldReady) {
                                                // Start Record Activity
                                                Intent intent = new Intent(activity, RecordActivity.class);
                                                Record record = new Record(observerIDEditText.getText().toString(), false);

                                                long intervalInMillis = 0;
                                                Bundle params = new Bundle();
                                                params.putBoolean("startTimer", false);
                                                params.putLong("scanInterval", intervalInMillis);
                                                intent.putExtras(params);

                                                GlobalVariables.currentRecord = record;

                                                startActivity(intent);
                                            } else {
                                                toast = Toast.makeText(getApplicationContext(), "Illegal Character in Observer ID", Toast.LENGTH_SHORT);
                                                toast.show();
                                            }
                                        }
                                    }
                                })
                        .setNegativeButton(
                                getResources().getString(R.string.ao_observer_id_picker_dialog_cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        // Do nothing
                                    }
                                })
                        .show();

                return true;

            case android.R.id.home:
                return mNavigationDrawerFragment.toggleDrawer();

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
