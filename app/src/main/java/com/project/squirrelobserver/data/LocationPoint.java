package com.project.squirrelobserver.data;

/**
 * Created by sean on 2/12/16.
 */
public class LocationPoint {

    public String label;    // Name of point (1, 2, A, -A, etc)
    public int x;           // x distance from origin (0, 10, 20, -10, etc)
    public int y;           // y distance from origin (0, 10, 20, -10, etc)

    public LocationPoint(String label, int x, int y) {
        this.label = label;
        this.x = x;
        this.y = y;
    }
}
