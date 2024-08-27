package com.example.yuvallehman.myapplication.simple_java_classes;

public class PayItem {
    private String title;
    private String value;

    public PayItem(String title2, String value2) {
        this.title = title2;
        this.value = value2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value2) {
        this.value = value2;
    }
}
