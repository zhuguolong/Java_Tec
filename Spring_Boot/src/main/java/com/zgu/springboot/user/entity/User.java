package com.zgu.springboot.user.entity;

public class User {
    /**
     * 姓名
     */
    private String userName;
    /**
     * 性别 0女1男
     */
    private int gender = 1;
    /**
     * 年龄
     */
    private int age = 0;

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public int getGender() {
        return gender;
    }

    public User setGender(int gender) {
        this.gender = gender;
        return this;
    }

    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                '}';
    }
}
