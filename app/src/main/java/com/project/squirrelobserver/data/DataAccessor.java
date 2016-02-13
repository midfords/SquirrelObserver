package com.project.squirrelobserver.data;

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
public class DataAccessor {

    public ArrayList<LocationPoint> locationPointsX; // List of all points in x coordinate system
    public ArrayList<LocationPoint> locationPointsY; // List of all points in y coordinate system
    public ArrayList<Behavior> behaviors;   // List of all behaviors
    public ArrayList<Actor> actors;     // List of all actors

    public boolean generateListOfLocationPoints (String csvFileLocation) {

        if (csvFileLocation == null)
            return false;

        locationPointsX = new ArrayList<LocationPoint>();    // Initialize our list of points
        locationPointsY = new ArrayList<LocationPoint>();    // Initialize our list of points
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

                    // Clean Strings (remove quotes)
                    label = label.replaceAll("^\"|\"$", "");
                    x = x.replaceAll("^\"|\"$", "");
                    y = y.replaceAll("^\"|\"$", "");

                    // Parse x and y integers
                    int x_parsed = Integer.parseInt(x);
                    int y_parsed = Integer.parseInt(y);

                    LocationPoint locationPoint = new LocationPoint(label, x_parsed, y_parsed);

                    if (x_parsed == 0) { // TODO verify that points will always have one zero value and one non-zero value

                        locationPointsY.add(locationPoint);

                    } else {

                        locationPointsX.add(locationPoint);
                    }
                }
            }

            return true;

        } catch (IOException e) {

            locationPointsX = null;
            locationPointsY = null;
            return false;

        } catch (Exception e) {

            locationPointsX = null;
            locationPointsY = null;
            return false;

        } finally {

            try {

                inputStream.close();

            } catch (IOException e) { }
        }
    }

    public boolean generateListOfBehaviors (String csvFileLocation) {

        if (csvFileLocation == null)
            return false;

        behaviors = new ArrayList<Behavior>();    // Initialize our list of behaviors
        InputStream inputStream = null;
        String code, desc, modif;   // Code, Description and Modifier

        try {
            File file = new File(csvFileLocation);
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

                    // Clean Strings (remove quotes)
                    code = code.replaceAll("^\"|\"$", "");
                    desc = desc.replaceAll("^\"|\"$", "");

                    // Generate list of modifiers
                    ArrayList<String> modifiers = new ArrayList<String>();
                    for (int i = 2; i < dataRow.length; i++) {

                        modif = dataRow[i];

                        // Clean String
                        modif = modif.replaceAll("^\"|\"$", "");

                        modifiers.add(modif);
                    }

                    // Parse code integers
                    int code_parsed = Integer.parseInt(code);

                    Behavior behavior = new Behavior(code_parsed, desc, modifiers);
                }
            }

            return true;

        } catch (IOException e) {

            behaviors = null;
            return false;

        } catch (Exception e) {

            behaviors = null;
            return false;

        } finally {

            try {

                inputStream.close();

            } catch (IOException e) { }
        }
    }

    public boolean generateListOfActors (String csvFileLocation) {

        if (csvFileLocation == null)
            return false;

        actors = new ArrayList<Actor>();    // Initialize our list of actors
        InputStream inputStream = null;
        String name, abb, tag, col, sex, age; // Name, Abbreviation, Tag no, Colony, Sex, Age

        try {
            File file = new File(csvFileLocation);
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

                    // Clean Strings (remove quotes)
                    name = name.replaceAll("^\"|\"$", "");
                    abb = abb.replaceAll("^\"|\"$", "");
                    tag = tag.replaceAll("^\"|\"$", "");
                    col = col.replaceAll("^\"|\"$", "");
                    sex = sex.replaceAll("^\"|\"$", "");
                    age = age.replaceAll("^\"|\"$", "");

                    // Parse x and y integers
                    int tag_parsed = Integer.parseInt(tag);
                    int sex_parsed = Integer.parseInt(sex);
                    int age_parsed = Integer.parseInt(age);

                    Actor actor = new Actor(name, abb, tag_parsed, col, sex_parsed, age_parsed);
                }
            }

            return true;

        } catch (IOException e) {

            actors = null;
            return false;

        } catch (Exception e) {

            actors = null;
            return false;

        } finally {

            try {

                inputStream.close();

            } catch (IOException e) { }
        }
    }
}
