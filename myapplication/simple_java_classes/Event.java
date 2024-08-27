package com.example.yuvallehman.myapplication.simple_java_classes;

import java.io.Serializable;

public class Event implements Serializable {
    public static final long id = 1;
    private long constParamsCreatedAt;
    private long createdAt;
    private long date;
    private String eventType;
    private double moneyGiven;
    private String moneyType;
    private long userParamsCreatedAt;
    private String username;
    private String workerId;

    public long getDate() {
        return this.date;
    }

    public Event(String username2, String workerId2, String eventType2, double moneyGiven2, long date2, long createdAt2, String moneyType2, long constParamsCreatedAt2, long userParamsCreatedAt2) {
        this.username = username2;
        this.workerId = workerId2;
        this.eventType = eventType2;
        this.moneyGiven = moneyGiven2;
        this.date = date2;
        this.createdAt = createdAt2;
        this.moneyType = moneyType2;
        this.constParamsCreatedAt = constParamsCreatedAt2;
        this.userParamsCreatedAt = userParamsCreatedAt2;
    }

    public String getMoneyType() {
        return this.moneyType;
    }

    public void setMoneyType(String moneyType2) {
        this.moneyType = moneyType2;
    }

    public static long getId() {
        return 1;
    }

    public String getUsername() {
        return this.username;
    }

    public String getWorkerId() {
        return this.workerId;
    }

    public String getEventType() {
        return this.eventType;
    }

    public double getMoneyGiven() {
        return this.moneyGiven;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setUsername(String username2) {
        this.username = username2;
    }

    public void setWorkerId(String workerId2) {
        this.workerId = workerId2;
    }

    public void setEventType(String eventType2) {
        this.eventType = eventType2;
    }

    public void setMoneyGiven(double moneyGiven2) {
        this.moneyGiven = moneyGiven2;
    }

    public void setCreatedAt(long createdAt2) {
        this.createdAt = createdAt2;
    }
}
