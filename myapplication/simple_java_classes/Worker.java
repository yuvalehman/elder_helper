package com.example.yuvallehman.myapplication.simple_java_classes;

import java.io.Serializable;

public class Worker implements Serializable {
    private static final String TAG = "Worker";
    public static final long id = 1;
    private String country;
    private long dateOfHire = 0;
    private String workerEmail;
    private String workerFirstName;
    private String workerIdNumber;
    private String workerLastName;
    private String workerPhone;

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country2) {
        this.country = country2;
    }

    public Worker() {
    }

    public long getDateOfHire() {
        return this.dateOfHire;
    }

    public void setDateOfHire(long dateOfHire2) {
        this.dateOfHire = dateOfHire2;
    }

    public Worker(String workerIdNumber2) {
        this.workerIdNumber = workerIdNumber2;
    }

    public String getWorkerFirstName() {
        return this.workerFirstName;
    }

    public void setWorkerFirstName(String workerFirstName2) {
        this.workerFirstName = workerFirstName2;
    }

    public String getWorkerLastName() {
        return this.workerLastName;
    }

    public void setWorkerLastName(String workerLastName2) {
        this.workerLastName = workerLastName2;
    }

    public String getWorkerPhone() {
        return this.workerPhone;
    }

    public String getWorkerEmail() {
        return this.workerEmail;
    }

    public void setWorkerEmail(String workerEmail2) {
        this.workerEmail = workerEmail2;
    }

    public String getWorkerIdNumber() {
        return this.workerIdNumber;
    }

    public void setWorkerIdNumber(String workerIdNumber2) {
        this.workerIdNumber = workerIdNumber2;
    }
}
