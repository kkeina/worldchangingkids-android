package com.world_changingkids.model;

import android.content.Context;

public class Profile {

    private ActsOfKindnessCollection mActsOfKindnessCollection;
    private String name;
    private int age;
    private int grade;

    public Profile() { }

    public Profile(String name, int age, int grade, Context context) {
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.mActsOfKindnessCollection = ActsOfKindnessCollection.getInstance(context);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getGrade() {
        return grade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGrade(int grade){
        this.grade = grade;
    }

    public ActsOfKindnessCollection getActsOfKindnessCollection() {
        return mActsOfKindnessCollection;
    }

    public void setActsOfKindnessCollection(ActsOfKindnessCollection mActsOfKindnessCollection) {
        this.mActsOfKindnessCollection = mActsOfKindnessCollection;
    }

}
