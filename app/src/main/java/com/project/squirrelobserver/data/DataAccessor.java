package com.project.squirrelobserver.data;

import java.util.ArrayList;

/**
 * Created by sean on 2/12/16.
 */
public class DataAccessor {

    public LocationPoint originPoint;   // Location of origin in  UTMs (Universal Transverse Mercator coordinate system)
    public ArrayList<LocationPoint> locationPoints; // List of all points in coordinate system
    public ArrayList<Behavior> behaviors;   // List of all behaviors
    public ArrayList<Actor> actors;     // List of all actors

    public void generateListOfLocationPoints (String csvFileLocation) {

    }

    public void generateListOfBehaviors (String csvFileLocation) {

    }

    public void generateListOfActors (String csvFileLocation) {

    }
}
