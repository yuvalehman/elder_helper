package com.example.yuvallehman.myapplication.simple_java_classes;

public class Preferences {
    public static final int MONTH = 102;
    public static final int WEEK = 101;
    private static Preferences instance = new Preferences();
    private boolean ElderHouse = false;
    private boolean Work7days;
    private long createdAt = -1;
    private int dayOfPay = 7;
    private boolean gettingFromCompany = false;
    private float healthInsuranceDeduction;
    private boolean isPocketMoneyPartOfPay = false;
    private float livingExpensesDeduction;
    private float nutritionPay;
    private int nutritionPayWeekOrMonth = 101;
    private float pocketMoney;
    private int pocketMoneyWeekOrMonth = 101;
    private float rentDeduction;
    private float saturdayBonus;
    private float totalPay;

    public static Preferences getInstance() {
        return instance;
    }

    private Preferences() {
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt2) {
        this.createdAt = createdAt2;
    }

    public float getSaturdayBonus() {
        return this.saturdayBonus;
    }

    public void setSaturdayBonus(float saturdayBonus2) {
        this.saturdayBonus = saturdayBonus2;
    }

    public float getTotalPay() {
        return this.totalPay;
    }

    public void setTotalPay(float totalPay2) {
        this.totalPay = totalPay2;
    }

    public float getRentDeduction() {
        return this.rentDeduction;
    }

    public void setRentDeduction(float rentDeduction2) {
        this.rentDeduction = rentDeduction2;
    }

    public float getLivingExpensesDeduction() {
        return this.livingExpensesDeduction;
    }

    public void setLivingExpensesDeduction(float livingExpensesDeduction2) {
        this.livingExpensesDeduction = livingExpensesDeduction2;
    }

    public float getPocketMoney() {
        return this.pocketMoney;
    }

    public void setPocketMoney(float pocketMoney2) {
        this.pocketMoney = pocketMoney2;
    }

    public float getHealthInsuranceDeduction() {
        return this.healthInsuranceDeduction;
    }

    public void setHealthInsuranceDeduction(float healthInsuranceDeduction2) {
        this.healthInsuranceDeduction = healthInsuranceDeduction2;
    }

    public float getNutritionPay() {
        return this.nutritionPay;
    }

    public void setNutritionPay(float nutritionPay2) {
        this.nutritionPay = nutritionPay2;
    }

    public boolean isWork7days() {
        return this.Work7days;
    }

    public void setWork7days(boolean work7days) {
        this.Work7days = work7days;
    }

    public boolean isPocketMoneyPartOfPay() {
        return this.isPocketMoneyPartOfPay;
    }

    public void setPocketMoneyPartOfPay(boolean pocketMoneyPartOfPay) {
        this.isPocketMoneyPartOfPay = pocketMoneyPartOfPay;
    }

    public boolean isGettingFromCompany() {
        return this.gettingFromCompany;
    }

    public void setGettingFromCompany(boolean gettingFromCompany2) {
        this.gettingFromCompany = gettingFromCompany2;
    }

    public boolean isElderHouse() {
        return this.ElderHouse;
    }

    public void setElderHouse(boolean elderHouse) {
        this.ElderHouse = elderHouse;
    }

    public void clear() {
        instance = new Preferences();
    }

    public int getPocketMoneyWeekOrMonth() {
        return this.pocketMoneyWeekOrMonth;
    }

    public void setPocketMoneyWeekOrMonth(int pocketMoneyWeekOrMonth2) {
        this.pocketMoneyWeekOrMonth = pocketMoneyWeekOrMonth2;
    }

    public int getNutritionPayWeekOrMonth() {
        return this.nutritionPayWeekOrMonth;
    }

    public void setNutritionPayWeekOrMonth(int nutritionPayWeekOrMonth2) {
        this.nutritionPayWeekOrMonth = nutritionPayWeekOrMonth2;
    }

    public int getDayOfPay() {
        return this.dayOfPay;
    }

    public void setDayOfPay(int dayOfPay2) {
        this.dayOfPay = dayOfPay2;
    }
}
