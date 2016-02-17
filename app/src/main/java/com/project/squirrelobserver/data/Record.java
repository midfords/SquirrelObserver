package com.project.squirrelobserver.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by sean on 2/16/16.
 */
public class Record implements Serializable {

    public Actor actor;
    public Actor actee;
    private Queue<Behavior> behaviors;
    public int behaviorsSize;
    public ArrayList<String> modifiers;
    public Date date;
    public double x;
    public double y;
    public int relationship;
    public int groupSize;
    public String observerID;
    public boolean aoOnly;

    public Record(String observerID, boolean aoOnly) {

        this.behaviors = new LinkedList<Behavior>();
        this.behaviorsSize = 0;
        this.modifiers = new ArrayList<String>();
        this.observerID = observerID;
        this.aoOnly = aoOnly;
    }

    public Behavior addBehavior(Behavior behavior) {

        Behavior removedBehavior = null;

        if (behaviorsSize < 3) {

            behaviors.add(behavior);
            behaviorsSize++;
        } else {

            removedBehavior = behaviors.remove();
            behaviors.add(behavior);
        }

        return removedBehavior;
    }

    public void removeBehavior(Behavior behavior) {

        if (behaviors.remove(behavior)) {

            behaviorsSize--;
        }
    }
}
