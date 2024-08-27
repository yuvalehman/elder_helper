package com.example.yuvallehman.myapplication.helpers;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import com.example.yuvallehman.myapplication.desktop.DesktopActivity;
import com.example.yuvallehman.myapplication.desktop.DesktopViewModel;
import com.example.yuvallehman.myapplication.server_side_functions.ServerDataSupplier;
import com.example.yuvallehman.myapplication.simple_java_classes.ConstantParams;
import com.example.yuvallehman.myapplication.simple_java_classes.Preferences;
import java.util.GregorianCalendar;

public class Calculations {
    private static final int ID_LENGTH = 9;
    private static final String TAG = "Calculations";
    private static ConstantParams constantParams = sds.getConstantParams();
    private static long dateOfHire = sds.getWorker().getDateOfHire();
    private static Preferences preferences = sds.getPreferences();
    private static ServerDataSupplier sds = ServerDataSupplier.getInstance();

    public static double calWellFareMonthlyBonus() {
        GregorianCalendar now = new GregorianCalendar();
        GregorianCalendar start = new GregorianCalendar();
        start.setTimeInMillis(dateOfHire);
        int seniorityInYears = ((((now.get(1) - start.get(1)) * 12) + now.get(2)) - start.get(2)) / 12;
        int daysOfWellFareDeserved = 0;
        for (String yearString : constantParams.getWellFareBonusMap().keySet()) {
            if (!(Integer.parseInt(yearString) < seniorityInYears || constantParams.getWellFareBonusMap() == null || constantParams.getWellFareBonusMap().get(yearString) == null)) {
                daysOfWellFareDeserved = constantParams.getWellFareBonusMap().get(yearString).intValue();
            }
        }
        return (((double) daysOfWellFareDeserved) * constantParams.getWellFareBonusDailyFee()) / 12.0d;
    }

    public static double calSaturdayDailyPay() {
        return (((double) preferences.getTotalPay()) / constantParams.getFullJobMonthlyHours()) * ((double) constantParams.getDailyWorkingHours()) * constantParams.getSaturdayOrHolidayBonus();
    }

    public static boolean idVerify(String idNumber) {
        if (idNumber == null || idNumber.equals("")) {
            return false;
        }
        String[] newStringSplit = new String[9];
        int[] splitNumbers = new int[9];
        int sum = 0;
        if (idNumber.matches(".*[a-z].*")) {
            Log.d(TAG, "idVerify: Letters had been found!!!");
            return false;
        }
        String[] splitText = idNumber.trim().split("(?!^)");
        if (splitText.length > 9) {
            Log.d(TAG, "idVerify: too long");
            return false;
        }
        if (splitText.length < 9) {
            int missingNumbers = 9 - splitText.length;
            for (int i = 0; i < missingNumbers; i++) {
                newStringSplit[i] = "0";
            }
            for (int i2 = 0; i2 < splitText.length; i2++) {
                newStringSplit[i2 + missingNumbers] = splitText[i2];
            }
        } else {
            System.arraycopy(splitText, 0, newStringSplit, 0, 9);
        }
        for (int i3 = 0; i3 < newStringSplit.length; i3++) {
            splitNumbers[i3] = Integer.parseInt(newStringSplit[i3]);
            if (i3 % 2 != 0) {
                splitNumbers[i3] = splitNumbers[i3] * 2;
            }
            if (splitNumbers[i3] > 9) {
                splitNumbers[i3] = (splitNumbers[i3] - 10) + 1;
            }
            sum += splitNumbers[i3];
        }
        return true;
    }

    public static int calSalary() {
        double pocketMoney = DesktopViewModel.allowance;
        int saturdayWorked = DesktopViewModel.saturdayWorked;
        int holidaysWorked = DesktopViewModel.holidaysWorked;
        Preferences preferences2 = preferences;
        if (preferences2 != null) {
            return (int) Math.round(((((1.0d - constantParams.getPension()) * (((double) (preferences2.getTotalPay() + (preferences.getSaturdayBonus() * ((float) (saturdayWorked + holidaysWorked))))) + calWellFareMonthlyBonus())) - ((double) preferences.getLivingExpensesDeduction())) - ((double) preferences.getHealthInsuranceDeduction())) - pocketMoney);
        }
        Log.d(TAG, "calAndShowSums: params are NULL");
        return 0;
    }

    static int calTotalVacationDays() {
        GregorianCalendar now = new GregorianCalendar();
        GregorianCalendar start = new GregorianCalendar();
        start.setTimeInMillis(dateOfHire);
        int seniorityInYears = ((((now.get(1) - start.get(1)) * 12) + now.get(2)) - start.get(2)) / 12;
        int vacationDaysDeserved = 0;
        for (String yearString : constantParams.getVacationDaysMap().keySet()) {
            if (!(seniorityInYears != Integer.parseInt(yearString) || constantParams.getVacationDaysMap() == null || constantParams.getVacationDaysMap().get(yearString) == null)) {
                vacationDaysDeserved = constantParams.getVacationDaysMap().get(yearString).intValue();
            }
        }
        return vacationDaysDeserved;
    }

    static int calSickDaysDeserved() {
        GregorianCalendar start = new GregorianCalendar();
        start.setTimeInMillis(dateOfHire);
        int startYear = start.get(1);
        int nowYear = DesktopActivity.chosenYear;
        int sickDaysDeserved = (int) (((double) ((((nowYear - startYear) * 12) - start.get(2)) + DesktopActivity.chosenMonth)) * constantParams.getSickDaysPerMonth());
        if (sickDaysDeserved > 90) {
            sickDaysDeserved = 90;
        }
        if (sickDaysDeserved < 0) {
            return 0;
        }
        return sickDaysDeserved;
    }

    public static String displayDate(Context context, long dateAsLong) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateAsLong);
        return DateFormat.getDateFormat(context).format(Long.valueOf(calendar.getTimeInMillis()));
    }
}
