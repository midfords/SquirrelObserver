package com.project.squirrelobserver.data;

import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by sean on 2/12/16.
 */
public class Behavior {

    public int code;    // Behavior numerical code
    public String desc; // Description of behavior
    public ArrayList<String> modifiers; // List of all modifiers
    public ToggleButton button; // Keep the corresponding button here

    public Behavior(int code, String desc, ArrayList<String> modifiers) {
        this.code = code;
        this.desc = desc;
        this.modifiers = modifiers;
        this.button = null;
    }
}
