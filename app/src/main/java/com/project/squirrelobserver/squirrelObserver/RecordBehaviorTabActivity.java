package com.project.squirrelobserver.squirrelObserver;

import android.app.TabActivity;
import android.os.Bundle;

import com.project.squirrelobserver.R;

/**
 * Created by sean on 2/9/16.
 */
public  class RecordBehaviorTabActivity
        extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_behaviors_tab_activity);
    }

}
