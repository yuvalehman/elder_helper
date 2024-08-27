package com.example.yuvallehman.myapplication.simple_java_classes;

import java.util.GregorianCalendar;

public class PastRecord {
    private static PastRecord instance = new PastRecord();
    private GregorianCalendar calendar = new GregorianCalendar();
    private long createdAt = -1;
    private boolean gotFromCompany = false;
    private int holidays = 0;
    private int sickDays = 0;
    private float totalCompanyPay = 0.0f;
    private int vacationDays = 0;
    private int wellFareDaysPaid = 0;

    public static PastRecord getInstance() {
        return instance;
    }

    private PastRecord() {
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt2) {
        this.createdAt = createdAt2;
    }

    public int getVacationDays() {
        return this.vacationDays;
    }

    public void setVacationDays(int vacationDays2) {
        this.vacationDays = vacationDays2;
    }

    public int getSickDays() {
        return this.sickDays;
    }

    public void setSickDays(int sickDays2) {
        this.sickDays = sickDays2;
    }

    public int getHolidays() {
        return this.holidays;
    }

    public void setHolidays(int holidays2) {
        this.holidays = holidays2;
    }

    public int getWellFareDaysPaid() {
        return this.wellFareDaysPaid;
    }

    public void setWellFareDaysPaid(int wellFareDaysPaid2) {
        this.wellFareDaysPaid = wellFareDaysPaid2;
    }

    public float getTotalCompanyPay() {
        return this.totalCompanyPay;
    }

    public void setTotalCompanyPay(float totalCompanyPay2) {
        this.totalCompanyPay = totalCompanyPay2;
    }

    public boolean isGotFromCompany() {
        return this.gotFromCompany;
    }

    public void setGotFromCompany(boolean gotFromCompany2) {
        this.gotFromCompany = gotFromCompany2;
    }

    public void clear() {
        instance = new PastRecord();
    }
}
