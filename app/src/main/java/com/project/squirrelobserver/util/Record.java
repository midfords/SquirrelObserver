package com.project.squirrelobserver.util;

import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by sean on 2/16/16.
 */
public class Record{

    private final int MAX_BEHAVIORS_SIZE = 3;

    public Actor actor = null;
    public Actor actee = null;
    private Queue<Behavior> behaviors;
    public int behaviorsSize;
    public ArrayList<String> modifiers;
    public Calendar date = null;
    public double x;
    public double y;
    public int relationship;
    public int groupSize;
    public String observerID;
    public int scanInterval;
    public boolean aoOnly;
    public DateFormat dateFormat = null;
    public DateFormat timeFormat = null;


    public Record(String observerID, boolean aoOnly) {

        dateFormat = new SimpleDateFormat("dd-MMM-yy");
        timeFormat = new SimpleDateFormat("HHmm");

        this.behaviors = new LinkedList<Behavior>();
        this.behaviorsSize = 0;
        this.modifiers = new ArrayList<String>();
        this.observerID = observerID;
        this.aoOnly = aoOnly;
        this.x = 0.0;
        this.y = 0.0;
        this.relationship = 0;
        this.groupSize = 0;
        this.scanInterval = 0;
    }

    public Behavior addBehavior(Behavior behavior) {

        Behavior removedBehavior = null;

        if (behaviorsSize < MAX_BEHAVIORS_SIZE) {

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

    public Behavior dequeueBehavior() {

        if (behaviorsSize <= 0)
            return null;

        Behavior behavior = (Behavior) behaviors.remove();
        behaviorsSize--;

        return behavior;
    }

    public Behavior getBehavior(int i) {

        if (i >= behaviorsSize)
            return null;
        else
            return (Behavior) ((LinkedList) behaviors).get(i);
    }

    public boolean requiresActee() {

        boolean requiresActee = false;

        for (int i = 0; i < behaviorsSize; i++) {

            requiresActee = requiresActee || getBehavior(i).requiresActee;
        }

        return requiresActee;
    }

    public boolean containsBehavior(Behavior b) {

        return behaviors != null && behaviors.contains(b);
    }
}
