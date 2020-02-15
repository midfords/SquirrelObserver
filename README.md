![Alt](/screenshots/banner.png "Banner")

![Alt](/screenshots/logo.png "Logo")

# Squirrel Observer
Version 1.0

Created by: Sean Midford, William Richards McNaughton

Squirrel Observer is an Android application designed for recording squirrel observations in the field. This app was designed for Android 4.4 KitKat and may not work on older versions of Android. We have created two versions of the app to solve some device specific issues you may encounter. Use app version B only if you experience problems with app version A (Described below in Installation Instructions). If the app should crash for any reason (we're only human) don't worry, all your data will be safe!

## Installation
Uninstall all previous versions of the app before attempting the install.
1. Navigate to android security settings (Settings > Security) and ensure "Unknown Sources" is checked. (Note: These steps may differ for different devices).
2. Download or Transfer the apk (Version A) to the device. You can do this by plugging the device into a PC or Mac and dragging the apk file onto the device.
3. At this point, we strongly recommend downloading a popular file manager (Such as ES File Explorer File Manager or File Manager HD). The File Manager app will help when using Squirrel Observer's import and export functions.
4. Navigate to the directory the apk file was transferred to in the file manager of your choice, and click on the apk to install the app.
5. You should now have the app installed and ready to use on your device.

>Note: If you experience problems with the app (the app is crashing for example), try uninstalling the app and install Version B using the same steps as above.

## CSV Formatting
The Squirrel Observer app uses csv (comma separated value) files for data management. This section will describe how these files should be formatted to import your data correctly into the app. If any rows are invalid in the csv files, they will simply be ignored but all other valid rows will still be imported successfully. Csv files can also be changed at any time using the app of your choice, changes made in csv files will appear in the app when the app is restarted or if the csv file is reimported. The first row of any CSV file will be ignored and is expected to be the column headers for the file.

### Location CSV File
The Location csv file is used for importing the set of location points for your reference stones at the site. This file also includes the origin point (UTM). The following is a sample of how the Location csv file should look:

>Note: The Origin point does not have to be the first row of the file.

| Label  | X (Int) | Y (Int)  |
|--------|:-------:|----------:|
| Origin | 706832  | 4344683  |
| 1      | 0       | 10       |
| -1     | 0       | -10      |
| A      | 10      | 0        |
| -A     | -10     | 0        |

### Actors CSV File
The Actors csv file describes all information about the actors and actees. The following is a sample of how the Actors csv file should look:

| Actor Name  | Abbreviation | Tag no. (Int)  | Colony  | Sex (Int) | Age (Int)  |
|-------------|:------------:|---------------:|--------:|----------:|-----------:|
| 2 squiggles | 2sq          | 228            | 8c      | 1         | 1          |
| EY vert     | eyv          | 351            | 37      | 2         | 3          |

Buttons in the app will be shown using the 'Actor Name' field, however items can be filtered using either the 'Actor Name' or 'Abbreviation' fields.
In the 'Sex' field, "1" defines male and "2" defines female.

### Behaviors CSV File
The behaviors csv file defines all information about the observable behaviors. It also allows you to define which behaviors are all occurrences behaviors. The following is a sample of how the Behaviors csv file should look:

| Behavior Code (Int) | Behavior Description | Requires Actee | Is All Occurrences | Modifiers |     |
|---------------------|:--------------------:|---------------:|-------------------:|----------:|----:|
| 1                   | Feed                 | 1              | 0                  |           |     |
| 2                   | Run                  | 0              | 1                  | >5m       | <5m |

* The 'Requires Actee' field can be either "0" meaning false or "1" meaning true.
* The 'Is All Occurences' field can be either "0" meaning false or "1" meaning true.
* The 'Modifiers' field defines all modifiers associated with a behavior. Simply fill as many fields as needed with modifiers, starting at the 'Modifiers' column. Note: List all modifiers needed for each behavior, even if they are repeated from another behavior.

## App Workflow
### Importing CSV Files
1. To import your csv files, pull out the 'navigation drawer from the app's main screen and navigate to 'Import/Export'. [Fig. 1.1]
2. Select the 'Import' button for whichever csv file you are importing (Location, Actors or Behaviors). [Fig. 1.2]
3. Choose a source to import from and select your desired csv file. [Fig. 1.3]

![Alt](/screenshots/screenshot-1.1.png "Screenshot")

*Fig. 1.1*


![Alt](/screenshots/screenshot-1.2.png "Screenshot")

*Fig. 1.2*


![Alt](/screenshots/screenshot-1.3.png "Screenshot")

*Fig. 1.3*


### Exporting CSV Files
1. To export your recorded observations, pull out the 'navigation drawer' from the app's main screen and navigate to 'Import/Export'. [Fig. 1.1]
2. Select the 'Export' button for the records you would like to export. (Scan or All Occurrences). [Fig 2.1]
3. Choose a destination to export to. No matter which app you use to export (Google Drive, Dropbox, etc.) a copy of your data will also be kept on the device in the following directory:
            `/sdcard/Download/SquirrelObserver/`

>Note: Exported data is NOT deleted from the app. Any further recorded observations are simply added to the bottom of the current file. To clear your records file, see 'Clearing CSV File'.
>Note: To view your exported files from a PC or Mac, you may need to restart the device.

![Alt](/screenshots/screenshot-2.1.png "Screenshot")

*Fig. 2.1*


### Clearing CSV Files
To clear your current recorded observations, pull out the 'navigation drawer' from the app's main screen and navigate to 'Import/Export'. [Fig. 1.1]

Select the 'Clear' button for the record file you would like to delete. (Scan or All Occurrences). [Fig 2.1]

>Warning: This action cannot be undone. Remember to export your data first!

Note: Clearing the data will clear the app's version of the data. Any files that have been exported, including the files created and stored in 
            `/sdcard/Download/SquirrelObserver/`
will NOT be deleted.

### Setup Recording Session
There are two ways to setup a recording session. You can start a 'Scan Session' which allows for both Scan observations and All Occurrences observations. Or you can start an 'All Occurrences Session' which will only allow for All Occurrences observations.

To start a Scan Session, fill out your observer id on the main app screen [Fig 4.1]. You can also modify the scan interval and all occurrences behaviors from the main screen. The scan interval will default to 10 minutes, and the All Occurrences behaviors will default to what was set in the behaviors csv file.

To start an All Occurrences Session, select the '+' button at the top right corner of the main screen or the Import/Export screen. Enter your observer id when prompted [Fig 4.2].

The flow of observations in Scan mode and All Occurrences mode are virtually the same. The only difference is in Scan mode you have the option to switch to All Occurrences mode temporarily. There is also a scan timer in Scan mode that will vibrate the device when an interval is completed.

![Alt](/screenshots/screenshot-4.1.png "Screenshot")

*Fig. 4.1*


![Alt](/screenshots/screenshot-4.2.png "Screenshot")

*Fig. 4.2*


The steps for making an observation are as follows:
1. Select your 'Actor'. Buttons are colour coded by sex. The four most recently used buttons are kept at the top for quick access [Fig 4.3]. Buttons can be filtered by typing in the 'Filter' field [Fig 4.4].
2. Once you have selected your actor, switch to the behaviors tab and select up to three behaviors. The four most frequently used buttons are kept at the top. [Fig 4.5]
   * If at least one of the selected behaviors requires an Actee, the actee tab will activate.
   * Select your 'Actee'. Buttons are colour coded by sex. The four most recently used buttons are kept at the top for quick access. [Fig 4.6]
   * If at least one of the selected behaviors has available modifiers, the modifiers tab will activate.
   * Select as many 'Modifiers' as you want. Modifiers are optional. [Fig 4.7]
3. Once all required information has been entered the 'Other' tab will activate.
4. Select your grid point from the drop down lists. You can modify the point coordinates using the number fields below. Select your group size, using the buttons available or by using the keyboard. [Fig 4.8]
5. Once all your information has been entered, press the 'Record' button to record data. If any of the selected behaviors are also an All Occurrences behavior, the observation will be recorded in both the Scan file and the All Occurrences file.
6. If you are in Scan mode, recorded Actor's buttons will disabled until the current interval has ended.
7. End your current session by using the device's 'Back' button or the 'End' button.

![Alt](/screenshots/screenshot-4.3.png "Screenshot")

*Fig. 4.3*


![Alt](/screenshots/screenshot-4.4.png "Screenshot")

*Fig. 4.4*


![Alt](/screenshots/screenshot-4.5.png "Screenshot")

*Fig. 4.5*


![Alt](/screenshots/screenshot-4.6.png "Screenshot")

*Fig. 4.6*


![Alt](/screenshots/screenshot-4.7.png "Screenshot")

*Fig. 4.7*


![Alt](/screenshots/screenshot-4.8.png "Screenshot")

*Fig. 4.8*

