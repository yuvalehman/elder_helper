package com.example.yuvallehman.myapplication.simple_java_classes;

import java.util.HashMap;
import java.util.Map;

public class ConstantParams {
    private static ConstantParams instance = new ConstantParams();
    private double compensation = 0.0d;
    private long createdAt;
    private double dailyMinPay = 0.0d;
    private int dailyWorkingHours = 0;
    private double foodLimitPercentFromMinSalary = 0.0d;
    private double fullJobMonthlyHours = 0.0d;
    private double healthInsuranceLimit = 0.0d;
    private double livingExpensesOthersLimit = 0.0d;
    private Map<String, Double> livingExpensesRentLimitMap = new HashMap();
    private int minimumSalary = 0;
    private double pension = 0.0d;
    private double saturdayOrHolidayBonus = 0.0d;
    private int sickDaysLimit = 0;
    private double sickDaysPerMonth = 0.0d;
    private Map<String, Integer> vacationDaysMap = new HashMap();
    private double wellFareBonusDailyFee = 0.0d;
    private Map<String, Integer> wellFareBonusMap = new HashMap();
    private int yearlyHolidays = 0;

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt2) {
        this.createdAt = createdAt2;
    }

    public double getDailyMinPay() {
        return this.dailyMinPay;
    }

    public void setDailyMinPay(double dailyMinPay2) {
        this.dailyMinPay = dailyMinPay2;
    }

    public static ConstantParams getInstance() {
        return instance;
    }

    public void setMinimumSalary(int minimumSalary2) {
        this.minimumSalary = minimumSalary2;
    }

    public void setFullJobMonthlyHours(double fullJobMonthlyHours2) {
        this.fullJobMonthlyHours = fullJobMonthlyHours2;
    }

    public void setWellFareBonusDailyFee(double wellFareBonusDailyFee2) {
        this.wellFareBonusDailyFee = wellFareBonusDailyFee2;
    }

    public void setSickDaysPerMonth(double sickDaysPerMonth2) {
        this.sickDaysPerMonth = sickDaysPerMonth2;
    }

    public void setYearlyHolidays(int yearlyHolidays2) {
        this.yearlyHolidays = yearlyHolidays2;
    }

    public void setSaturdayOrHolidayBonus(double saturdayOrHolidayBonus2) {
        this.saturdayOrHolidayBonus = saturdayOrHolidayBonus2;
    }

    public void setPension(double pension2) {
        this.pension = pension2;
    }

    public void setCompensation(double compensation2) {
        this.compensation = compensation2;
    }

    public void setDailyWorkingHours(int dailyWorkingHours2) {
        this.dailyWorkingHours = dailyWorkingHours2;
    }

    public void setSickDaysLimit(int sickDaysLimit2) {
        this.sickDaysLimit = sickDaysLimit2;
    }

    public void setLivingExpensesOthersLimit(double livingExpensesOthersLimit2) {
        this.livingExpensesOthersLimit = livingExpensesOthersLimit2;
    }

    public void setHealthInsuranceLimit(double healthInsuranceLimit2) {
        this.healthInsuranceLimit = healthInsuranceLimit2;
    }

    public void setFoodLimitPercentFromMinSalary(double foodLimitPercentFromMinSalary2) {
        this.foodLimitPercentFromMinSalary = foodLimitPercentFromMinSalary2;
    }

    public void setLivingExpensesRentLimitMap(Map<String, Double> livingExpensesRentLimitMap2) {
        this.livingExpensesRentLimitMap = livingExpensesRentLimitMap2;
    }

    public void setVacationDaysMap(Map<String, Integer> vacationDaysMap2) {
        this.vacationDaysMap = vacationDaysMap2;
    }

    public void setWellFareBonusMap(Map<String, Integer> wellFareBonusMap2) {
        this.wellFareBonusMap = wellFareBonusMap2;
    }

    public int getMinimumSalary() {
        return this.minimumSalary;
    }

    public double getFullJobMonthlyHours() {
        return this.fullJobMonthlyHours;
    }

    public double getWellFareBonusDailyFee() {
        return this.wellFareBonusDailyFee;
    }

    public double getSickDaysPerMonth() {
        return this.sickDaysPerMonth;
    }

    public int getYearlyHolidays() {
        return this.yearlyHolidays;
    }

    public double getSaturdayOrHolidayBonus() {
        return this.saturdayOrHolidayBonus;
    }

    public double getPension() {
        return this.pension;
    }

    public double getFoodLimitPercentFromMinSalary() {
        return this.foodLimitPercentFromMinSalary;
    }

    public double getCompensation() {
        return this.compensation;
    }

    public int getDailyWorkingHours() {
        return this.dailyWorkingHours;
    }

    public Map<String, Integer> getVacationDaysMap() {
        return this.vacationDaysMap;
    }

    public Map<String, Integer> getWellFareBonusMap() {
        return this.wellFareBonusMap;
    }

    public int getSickDaysLimit() {
        return this.sickDaysLimit;
    }

    public double getLivingExpensesOthersLimit() {
        return this.livingExpensesOthersLimit;
    }

    public double getHealthInsuranceLimit() {
        return this.healthInsuranceLimit;
    }

    public Map<String, Double> getLivingExpensesRentLimitMap() {
        return this.livingExpensesRentLimitMap;
    }
}
