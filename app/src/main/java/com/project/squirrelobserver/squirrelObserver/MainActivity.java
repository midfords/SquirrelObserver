package com.project.squirrelobserver.squirrelObserver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Spinner;

import com.project.squirrelobserver.fragments.ImportExportFragment;
import com.project.squirrelobserver.fragments.SetupFragment;
import com.project.squirrelobserver.fragments.NavigationDrawerFragment;
import com.project.squirrelobserver.R;
import com.project.squirrelobserver.util.GlobalVariables;

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


    public void onScanIntervalFieldClicked (final View view) {

        View intervalPickerView = View.inflate(view.getContext(), R.layout.dialog_interval_picker, null);
        final EditText minuteText = (EditText) intervalPickerView.findViewById(R.id.minuteEditText);
        final EditText secondText = (EditText) intervalPickerView.findViewById(R.id.secondEditText);

        minuteText.setText("" + GlobalVariables.scanIntervalMinutes);
        secondText.setText("" + GlobalVariables.scanIntervalSeconds);

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Select Interval");
        builder.setView(intervalPickerView)
                .setCancelable(false)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
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
                        EditText scanIntervalEditText = (EditText) findViewById(R.id.scanIntervalInput);

                        GlobalVariables.scanIntervalMinutes = minute;
                        GlobalVariables.scanIntervalSeconds = second;

                        scanIntervalEditText.setText(
                                (minute < 10 ? "0" : "") + minute + ":"
                                        + (second < 10 ? "0" : "") + second);
                    }
                })
                .show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_all_occurrences:
                // Start Record Activity
                Intent intent = new Intent(this, RecordActivity.class);
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
