package com.project.squirrelobserver.util;

import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by sean on 2/12/16.
 */
public class Behavior {

    public int code;    // Behavior numerical code
    public String desc; // Description of behavior
    public ArrayList<String> modifiers; // List of all modifiers
    public ArrayList<ToggleButton> modifierButtons = new ArrayList<ToggleButton>();
    public ToggleButton button;
    public boolean requiresActee;
    public boolean isAO;
    public int numTimeUsed;

    public Behavior(int code, boolean requiresActee, String desc, boolean isAO, ArrayList<String> modifiers) {
        this.code = code;
        this.requiresActee = requiresActee;
        this.desc = desc;
        this.isAO = isAO;
        this.modifiers = modifiers;
        button = null;
        numTimeUsed = 0;
    }
}
