package com.example.jiashuaishuai.androidreflection;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by jiashuaishuai on 2016/4/14.
 */
public class Man extends Person implements Observer {
    private int age = 0;
    private String var1 = "I am var1";
    public int var2 = 20;

    public Man() {
        System.out.println("I am Man" + var1);
        age = 20;
    }

    private String getTest(String name, int age) {

        Log.e("测试这个方法运行没有", "该方法运行" + name + "age-" + age);
        return "返回测试数据";
    }

    public Man(int i) {
        Log.e("测试构造方法单参数", "-----");
    }

    public Man(int i, String name) {
        Log.e("测试构造方法单参数", "-----"+i+"name"+name);
    }

    public int myAge() {
        return 28;
    }

    public String myName() {
        return "Jerome";
    }

    private void getName() {
        System.out.println("I am Jerome");
    }

    /**
     * @hide
     */
    private void getAge() {
        System.out.println("I am " + age);
    }

    @Override
    void getPhone() {
        System.out.println("I am sun , My age is " + age + "___" + var2);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
