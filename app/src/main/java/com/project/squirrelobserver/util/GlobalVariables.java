package com.project.squirrelobserver.util;

import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TooManyListenersException;

/**
 * Created by sean on 2/13/16.
 */
public class GlobalVariables {

    public static boolean activateFrequentRecentButtonLists = true;

    public static ArrayList<LocationPoint> locationPointsX; // List of all points in x coordinate system
    public static ArrayList<LocationPoint> locationPointsY; // List of all points in y coordinate system
    public static ArrayList<Behavior> behaviors;   // List of all behaviors
    public static ArrayList<Actor> actors;     // List of all actors

    public static int originX = 0;
    public static int originY = 0;

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

    public static String csvAOFileName = "Project_Export_AO_Data.csv";
    public static String csvScanFileName = "Project_Export_Scan_Data.csv";

    public static String exportDownloadPath = "/sdcard/Download/SquirrelObserver/";
    public static String settingsFileName = "App_Settings.txt";

    public static String settingsDelimiter = "=>";
    public static String settingsLocationTag = "Location_CSV_Path";
    public static String settingsActorTag = "Actor_CSV_Path";
    public static String settingsBehaviorTag = "Behavior_CSV_Path";

    public static String csvScanDataHeader =
            "Tag," +
            "Sex," +
            "Age," +
            "Col#," +
            "Date," +
            "x," +
            "y," +
            "Behavior 1," +
            "Behavior 2," +
            "Behavior 3," +
            "Modifier," +
            "Sex of Interactor," +
            "Age of Interactor," +
            "Tag of Interactor," +
            "Relationship," +
            "Time," +
            "# Squirrels in Group," +
            "ID," +
            "Scan Interval," +
            "Comments";

    public static Record currentRecord = null;
    public static ArrayList<Behavior> aoBehaviors = null;

    public static int scanInterval = 0;
}
