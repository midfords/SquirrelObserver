package com.project.squirrelobserver.squirrelObserver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.util.GlobalVariables;
import com.project.squirrelobserver.util.LocationPoint;
import com.project.squirrelobserver.util.Utils;

import java.util.ArrayList;

/**
 * Created by sean on 2/9/16.
 */
public  class RecordOtherTabActivity
        extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_other_tab);

        Spinner xSpinner = (Spinner) findViewById(R.id.location_x_spinner);

        ArrayAdapter<LocationPoint> x_list = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, GlobalVariables.locationPointsX);
        x_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        xSpinner.setAdapter(x_list);
        xSpinner.setSelection(0);

        Spinner ySpinner = (Spinner) findViewById(R.id.location_y_spinner);

        ArrayAdapter<LocationPoint> y_list = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, GlobalVariables.locationPointsY);
        y_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ySpinner.setAdapter(y_list);
        ySpinner.setSelection(0);

        Button recordButton = (Button) findViewById(R.id.record_button);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText groupSize = (EditText) findViewById(R.id.group_size);
                EditText xMod = (EditText) findViewById(R.id.x_mod_distance);
                EditText yMod = (EditText) findViewById(R.id.y_mod_distance);

                GlobalVariables.currentRecord.groupSize = Integer.parseInt(groupSize.getText().toString());
                Spinner xSpinner = (Spinner) findViewById(R.id.location_x_spinner);
                Spinner ySpinner = (Spinner) findViewById(R.id.location_y_spinner);

                double x, y;

                x = ((LocationPoint) xSpinner.getSelectedItem()).x + (Double.parseDouble(xMod.getText().toString()));
                y = ((LocationPoint) ySpinner.getSelectedItem()).y + (Double.parseDouble(yMod.getText().toString()));

                GlobalVariables.currentRecord.x = x;
                GlobalVariables.currentRecord.y = y;


            }
        });
    }

    // This is a listener for the physical back button, so we can display the verification dialog here too
    @Override
    public void onBackPressed() {

        Context context = RecordOtherTabActivity.this;

        // Close current activity after verify dialogue
        Utils.endRecordingSessionVerifyMessage(context, this);
    }


    public void groupSizeButtonClick(View v) {
        EditText groupSizeText = (EditText) findViewById(R.id.group_size);
        Button btn = (Button) v;
        groupSizeText.setText(btn.getText());
    }


}
