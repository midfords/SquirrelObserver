package com.project.squirrelobserver.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class ButtonAdapter extends BaseAdapter {

    private ArrayList<ToggleButton> list;

    public ButtonAdapter(Context context, int textViewResourceId, ArrayList<ToggleButton> list) {

        this.list = list;
    }

    @Override
    public int getCount() {

        int count = 0;

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getVisibility() == View.VISIBLE)
                count++;
        }

        return count;
    }

    @Override
    public Object getItem(int position) {

        return findVisibleButton(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        return findVisibleButton(position);
    }

    private View findVisibleButton(int position) {

        ToggleButton button = null;
        int numVisible = 0;     // Number of visible items we've passed

        // Find and return next visible item
        // Run through all items
        for (int i = 0; i < list.size() && numVisible != position + 1; i++) {

            if (list.get(i).getVisibility() == View.VISIBLE) {

                numVisible++;
                button = (ToggleButton) list.get(i);
            }
        }

        return button;
    }
}
