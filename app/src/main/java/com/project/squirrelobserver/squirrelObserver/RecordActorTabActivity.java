package com.project.squirrelobserver.squirrelObserver;

import android.os.Bundle;
import android.app.TabActivity;

import com.project.squirrelobserver.R;

/**
 * Created by sean on 2/9/16.
 */
public  class RecordActorTabActivity
        extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_actor_tab_activity);
    }

}
