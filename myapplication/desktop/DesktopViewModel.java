package com.example.yuvallehman.myapplication.desktop;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.server_side_functions.ServerDataSupplier;
import com.example.yuvallehman.myapplication.simple_java_classes.Event;
import com.example.yuvallehman.myapplication.simple_java_classes.PayItem;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class DesktopViewModel extends AndroidViewModel {
    private static final String PAYCHECK_BOOLEAN_ARRAY = "paycheckBooleanArray";
    private static final String TAG = "DesktopViewModel";
    public static double allowance;
    public static double companyPay;
    public static double convalescencePaid;
    private static int dayOfPay;
    public static int holidayPaid;
    public static int holidaysTaken;
    public static int holidaysWorked;
    public static double nutritionPaid;
    public static double othersPaid;
    public static int saturdayPaid;
    public static int saturdayWorked;
    public static int sickDaysTaken;
    public static int vacationDaysTaken;
    private GregorianCalendar calendar;
    private ServerDataSupplier sds = ServerDataSupplier.getInstance();
    private String[] titles;

    public DesktopViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "DesktopViewModel: constructor");
        this.titles = application.getResources().getStringArray(R.array.paycheck_titles);
        this.calendar = new GregorianCalendar();
        dayOfPay = this.sds.getPreferences().getDayOfPay();
    }

    public void sortEventsFromServer() {
        Log.d(TAG, "sortEventsFromServer: ");
        List<Event> eventList = this.sds.getEvents();
        saturdayWorked = 0;
        holidaysWorked = 0;
        allowance = 0.0d;
        companyPay = 0.0d;
        othersPaid = 0.0d;
        convalescencePaid = 0.0d;
        holidayPaid = 0;
        holidaysTaken = 0;
        saturdayPaid = 0;
        vacationDaysTaken = 0;
        sickDaysTaken = 0;
        nutritionPaid = 0.0d;
        for (Event event : eventList) {
            this.calendar.setTimeInMillis(event.getDate());
            String eventType = event.getEventType();
            if (DesktopActivity.chosenMonth == this.calendar.get(2) && DesktopActivity.chosenYear == this.calendar.get(1)) {
                if (eventType.equals(DesktopActivity.eventTypeListUnique[0])) {
                    saturdayWorked++;
                } else if (eventType.equals(DesktopActivity.eventTypeListUnique[2])) {
                    if (event.getMoneyType().equals(DesktopActivity.moneyTypeListUnique[0])) {
                        allowance += event.getMoneyGiven();
                    } else if (event.getMoneyType().equals(DesktopActivity.moneyTypeListUnique[3])) {
                        nutritionPaid += event.getMoneyGiven();
                    } else if (event.getMoneyType().equals(DesktopActivity.moneyTypeListUnique[2])) {
                        saturdayPaid = (int) (((double) saturdayPaid) + event.getMoneyGiven());
                    } else if (event.getMoneyType().equals(DesktopActivity.moneyTypeListUnique[1])) {
                        holidayPaid = (int) (((double) holidayPaid) + event.getMoneyGiven());
                    } else if (event.getMoneyType().equals(DesktopActivity.moneyTypeListUnique[5])) {
                        othersPaid += event.getMoneyGiven();
                    } else if (event.getMoneyType().equals(DesktopActivity.moneyTypeListUnique[4])) {
                        convalescencePaid += event.getMoneyGiven();
                    }
                } else if (eventType.equals(DesktopActivity.eventTypeListUnique[6])) {
                    companyPay = event.getMoneyGiven();
                } else if (eventType.equals(DesktopActivity.eventTypeListUnique[4])) {
                    holidaysTaken++;
                } else if (eventType.equals(DesktopActivity.eventTypeListUnique[3])) {
                    vacationDaysTaken++;
                } else if (eventType.equals(DesktopActivity.eventTypeListUnique[1])) {
                    holidaysWorked++;
                } else if (eventType.equals(DesktopActivity.eventTypeListUnique[5])) {
                    sickDaysTaken++;
                }
            }
        }
    }

    public List<PayItem> createPaycheckDisplayList() {
        Log.d(TAG, "createPaycheckDisplayList: start");
        List<PayItem> payItemList = new ArrayList<>();
        this.calendar.set(DesktopActivity.chosenYear, DesktopActivity.chosenMonth, 1);
        this.calendar.add(5, dayOfPay - this.calendar.get(7));
        int numberOfDayOfPayInTheSelectedMonth = this.calendar.getActualMaximum(8);
        int i = 0;
        while (true) {
            String[] strArr = this.titles;
            if (i < strArr.length) {
                PayItem payItem = new PayItem(strArr[i], "");
                int value = 0;
                switch (i) {
                    case 0:
                        value = (int) this.sds.getPreferences().getTotalPay();
                        break;
                    case 1:
                        value = (int) this.sds.getPreferences().getHealthInsuranceDeduction();
                        break;
                    case 2:
                        value = (int) this.sds.getPreferences().getRentDeduction();
                        break;
                    case 3:
                        value = (int) this.sds.getPreferences().getLivingExpensesDeduction();
                        break;
                    case 4:
                        value = calTotalDeduction();
                        break;
                    case 5:
                        value = calNetSalary();
                        break;
                    case 6:
                        if (this.sds.getPreferences().getPocketMoneyWeekOrMonth() != 101) {
                            value = (int) this.sds.getPreferences().getPocketMoney();
                            break;
                        } else {
                            value = (int) (this.sds.getPreferences().getPocketMoney() * ((float) numberOfDayOfPayInTheSelectedMonth));
                            break;
                        }
                    case 7:
                        value = (int) allowance;
                        break;
                    case 8:
                        value = saturdayWorked;
                        break;
                    case 9:
                        value = saturdayPaid;
                        break;
                    case 10:
                        value = holidaysWorked;
                        break;
                    case 11:
                        value = holidayPaid;
                        break;
                    case 12:
                        value = (int) companyPay;
                        break;
                    case 13:
                        if (this.sds.getPreferences().getNutritionPayWeekOrMonth() != 101) {
                            value = (int) this.sds.getPreferences().getNutritionPay();
                            break;
                        } else {
                            value = (int) (this.sds.getPreferences().getNutritionPay() * ((float) numberOfDayOfPayInTheSelectedMonth));
                            break;
                        }
                    case 14:
                        value = (int) nutritionPaid;
                        break;
                    case 15:
                        value = (int) othersPaid;
                        break;
                    case 16:
                        value = calConvalescenceTBP();
                        break;
                    case 17:
                        value = (int) convalescencePaid;
                        break;
                    case 18:
                        value = calNetSum();
                        break;
                    case 19:
                        value = vacationDaysTaken;
                        break;
                    case 20:
                        value = holidaysTaken;
                        break;
                    case 21:
                        value = sickDaysTaken;
                        break;
                }
                payItem.setValue(String.valueOf(value));
                payItemList.add(payItem);
                i++;
            } else {
                Log.d(TAG, "createPaycheckDisplayList: payItemList.size(): " + payItemList.size());
                return payItemList;
            }
        }
    }

    private int calTotalDeduction() {
        return (int) (this.sds.getPreferences().getHealthInsuranceDeduction() + this.sds.getPreferences().getLivingExpensesDeduction() + this.sds.getPreferences().getRentDeduction());
    }

    private int calConvalescenceTBP() {
        return 0;
    }

    private int calNetSum() {
        return 0;
    }

    private int calNetSalary() {
        return 0;
    }
}
