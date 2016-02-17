package com.project.squirrelobserver.util;

import java.util.ArrayList;

/**
 * Created by sean on 2/13/16.
 */
public class GlobalVariables {

    public static ArrayList<LocationPoint> locationPointsX; // List of all points in x coordinate system
    public static ArrayList<LocationPoint> locationPointsY; // List of all points in y coordinate system
    public static ArrayList<Behavior> behaviors;   // List of all behaviors
    public static ArrayList<Actor> actors;     // List of all actors

    public static final int SCAN_INTERVAL_DEFAULT_MINUTES = 10;
    public static final int SCAN_INTERVAL_DEFAULT_SECONDS = 0;

    public static int scanIntervalMinutes = GlobalVariables.SCAN_INTERVAL_DEFAULT_MINUTES;
    public static int scanIntervalSeconds = GlobalVariables.SCAN_INTERVAL_DEFAULT_SECONDS;

    public static final int LOCATION_CSV_SELECT_CODE = 0;
    public static final int ACTORS_CSV_SELECT_CODE = 1;
    public static final int BEHAVIORS_CSV_SELECT_CODE = 2;

    public static String locationCSVPath = "";
    public static String actorsCSVPath = "";
    public static String behaviorsCSVPath = "";

    public static Record currentRecord = null;
    public static ArrayList<Behavior> aoBehaviors = null;
}
