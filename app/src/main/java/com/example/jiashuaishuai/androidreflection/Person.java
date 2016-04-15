package com.example.jiashuaishuai.androidreflection;

/**
 * Created by jiashuaishuai on 2016/4/14.
 */
public abstract class Person {
    String name = "";
    private int age = 0;
    public int fPubVar = 0;

    abstract void getPhone();

    public Person() {
        System.out.println("I am Farther");
    }

    int myAge() {
        return 50;
    }

    String myName() {
        return "Father";
    }

    public void callSun() {
        getPhone();
        priMethod(25);
    }

    private void priMethod(Integer a) {
        age = a;
        System.out.println("I am priMethod , Dont call me! age " + age);
    }

    /**
     * @hide
     */
    public void hideMethod(String name) {
        System.out.println("I am hideMethod , Dont call me! name:" + name
                + "   age:" + age);
    }
}
