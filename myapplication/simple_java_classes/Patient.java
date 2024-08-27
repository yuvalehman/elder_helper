package com.example.yuvallehman.myapplication.simple_java_classes;

public class Patient {
    String address;
    String first;
    String id;
    String last;
    String phone;
    String region;

    public Patient() {
    }

    public Patient(String first2, String last2, String phone2, String address2, String id2, String region2) {
        this.first = first2;
        this.last = last2;
        this.phone = phone2;
        this.address = address2;
        this.id = id2;
        this.region = region2;
    }

    public String getFirst() {
        return this.first;
    }

    public void setFirst(String first2) {
        this.first = first2;
    }

    public String getLast() {
        return this.last;
    }

    public void setLast(String last2) {
        this.last = last2;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address2) {
        this.address = address2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region2) {
        this.region = region2;
    }
}
