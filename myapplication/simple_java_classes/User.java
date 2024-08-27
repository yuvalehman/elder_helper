package com.example.yuvallehman.myapplication.simple_java_classes;

import java.io.Serializable;

public class User implements Serializable {
    public static final long id = 1;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
    private long signUpDate = 0;
    private String signUpState;
    private long startLoggingDate = 0;
    private String username;

    public User() {
    }

    public void setStartLoggingDate(long startLoggingDate2) {
        this.startLoggingDate = startLoggingDate2;
    }

    public static long getId() {
        return 1;
    }

    public String getSignUpState() {
        return this.signUpState;
    }

    public void setSignUpState(String signUpState2) {
        this.signUpState = signUpState2;
    }

    public long getSignUpDate() {
        return this.signUpDate;
    }

    public User(String firstName2, String lastName2, String email2, String phone2, String username2, String signUpState2, long startLoggingDate2, long signUpDate2) {
        this.firstName = firstName2;
        this.lastName = lastName2;
        this.email = email2;
        this.phone = phone2;
        this.username = username2;
        this.signUpState = signUpState2;
        this.startLoggingDate = startLoggingDate2;
        this.signUpDate = signUpDate2;
    }

    public long getStartLoggingDate() {
        return this.startLoggingDate;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName2) {
        this.firstName = firstName2;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName2) {
        this.lastName = lastName2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username2) {
        this.username = username2;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password2) {
        this.password = password2;
    }
}
