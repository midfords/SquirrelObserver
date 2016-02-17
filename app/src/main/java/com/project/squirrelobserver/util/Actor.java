package com.project.squirrelobserver.util;

/**
 * Created by sean on 2/12/16.
 */
public class Actor {

    public String name; // Name
    public String abb;  // Abbreviation
    public int tag;     // Tag no.
    public String colony;   // Colony code
    public int sex;     // Sex (1-m, 2-f)
    public int age;     // Age

    public Actor(String name, String abb, int tag, String colony, int sex, int age) {
        this.name = name;
        this.abb = abb;
        this.tag = tag;
        this.colony = colony;
        this.sex = sex;
        this.age = age;
    }
}
