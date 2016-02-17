package com.project.squirrelobserver.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by sean on 2/12/16.
 */
public class FileParser {

    public static boolean generateListOfLocationPoints (String csvFileLocation) {

        if (csvFileLocation == null)
            return false;
        String extension = csvFileLocation.substring(
                csvFileLocation.lastIndexOf(".") + 1, csvFileLocation.length());
        if (!extension.equalsIgnoreCase("csv"))
            return false;

        GlobalVariables.locationPointsX = new ArrayList<LocationPoint>();    // Initialize our list of points
        GlobalVariables.locationPointsY = new ArrayList<LocationPoint>();    // Initialize our list of points
        InputStream inputStream = null;
        String label, x, y;

        try {
            File file = new File(csvFileLocation);
            inputStream = new BufferedInputStream(new FileInputStream(file));

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            // Skip first line, it contains the column headers
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] dataRow = line.split(",");

                if (dataRow.length == 3) { // Don't use row if there are incomplete fields

                    label = dataRow[0];
                    x = dataRow[1];
                    y = dataRow[2];

                    // Parse x and y integers
                    int x_parsed = 0;
                    int y_parsed = 0;

                    // Attempt to parse the int
                    try { x_parsed = Integer.parseInt(x); }
                    catch (Exception e) { }
                    try { y_parsed = Integer.parseInt(y); }
                    catch (Exception e) { }

                    LocationPoint locationPoint = new LocationPoint(label, x_parsed, y_parsed);

                    if (x_parsed == 0) {

                        GlobalVariables.locationPointsY.add(locationPoint);

                    } else if (y_parsed == 0) {

                        GlobalVariables.locationPointsX.add(locationPoint);
                    }
                }
            }

            return true;

        } catch (IOException e) {

            GlobalVariables.locationPointsX = null;
            GlobalVariables.locationPointsY = null;
            return false;

        } catch (Exception e) {

            GlobalVariables.locationPointsX = null;
            GlobalVariables.locationPointsY = null;
            return false;

        } finally {

            try {

                inputStream.close();

            } catch (IOException e) { }
        }
    }

    public static boolean generateListOfBehaviors (String csvFileBehaviors) {

        if (csvFileBehaviors == null)
            return false;
        String extension = csvFileBehaviors.substring(
                csvFileBehaviors.lastIndexOf(".") + 1, csvFileBehaviors.length());
        if (!extension.equalsIgnoreCase("csv"))
            return false;

        GlobalVariables.behaviors = new ArrayList<Behavior>();    // Initialize our list of behaviors
        InputStream inputStream = null;
        String code, desc, modif;   // Code, Description and Modifier

        try {
            File file = new File(csvFileBehaviors);
            inputStream = new BufferedInputStream(new FileInputStream(file));

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            // Skip first line, it contains the column headers
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] dataRow = line.split(",");

                if (dataRow.length >= 2) { // Don't use row if there are incomplete fields

                    code = dataRow[0];
                    desc = dataRow[1];

                    // Generate list of modifiers
                    ArrayList<String> modifiers = new ArrayList<String>();
                    for (int i = 2; i < dataRow.length; i++) {

                        modif = dataRow[i];
                        modifiers.add(modif);
                    }

                    try {
                        // Parse code integer
                        int code_parsed = Integer.parseInt(code);

                        Behavior behavior = new Behavior(code_parsed, desc, modifiers);
                        GlobalVariables.behaviors.add(behavior);

                    } catch (Exception e) { }
                }
            }

            return true;

        } catch (IOException e) {

            GlobalVariables.behaviors = null;
            return false;

        } catch (Exception e) {

            GlobalVariables.behaviors = null;
            return false;

        } finally {

            try {

                inputStream.close();

            } catch (IOException e) { }
        }
    }

    public static boolean generateListOfActors (String csvFileActors) {

        if (csvFileActors == null)
            return false;
        String extension = csvFileActors.substring(
                csvFileActors.lastIndexOf(".") + 1, csvFileActors.length());
        if (!extension.equalsIgnoreCase("csv"))
            return false;

        GlobalVariables.actors = new ArrayList<Actor>();    // Initialize our list of actors
        InputStream inputStream = null;
        String name, abb, tag, col, sex, age; // Name, Abbreviation, Tag no, Colony, Sex, Age

        try {
            File file = new File(csvFileActors);
            inputStream = new BufferedInputStream(new FileInputStream(file));

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            // Skip first line, it contains the column headers
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] dataRow = line.split(",");

                if (dataRow.length == 6) { // TODO verify there will not be missing fields in data

                    name = dataRow[0];
                    abb = dataRow[1];
                    tag = dataRow[2];
                    col = dataRow[3];
                    sex = dataRow[4];
                    age = dataRow[5];

                    // Parse integers
                    int tag_parsed = -1;
                    int sex_parsed = -1;
                    int age_parsed = -1;

                    try { tag_parsed = Integer.parseInt(tag); }
                    catch (Exception e) { }
                    try { sex_parsed = Integer.parseInt(sex); }
                    catch (Exception e) { }
                    try { age_parsed = Integer.parseInt(age); }
                    catch (Exception e) { }

                    Actor actor = new Actor(name, abb, tag_parsed, col, sex_parsed, age_parsed);
                    GlobalVariables.actors.add(actor);
                }
            }

            return true;

        } catch (IOException e) {

            GlobalVariables.actors = null;
            return false;

        } catch (Exception e) {

            GlobalVariables.actors = null;
            return false;

        } finally {

            try {

                inputStream.close();

            } catch (IOException e) { }
        }
    }

    public static void writeLineToScanRecords(Record record) {

    }

    public static void writeLineToAORecords(Record record) {

    }
}
