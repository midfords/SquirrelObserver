package com.project.squirrelobserver.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sean on 2/12/16.
 */
public class FileParser {

    public static boolean generateListOfLocationPoints (String csvFileLocation) {

        if (csvFileLocation == null || csvFileLocation.isEmpty())
            return false;
        String extension = csvFileLocation.substring(
                csvFileLocation.lastIndexOf("."), csvFileLocation.length());
        if (!".csv".equalsIgnoreCase(extension))
            return false;

        GlobalVariables.locationPointsX = new ArrayList<LocationPoint>();    // Initialize our list of points
        GlobalVariables.locationPointsY = new ArrayList<LocationPoint>();    // Initialize our list of points
        InputStream inputStream = null;
        String label, x, y;

        try {
            File file = new File(csvFileLocation);
            if (!file.exists())
                return false;

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

                    // Check if we're reading the origin point
                    if ("origin".equalsIgnoreCase(label)) {

                        // Parse x and y integers
                        int origin_x_parsed = 0;
                        int origin_y_parsed = 0;

                        // Attempt to parse the int
                        try { origin_x_parsed = Integer.parseInt(x); }
                        catch (Exception e) { }
                        try { origin_y_parsed = Integer.parseInt(y); }
                        catch (Exception e) { }

                        GlobalVariables.originX = origin_x_parsed;
                        GlobalVariables.originY = origin_y_parsed;

                    } else {

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
            }

            return true;

        } catch (Exception e) {

            GlobalVariables.locationPointsX = null;
            GlobalVariables.locationPointsY = null;
            return false;

        } finally {

            try {

                if (inputStream != null) inputStream.close();

            } catch (Exception e) { }
        }
    }

    public static boolean generateListOfBehaviors (String csvFileBehaviors) {

        if (csvFileBehaviors == null || csvFileBehaviors.isEmpty())
            return false;
        String extension = csvFileBehaviors.substring(
                csvFileBehaviors.lastIndexOf("."), csvFileBehaviors.length());
        if (!".csv".equalsIgnoreCase(extension))
            return false;

        GlobalVariables.behaviors = new ArrayList<Behavior>();    // Initialize our list of behaviors
        InputStream inputStream = null;
        String code, reqActee, desc, isAO, modif;   // Code, Requires Acee, Description, Is All-Occ and Modifier

        try {
            File file = new File(csvFileBehaviors);
            if (!file.exists())
                return false;

            inputStream = new BufferedInputStream(new FileInputStream(file));

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            // Skip first line, it contains the column headers
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] dataRow = line.split(",");

                if (dataRow.length >= 3) { // Don't use row if there are incomplete fields

                    code = dataRow[0];
                    desc = dataRow[1];
                    reqActee = dataRow[2];
                    isAO = dataRow[3];

                    // Generate list of modifiers
                    ArrayList<String> modifiers = new ArrayList<String>();
                    for (int i = 4; i < dataRow.length; i++) {

                        modif = dataRow[i];
                        modifiers.add(modif);
                    }

                    try {
                        // Parse code integer
                        int code_parsed = Integer.parseInt(code);
                        int reqActee_parsed = Integer.parseInt(reqActee);
                        boolean reqActee_bool = reqActee_parsed == 1;
                        int isAO_parsed = Integer.parseInt(isAO);
                        boolean isAO_bool = isAO_parsed == 1;

                        Behavior behavior = new Behavior(code_parsed, reqActee_bool, desc, isAO_bool, modifiers);
                        GlobalVariables.behaviors.add(behavior);

                    } catch (Exception e) { }
                }
            }
            GlobalVariables.aoBehaviors = null;
            return true;

        } catch (Exception e) {

            GlobalVariables.behaviors = null;
            return false;

        } finally {

            try {

                if (inputStream != null) inputStream.close();

            } catch (Exception e) { }
        }
    }

    public static boolean generateListOfActors (String csvFileActors) {

        if (csvFileActors == null || csvFileActors.isEmpty())
            return false;
        String extension = csvFileActors.substring(
                csvFileActors.lastIndexOf("."), csvFileActors.length());
        if (!".csv".equalsIgnoreCase(extension))
            return false;

        GlobalVariables.actors = new ArrayList<Actor>();    // Initialize our list of actors
        InputStream inputStream = null;
        String name, abb, tag, col, sex, age; // Name, Abbreviation, Tag no, Colony, Sex, Age

        try {
            File file = new File(csvFileActors);
            if (!file.exists())
                return false;

            inputStream = new BufferedInputStream(new FileInputStream(file));

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            // Skip first line, it contains the column headers
            reader.readLine().toString();

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

        } catch (Exception e) {

            GlobalVariables.actors = null;
            return false;

        } finally {

            try {

                if (inputStream != null) inputStream.close();

            } catch (Exception e) { }
        }
    }

    public static boolean writeLineToRecordCSV(String csvRecordPath, Record record) {

        if (csvRecordPath == null || record == null)
            return false;
        if (record.actor == null)
            return false;

        FileWriter csvWriter = null;

        try {

            File file = new File(csvRecordPath);

            if (!file.exists()){

                file.createNewFile();
                csvWriter = new FileWriter(file, true);

                // Write header to new file
                csvWriter.write(GlobalVariables.csvScanDataHeader);
                csvWriter.append('\n');

            } else {

                csvWriter = new FileWriter(file, true);
            }

            StringBuilder recordLine = new StringBuilder();

            // Write actor information
            recordLine.append(record.actor.tag);
            recordLine.append(',');
            recordLine.append(record.actor.sex);
            recordLine.append(',');
            recordLine.append(record.actor.age);
            recordLine.append(',');
            recordLine.append(record.actor.colony);
            recordLine.append(',');

            // Write the date
            record.date = Calendar.getInstance();
            recordLine.append(record.dateFormat.format(record.date.getTime()));
            recordLine.append(',');

            // Write the location coordinates
            recordLine.append(record.x);
            recordLine.append(',');
            recordLine.append(record.y);
            recordLine.append(',');

            // Write the behaviors
            Behavior behavior0 = record.getBehavior(0);
            if (behavior0 != null)
                recordLine.append(behavior0.code);
            recordLine.append(',');

            Behavior behavior1 = record.getBehavior(1);
            if (behavior1 != null)
                recordLine.append(behavior1.code);
            recordLine.append(',');

            Behavior behavior2 = record.getBehavior(2);
            if (behavior2 != null)
                recordLine.append(behavior2.code);
            recordLine.append(',');

            // Write all modifiers to single csv field. Surround in "" to avoid comma problems
            String modifiersString = "\"";
            for (int i = 0; i < record.modifiers.size(); i++) {

                modifiersString += record.modifiers.get(i);
                modifiersString += ",";
            }
            modifiersString += "\"";

            recordLine.append(modifiersString);
            recordLine.append(',');

            // Write actee information if record contains an actee
            if (record.actee != null) {

                recordLine.append(record.actee.sex);
                recordLine.append(',');
                recordLine.append(record.actee.age);
                recordLine.append(',');
                recordLine.append(record.actee.tag);
                recordLine.append(',');

            } else {

                recordLine.append(",,,");
            }

            // Write additional info
            if(record.relationship > 0) {
                recordLine.append(record.relationship);
            }
            recordLine.append(',');

            // Write Time
            recordLine.append(record.timeFormat.format(record.date.getTime()));
            recordLine.append(',');

            recordLine.append(record.groupSize);
            recordLine.append(',');

            recordLine.append(record.observerID);
            recordLine.append(',');

            recordLine.append(record.scanInterval);
            recordLine.append(',');
            // Finish line
            csvWriter.write(recordLine.toString());
            csvWriter.append('\n');
            csvWriter.close();

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public static boolean clearRecordCSV(String csvRecordPath) {

        if (csvRecordPath == null || csvRecordPath.isEmpty())
            return false;

        FileWriter csvWriter = null;

        try {

            File file = new File(csvRecordPath);

            // If record file already exists, delete it
            if (file.exists() && !file.delete()) {

                return false;
            }

            // Create new file
            file = new File(csvRecordPath);
            file.createNewFile();
            csvWriter = new  FileWriter(file, true);

            // Write header to new file
            csvWriter.write(GlobalVariables.csvScanDataHeader);
            csvWriter.append('\n');
            csvWriter.close();

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public static boolean exportRecordCSV(Context context, String csvRecordPath) {

        if (csvRecordPath == null || csvRecordPath.isEmpty() || csvRecordPath.equals(".csv"))
            return false;

        try {

            File file = new File(csvRecordPath);

            File exportDownloadPath = new File(GlobalVariables.exportDownloadPath);
            exportDownloadPath.mkdirs();

            File fileCopy = new File(GlobalVariables.exportDownloadPath + file.getName());

            if (file.exists() && copyFile(file, fileCopy)) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileCopy));

                context.startActivity(sendIntent);

                return true;
            }

            return false;

        } catch (Exception e) {

            return false;
        }
    }

    public static boolean removeValueFromAppSettings(String settingsPath, String tag) {

        if (settingsPath == null || settingsPath.isEmpty())
            return false;
        if (tag == null || tag.isEmpty())
            return false;

        FileWriter csvWriter = null;
        InputStream inputStream = null;

        try {
            File file = new File(settingsPath);

            // If settings don't exist, create the file
            if (!file.exists()) {

                return false;
            }

            inputStream = new BufferedInputStream(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            ArrayList<String> settingsLines = new ArrayList<String>();
            String line;

            // Read in entire settings file
            while ((line = reader.readLine()) != null) {

                settingsLines.add(line);
            }

            // Delete settings file contents
            file.delete();
            file.createNewFile();

            // Write settings values while ignoring removed tag
            csvWriter = new FileWriter(file, true);
            for (int i = 0; i < settingsLines.size(); i++) {

                String[] data = settingsLines.get(i).split(GlobalVariables.settingsDelimiter);

                if (!tag.equals(data[0])) {

                    csvWriter.write(settingsLines.get(i));
                    csvWriter.append('\n');
                }
            }

            csvWriter.close();

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public static boolean writeValueToAppSettings(String settingsPath, String tag, String value) {

        if (settingsPath == null || settingsPath.isEmpty())
            return false;
        if (tag == null || tag.isEmpty())
            return false;
        if (value == null || value.isEmpty())
            return false;

        FileWriter csvWriter = null;
        InputStream inputStream = null;

        try {

            File file = new File(settingsPath);

            // If settings don't exist, create the file
            if (!file.exists()) {

                // Create new settings file
                file.createNewFile();
            }

            inputStream = new BufferedInputStream(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            ArrayList<String> settingsLines = new ArrayList<String>();
            String newSettingsLine = tag + GlobalVariables.settingsDelimiter + value;
            String line;
            boolean wroteSetting = false;

            // Read in entire settings file
            while ((line = reader.readLine()) != null) {

                settingsLines.add(line);
            }

            // Delete settings file contents
            file.delete();
            file.createNewFile();

            // Replace settings value while writing back all data
            csvWriter = new FileWriter(file, true);
            for (int i = 0; i < settingsLines.size(); i++) {

                String[] data = settingsLines.get(i).split(GlobalVariables.settingsDelimiter);

                if (tag.equals(data[0])) {

                    settingsLines.remove(i);
                    settingsLines.add(i, newSettingsLine);
                    wroteSetting = true;
                }

                csvWriter.write(settingsLines.get(i));
                csvWriter.append('\n');
            }

            // Write new setting
            if (!wroteSetting) {

                csvWriter.write(newSettingsLine);
                csvWriter.append('\n');
            }

            csvWriter.close();

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public static boolean importAppSettings(String settingsPath) {

        if (settingsPath == null || settingsPath.isEmpty())
            return false;

        FileWriter csvWriter = null;
        InputStream inputStream = null;

        try {

            File file = new File(settingsPath);

            // If settings don't exist, create the file
            if (!file.exists()) {

                // Create new settings file
                file.createNewFile();

            } else {

                inputStream = new BufferedInputStream(new FileInputStream(file));
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] dataRow = line.split(GlobalVariables.settingsDelimiter);

                    if (GlobalVariables.settingsLocationTag.equals(dataRow[0])) {

                        GlobalVariables.locationCSVPath = dataRow[1];
                    } else if (GlobalVariables.settingsActorTag.equals(dataRow[0])) {

                        GlobalVariables.actorsCSVPath = dataRow[1];
                    } else if (GlobalVariables.settingsBehaviorTag.equals(dataRow[0])) {

                        GlobalVariables.behaviorsCSVPath = dataRow[1];
                    }
                }
            }

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    private static boolean copyFile(File source, File destination) {

        try {
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(destination);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}
