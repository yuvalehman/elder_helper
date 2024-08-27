package com.example.yuvallehman.myapplication.server_side_functions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import com.example.yuvallehman.myapplication.advanced_registration.PastFragment;
import com.example.yuvallehman.myapplication.advanced_registration.PayPrefFragment;
import com.example.yuvallehman.myapplication.advanced_registration.WorkerFragment;
import com.example.yuvallehman.myapplication.desktop.desktop.DesktopFragment;
import com.example.yuvallehman.myapplication.interfaces.MyActions;
import com.example.yuvallehman.myapplication.interfaces.MyResults;
import com.example.yuvallehman.myapplication.interfaces.MyServerCallback;
import com.example.yuvallehman.myapplication.interfaces.SignUpState;
import com.example.yuvallehman.myapplication.old.ElderFragment;
import com.example.yuvallehman.myapplication.simple_java_classes.ConstantParams;
import com.example.yuvallehman.myapplication.simple_java_classes.Event;
import com.example.yuvallehman.myapplication.simple_java_classes.PastRecord;
import com.example.yuvallehman.myapplication.simple_java_classes.Patient;
import com.example.yuvallehman.myapplication.simple_java_classes.Preferences;
import com.example.yuvallehman.myapplication.simple_java_classes.User;
import com.example.yuvallehman.myapplication.simple_java_classes.Worker;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ServerDataSupplier {
    private static final String CENTER = "center";
    private static final String COMPENSATION_PERCENT = "compensationPercent";
    private static final String CONST_CLASS = "ConstantParams";
    private static final String CREATED_AT = "createdAtLong";
    private static final String DAILY_MIN_PAY = "dailyMinPay";
    private static final String DATE = "date";
    public static final String DAY_OF_PAY = "dayOfPay";
    private static final String DAY_WORK_HOURS = "dayWorkHours";
    private static final String EVENTS_CLASS = "Events";
    public static final String EVENT_TYPE = "eventType";
    private static final String FIRST_NAME = "firstName";
    private static final String FOOD_LIMIT_PERCENT_FROM_MIN_SALAry = "foodLimitPercentFromMinSalary";
    private static final String HAIFA = "haifa";
    private static final String HEALTH_INSURANCE_LIMIT = "healthInsuranceLimit";
    private static final String HOLIDAY_DAYS_PER_YEAR = "holidaysPerYear";
    private static final String HOLIDAY_WORK_BONUS_PERCENT = "holidayBonusPercent";
    private static final String JERUSALEM = "jerusalem";
    private static final String LAST_NAME = "lastName";
    private static final String LIVING_EXPENSES_OTHERS_LIMIT = "livingExpensesOthersLimit";
    private static final String LIVING_EXPENSES_RENT_LIMIT_MAP = "livingExpensesRentLimitMap";
    private static final String MIN_SALARY = "minSalary";
    private static final String MONEY_GIVEN = "moneyGiven";
    private static final String MONEY_TYPE = "moneyType";
    private static final String MONTHLY_HOURS = "monthlyHours";
    private static final String NORTH = "north";
    private static final String PAST_RECORD_CLASS = "PastRecord";
    private static final String PATIENT_CLASS = "Patient";
    private static final String PENSION_PERCENT = "pensionPercent";
    private static final String PHONE = "phone";
    private static final String PREFERENCES_CLASS = "Preferences";
    private static final long SERVER_TOO_LONG_TIME = 30000;
    public static final String SHARED_PREF_NAME = "ElderHelperSharedPref";
    private static final String SICK_DAYS_LIMIT = "sickDaysLimit";
    private static final String SICK_DAYS_PER_MONTH = "sickDaysPerMonth";
    private static final String SIGN_UP_DATE = "signUpDate";
    private static final String SIGN_UP_STATE = "signUpState";
    private static final String SOUTH = "south";
    public static final String TAG = "ServerDataSupplier";
    private static final String TEL_AVIV = "telAviv";
    private static final String USERNAME = "username";
    private static final String VACATION_DAYS_MAP = "vacationDaysMap";
    private static final String WELL_FARE_DAILY_PAY = "wellFarePerDay";
    private static final String WELL_FARE_MAP = "wellFareMap";
    private static final String WORKERS_CLASS = "Workers";
    private static final ServerDataSupplier ourInstance = new ServerDataSupplier();
    public boolean advancedUserDataReady = false;
    public boolean constReady = false;
    private ConstantParams constantParams = ConstantParams.getInstance();
    private List<Event> events = new ArrayList();
    public boolean eventsReady = false;
    private boolean firstRunConst = true;
    private PastRecord pastRecord = PastRecord.getInstance();
    private boolean pastRecordReady = false;
    private Patient patient;
    private Preferences preferences = Preferences.getInstance();
    private boolean preferencesReady = false;
    private SharedPreferences sharedPreferences;
    private User user;
    private Worker worker;
    private boolean workerReady = false;

    public static ServerDataSupplier getInstance() {
        return ourInstance;
    }

    public void init(Context context) {
        Log.d(TAG, "init: start");
        Parse.initialize(new Parse.Configuration.Builder(context).applicationId("8Ifs7a7xmrFbaxV7cXGvdi65S2A3TQTojdcAJOEz").clientKey("S0RiRuOISJtm9Bz1TRBouHU5TWEg0EEvWpAVn2ZC").server("https://parseapi.back4app.com/").build());
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, 0);
    }

    private void cleanData() {
        Log.d(TAG, "cleanData: ");
        this.user = null;
        this.worker = null;
        this.events.clear();
        this.pastRecord.clear();
        this.preferences.clear();
        this.advancedUserDataReady = false;
        this.eventsReady = false;
        this.workerReady = false;
        this.pastRecordReady = false;
        this.preferencesReady = false;
    }

    public void logout(MyServerCallback listener) {
        Log.d(TAG, "logout: start");
        CountDownTimer timer = initTimer(listener);
        timer.start();
        ParseUser.logOutInBackground(new LogOutCallback(timer, listener) {
            private final /* synthetic */ CountDownTimer f$1;
            private final /* synthetic */ MyServerCallback f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void done(ParseException parseException) {
                ServerDataSupplier.lambda$logout$0(ServerDataSupplier.this, this.f$1, this.f$2, parseException);
            }
        });
    }

    public static /* synthetic */ void lambda$logout$0(ServerDataSupplier serverDataSupplier, CountDownTimer timer, MyServerCallback listener, ParseException e) {
        timer.cancel();
        if (e == null) {
            serverDataSupplier.cleanData();
            listener.serverDone(MyActions.LOGOUT, MyResults.SUCCESS, (Exception) null);
            Log.d(TAG, "logout: success");
            return;
        }
        listener.serverDone(MyActions.LOGOUT, MyResults.FAILED, e);
        Log.e(TAG, "logout: failed", e);
    }

    public void login(MyServerCallback listener, String username, String password) {
        Log.d(TAG, "login: start");
        CountDownTimer timer = initTimer(listener);
        timer.start();
        ParseUser.logInInBackground(username, password, new LogInCallback(timer, listener) {
            private final /* synthetic */ CountDownTimer f$1;
            private final /* synthetic */ MyServerCallback f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void done(ParseUser parseUser, ParseException parseException) {
                ServerDataSupplier.lambda$login$1(ServerDataSupplier.this, this.f$1, this.f$2, parseUser, parseException);
            }
        });
    }

    public static /* synthetic */ void lambda$login$1(ServerDataSupplier serverDataSupplier, CountDownTimer timer, MyServerCallback listener, ParseUser userFromServer, ParseException e) {
        timer.cancel();
        if (e == null) {
            serverDataSupplier.cleanData();
            serverDataSupplier.checkIfLoggedInAndConnect();
            listener.serverDone(MyActions.LOGIN, MyResults.SUCCESS, (Exception) null);
            Log.d(TAG, "login: DONE");
            return;
        }
        listener.serverDone(MyActions.LOGIN, MyResults.FAILED, e);
    }

    public void signUpUser(MyServerCallback callBack, String username, String pass1, String firstName, String lastName, String workerID) {
        CountDownTimer timer = initTimer(callBack);
        timer.start();
        GregorianCalendar calendar = new GregorianCalendar();
        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(username);
        parseUser.setPassword(pass1);
        parseUser.setEmail(username);
        parseUser.put(FIRST_NAME, firstName);
        parseUser.put(LAST_NAME, lastName);
        parseUser.put(SIGN_UP_DATE, Long.valueOf(calendar.getTimeInMillis()));
        parseUser.put(SIGN_UP_STATE, SignUpState.TRIAL);
        parseUser.put(WorkerFragment.WORKER_ID, workerID);
        parseUser.signUpInBackground(new SignUpCallback(timer, callBack) {
            private final /* synthetic */ CountDownTimer f$1;
            private final /* synthetic */ MyServerCallback f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void done(ParseException parseException) {
                ServerDataSupplier.lambda$signUpUser$2(ServerDataSupplier.this, this.f$1, this.f$2, parseException);
            }
        });
    }

    public static /* synthetic */ void lambda$signUpUser$2(ServerDataSupplier serverDataSupplier, CountDownTimer timer, MyServerCallback callBack, ParseException e) {
        timer.cancel();
        if (e == null) {
            Log.d(TAG, "signUpUser: SUCCESS");
            serverDataSupplier.checkIfLoggedInAndConnect();
            callBack.serverDone(MyActions.SIGN_UP, MyResults.SUCCESS, (Exception) null);
            return;
        }
        callBack.serverDone(MyActions.SIGN_UP, MyResults.FAILED, e);
    }

    public boolean checkIfLoggedInAndConnect() {
        String signUpState;
        ParseUser parseUser = ParseUser.getCurrentUser();
        boolean b = false;
        if (parseUser != null) {
            if (this.user == null) {
                Log.d(TAG, "checkIfLoggedInAndConnect: creating a user");
                String username = parseUser.getUsername();
                String firstName = parseUser.getString(FIRST_NAME);
                String lastName = parseUser.getString(LAST_NAME);
                String email = parseUser.getEmail();
                String phone = parseUser.getString(PHONE);
                String signUpState2 = parseUser.getString(SIGN_UP_STATE);
                long signUpDate = parseUser.getLong(SIGN_UP_DATE);
                long startLoggingDate = parseUser.getLong(DesktopFragment.START_LOGGING_DATE);
                if (startLoggingDate == 0) {
                    startLoggingDate = signUpDate;
                }
                long startLoggingDate2 = startLoggingDate;
                String workerID = parseUser.getString(WorkerFragment.WORKER_ID);
                GregorianCalendar now = new GregorianCalendar();
                GregorianCalendar then = new GregorianCalendar();
                then.setTimeInMillis(signUpDate);
                int daysSignedUp = ((((int) (now.getTimeInMillis() - then.getTimeInMillis())) / 1000) / 3600) / 24;
                if (daysSignedUp <= 90 || !SignUpState.TRIAL.equals(signUpState2)) {
                    signUpState = signUpState2;
                } else {
                    signUpState = SignUpState.BASIC;
                }
                ParseUser parseUser2 = parseUser;
                User user2 = r5;
                int i = daysSignedUp;
                GregorianCalendar gregorianCalendar = then;
                String workerID2 = workerID;
                long startLoggingDate3 = startLoggingDate2;
                long j = signUpDate;
                User user3 = new User(firstName, lastName, email, phone, username, signUpState, startLoggingDate2, signUpDate);
                this.user = user2;
                if (this.worker == null) {
                    this.worker = new Worker(workerID2);
                }
                if (this.worker.getDateOfHire() == 0) {
                    this.worker.setDateOfHire(startLoggingDate3);
                }
            }
            b = true;
        }
        Log.d(TAG, "checkIfLoggedInAndConnect: " + b);
        return b;
    }

    public void fetchPatientFromServer(MyServerCallback listener) {
        Log.d(TAG, "fetchPatientFromServer: ");
        CountDownTimer timer = initTimer(listener);
        timer.start();
        User user2 = this.user;
        if (user2 != null) {
            new ParseQuery(PATIENT_CLASS).whereEqualTo(USERNAME, user2.getUsername()).findInBackground(new FindCallback(timer) {
                private final /* synthetic */ CountDownTimer f$1;

                {
                    this.f$1 = r2;
                }

                public final void done(List list, ParseException parseException) {
                    ServerDataSupplier.lambda$fetchPatientFromServer$3(ServerDataSupplier.this, this.f$1, list, parseException);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$fetchPatientFromServer$3(ServerDataSupplier serverDataSupplier, CountDownTimer timer, List objects, ParseException e) {
        timer.cancel();
        if (e != null || objects.size() <= 0) {
            ServerDataSupplier serverDataSupplier2 = serverDataSupplier;
            List list = objects;
            return;
        }
        ParseObject parseObject = (ParseObject) objects.get(0);
        serverDataSupplier.patient = new Patient(parseObject.getString(ElderFragment.PATIENTS_FIRST), parseObject.getString(ElderFragment.PATIENTS_LAST), parseObject.getString(ElderFragment.PATIENTS_PHONE), parseObject.getString(ElderFragment.PATIENTS_ADDRESS), parseObject.getString(ElderFragment.PATIENTS_ID), (String) null);
    }

    private void fetchPastRecordsFromServer(MyServerCallback listener) {
        Log.d(TAG, "fetchPastRecordsFromServer: ");
        CountDownTimer timer = initTimer(listener);
        timer.start();
        User user2 = this.user;
        if (user2 != null) {
            new ParseQuery(PAST_RECORD_CLASS).whereEqualTo(USERNAME, user2.getUsername()).whereEqualTo(WorkerFragment.WORKER_ID, this.worker.getWorkerIdNumber()).findInBackground(new FindCallback(timer, listener) {
                private final /* synthetic */ CountDownTimer f$1;
                private final /* synthetic */ MyServerCallback f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                public final void done(List list, ParseException parseException) {
                    ServerDataSupplier.lambda$fetchPastRecordsFromServer$4(ServerDataSupplier.this, this.f$1, this.f$2, list, parseException);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$fetchPastRecordsFromServer$4(ServerDataSupplier serverDataSupplier, CountDownTimer timer, MyServerCallback listener, List objects, ParseException e) {
        timer.cancel();
        if (e == null && objects.size() > 0) {
            ParseObject parseObject = (ParseObject) objects.get(0);
            serverDataSupplier.pastRecord.setCreatedAt(parseObject.getLong(CREATED_AT));
            serverDataSupplier.pastRecord.setGotFromCompany(parseObject.getBoolean(PastFragment.GOT_FROM_COMPANY));
            serverDataSupplier.pastRecord.setTotalCompanyPay((float) parseObject.getDouble(PastFragment.TOTAL_COMPANY_PAY));
            serverDataSupplier.pastRecord.setWellFareDaysPaid(parseObject.getInt(PastFragment.WELL_FARE_DAYS));
            serverDataSupplier.pastRecord.setHolidays(parseObject.getInt(PastFragment.HOLIDAYS));
            serverDataSupplier.pastRecord.setSickDays(parseObject.getInt(PastFragment.SICK_DAYS));
            serverDataSupplier.pastRecord.setVacationDays(parseObject.getInt(PastFragment.VACATION_DAYS));
            Log.d(TAG, "fetchPastRecordsFromServer: SUCCESS");
            serverDataSupplier.pastRecordReady = true;
            listener.serverDone(MyActions.FETCH_PAST, MyResults.SUCCESS, (Exception) null);
        }
    }

    public void fetchEvensFromServer(MyServerCallback listener) {
        Log.d(TAG, "fetchEvensFromServer: start");
        CountDownTimer timer = initTimer(listener);
        timer.start();
        String username = this.user.getUsername();
        String workerID = this.worker.getWorkerIdNumber();
        new ParseQuery(EVENTS_CLASS).whereEqualTo(USERNAME, username).whereEqualTo(WorkerFragment.WORKER_ID, workerID).findInBackground(new FindCallback(timer, username, workerID, listener) {
            private final /* synthetic */ CountDownTimer f$1;
            private final /* synthetic */ String f$2;
            private final /* synthetic */ String f$3;
            private final /* synthetic */ MyServerCallback f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            public final void done(List list, ParseException parseException) {
                ServerDataSupplier.lambda$fetchEvensFromServer$5(ServerDataSupplier.this, this.f$1, this.f$2, this.f$3, this.f$4, list, parseException);
            }
        });
    }

    public static /* synthetic */ void lambda$fetchEvensFromServer$5(ServerDataSupplier serverDataSupplier, CountDownTimer timer, String username, String workerID, MyServerCallback listener, List parseEvents, ParseException e) {
        ServerDataSupplier serverDataSupplier2 = serverDataSupplier;
        MyServerCallback myServerCallback = listener;
        ParseException parseException = e;
        timer.cancel();
        List<Event> tempEventsList = new ArrayList<>();
        if (parseException == null) {
            if (parseEvents.size() > 0) {
                Iterator it = parseEvents.iterator();
                while (it.hasNext()) {
                    ParseObject parseEvent = (ParseObject) it.next();
                    double moneyGiven = parseEvent.getDouble(MONEY_GIVEN);
                    String eventType = parseEvent.getString(EVENT_TYPE);
                    long date = parseEvent.getLong(DATE);
                    long createdAtDate = parseEvent.getLong(CREATED_AT);
                    Event event = r8;
                    Event event2 = new Event(username, workerID, eventType, moneyGiven, date, createdAtDate, parseEvent.getString(MONEY_TYPE), serverDataSupplier2.constantParams.getCreatedAt(), serverDataSupplier2.preferences.getCreatedAt());
                    tempEventsList.add(event);
                }
                serverDataSupplier2.events = tempEventsList;
                Log.d(TAG, "fetchEvensFromServer: SUCCESS");
                myServerCallback.serverDone(MyActions.FETCH_EVENTS, MyResults.SUCCESS, (Exception) null);
            } else {
                Log.d(TAG, "fetchEvensFromServer: SUCCESS but (parseEvents.size == 0)");
                myServerCallback.serverDone(MyActions.FETCH_EVENTS, MyResults.FAILED, (Exception) null);
            }
            serverDataSupplier2.eventsReady = true;
            return;
        }
        Log.e(TAG, "fetchEvensFromServer: else 2" + e.getMessage(), parseException);
        myServerCallback.serverDone(MyActions.FETCH_EVENTS, MyResults.FAILED, parseException);
    }

    private void fetchWorkerFromServer(MyServerCallback listener) {
        Log.d(TAG, "fetchWorkerFromServer: start");
        CountDownTimer timer = initTimer(listener);
        timer.start();
        if (this.user != null) {
            new ParseQuery(WORKERS_CLASS).whereEqualTo(USERNAME, this.user.getUsername()).whereEqualTo(WorkerFragment.WORKER_ID, this.worker.getWorkerIdNumber()).findInBackground(new FindCallback(timer, listener) {
                private final /* synthetic */ CountDownTimer f$1;
                private final /* synthetic */ MyServerCallback f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                public final void done(List list, ParseException parseException) {
                    ServerDataSupplier.lambda$fetchWorkerFromServer$6(ServerDataSupplier.this, this.f$1, this.f$2, list, parseException);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$fetchWorkerFromServer$6(ServerDataSupplier serverDataSupplier, CountDownTimer timer, MyServerCallback listener, List objects, ParseException e) {
        timer.cancel();
        if (e == null && objects.size() > 0) {
            String workerFirst = ((ParseObject) objects.get(0)).getString(WorkerFragment.WORKER_FIRST_NAME);
            String workerLast = ((ParseObject) objects.get(0)).getString(WorkerFragment.WORKER_LAST_NAME);
            String workerEmail = ((ParseObject) objects.get(0)).getString(WorkerFragment.WORKER_EMAIL);
            String workerCountry = ((ParseObject) objects.get(0)).getString(WorkerFragment.WORKER_COUNTRY);
            long dateOfHire = ((ParseObject) objects.get(0)).getLong(WorkerFragment.DATE_OF_HIRE);
            serverDataSupplier.worker.setWorkerLastName(workerLast);
            serverDataSupplier.worker.setWorkerFirstName(workerFirst);
            serverDataSupplier.worker.setWorkerEmail(workerEmail);
            serverDataSupplier.worker.setCountry(workerCountry);
            serverDataSupplier.worker.setDateOfHire(dateOfHire);
            serverDataSupplier.workerReady = true;
            Log.d(TAG, "fetchWorkerFromServer: SUCCESS");
            listener.serverDone(MyActions.FETCH_WORKER, MyResults.SUCCESS, (Exception) null);
        } else if (e != null) {
            Log.e(TAG, "fetchWorkerFromServer: " + e.getMessage(), e);
            listener.serverDone(MyActions.FETCH_WORKER, MyResults.FAILED, e);
        } else {
            listener.serverDone(MyActions.FETCH_WORKER, MyResults.FAILED, (Exception) null);
            Log.d(TAG, "fetchWorkerFromServer: no Workers.");
        }
    }

    private void fetchPreferencesFromServer(MyServerCallback listener) {
        Log.d(TAG, "fetchPreferencesFromServer: start");
        CountDownTimer timer = initTimer(listener);
        timer.start();
        if (this.user != null) {
            new ParseQuery(PREFERENCES_CLASS).whereEqualTo(USERNAME, this.user.getUsername()).whereEqualTo(WorkerFragment.WORKER_ID, this.worker.getWorkerIdNumber()).findInBackground(new FindCallback(timer, listener) {
                private final /* synthetic */ CountDownTimer f$1;
                private final /* synthetic */ MyServerCallback f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                public final void done(List list, ParseException parseException) {
                    ServerDataSupplier.lambda$fetchPreferencesFromServer$7(ServerDataSupplier.this, this.f$1, this.f$2, list, parseException);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$fetchPreferencesFromServer$7(ServerDataSupplier serverDataSupplier, CountDownTimer timer, MyServerCallback listener, List objects, ParseException e) {
        timer.cancel();
        if (e == null && objects.size() > 0) {
            ParseObject params = (ParseObject) objects.get(0);
            Long createdAt = Long.valueOf(params.getLong(CREATED_AT));
            for (int i = 1; i < objects.size(); i++) {
                ParseObject params2 = (ParseObject) objects.get(i);
                Long createdAt2 = Long.valueOf(params2.getLong(CREATED_AT));
                if (createdAt2.longValue() > createdAt.longValue()) {
                    createdAt = createdAt2;
                    params = params2;
                }
            }
            serverDataSupplier.preferences.setCreatedAt(params.getLong(CREATED_AT));
            serverDataSupplier.preferences.setSaturdayBonus((float) params.getDouble(PayPrefFragment.SATURDAY_BONUS));
            serverDataSupplier.preferences.setTotalPay((float) params.getDouble(PayPrefFragment.TOTAL_PAY));
            serverDataSupplier.preferences.setRentDeduction((float) params.getDouble("rentDeduction"));
            serverDataSupplier.preferences.setLivingExpensesDeduction((float) params.getDouble("livingExpensesDeduction"));
            serverDataSupplier.preferences.setPocketMoney((float) params.getDouble(PayPrefFragment.POCKET_MONEY));
            serverDataSupplier.preferences.setHealthInsuranceDeduction((float) params.getDouble("healthInsuranceDeduction"));
            serverDataSupplier.preferences.setNutritionPay((float) params.getDouble(PayPrefFragment.NUTRITION_PAY));
            serverDataSupplier.preferences.setPocketMoneyWeekOrMonth(params.getInt(PayPrefFragment.POCKET_MONEY_WEEK_OR_MONTH));
            serverDataSupplier.preferences.setNutritionPayWeekOrMonth(params.getInt(PayPrefFragment.NUTRITION_WEEK_OR_MONTH));
            serverDataSupplier.preferences.setWork7days(params.getBoolean(PayPrefFragment.WORK_7_DAYS));
            serverDataSupplier.preferences.setPocketMoneyPartOfPay(params.getBoolean(PayPrefFragment.POCKET_MONEY_PART_OF_PAY));
            serverDataSupplier.preferences.setGettingFromCompany(params.getBoolean("wellFareFromCompany"));
            serverDataSupplier.preferences.setElderHouse(params.getBoolean("isElderHouse"));
            serverDataSupplier.preferences.setDayOfPay(params.getInt(DAY_OF_PAY));
            serverDataSupplier.preferencesReady = true;
            Log.d(TAG, "fetchPreferencesFromServer: SUCCESS");
            listener.serverDone(MyActions.FETCH_PREF, MyResults.SUCCESS, (Exception) null);
        } else if (e != null) {
            listener.serverDone(MyActions.FETCH_PREF, MyResults.FAILED, e);
            Log.e(TAG, "fetchPreferencesFromServer: " + e.getMessage(), e);
        } else {
            listener.serverDone(MyActions.FETCH_PREF, MyResults.FAILED, (Exception) null);
            Log.d(TAG, "fetchPreferencesFromServer: no preferences...");
        }
    }

    public void fetchConstParams(MyServerCallback activityCallback) {
        Log.d(TAG, "fetchConstParams: start");
        CountDownTimer timer = initTimer(activityCallback);
        timer.start();
        if (!this.firstRunConst) {
            Log.d(TAG, "fetchConstParams: !firstRunConst");
            activityCallback.serverDone(MyActions.FETCH_CONST, MyResults.SUCCESS, (Exception) null);
            timer.cancel();
            return;
        }
        new ParseQuery(CONST_CLASS).findInBackground(new FindCallback(timer, activityCallback) {
            private final /* synthetic */ CountDownTimer f$1;
            private final /* synthetic */ MyServerCallback f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void done(List list, ParseException parseException) {
                ServerDataSupplier.lambda$fetchConstParams$8(ServerDataSupplier.this, this.f$1, this.f$2, list, parseException);
            }
        });
    }

    public static /* synthetic */ void lambda$fetchConstParams$8(ServerDataSupplier serverDataSupplier, CountDownTimer timer, MyServerCallback activityCallback, List objects, ParseException e) {
        timer.cancel();
        if (e == null && objects.size() > 0) {
            ParseObject object = (ParseObject) objects.get(0);
            Long createdAt = Long.valueOf(object.getLong(CREATED_AT));
            for (int i = 1; i < objects.size(); i++) {
                Long createdAt2 = Long.valueOf(((ParseObject) objects.get(i)).getLong(CREATED_AT));
                if (createdAt2.longValue() > createdAt.longValue()) {
                    createdAt = createdAt2;
                }
            }
            serverDataSupplier.constantParams.setCreatedAt(createdAt.longValue());
            serverDataSupplier.constantParams.setMinimumSalary(object.getInt(MIN_SALARY));
            serverDataSupplier.constantParams.setFullJobMonthlyHours(object.getDouble(MONTHLY_HOURS));
            serverDataSupplier.constantParams.setWellFareBonusDailyFee(object.getDouble(WELL_FARE_DAILY_PAY));
            serverDataSupplier.constantParams.setSickDaysPerMonth(object.getDouble(SICK_DAYS_PER_MONTH));
            serverDataSupplier.constantParams.setYearlyHolidays(object.getInt(HOLIDAY_DAYS_PER_YEAR));
            serverDataSupplier.constantParams.setSaturdayOrHolidayBonus(object.getDouble(HOLIDAY_WORK_BONUS_PERCENT));
            serverDataSupplier.constantParams.setPension(object.getDouble(PENSION_PERCENT));
            serverDataSupplier.constantParams.setCompensation(object.getDouble(COMPENSATION_PERCENT));
            serverDataSupplier.constantParams.setDailyWorkingHours(object.getInt(DAY_WORK_HOURS));
            serverDataSupplier.constantParams.setHealthInsuranceLimit(object.getDouble(HEALTH_INSURANCE_LIMIT));
            serverDataSupplier.constantParams.setLivingExpensesOthersLimit(object.getDouble(LIVING_EXPENSES_OTHERS_LIMIT));
            serverDataSupplier.constantParams.setSickDaysLimit(object.getInt(SICK_DAYS_LIMIT));
            serverDataSupplier.constantParams.setFoodLimitPercentFromMinSalary(object.getDouble(FOOD_LIMIT_PERCENT_FROM_MIN_SALAry));
            serverDataSupplier.constantParams.setDailyMinPay((double) object.getInt(DAILY_MIN_PAY));
            serverDataSupplier.constantParams.setVacationDaysMap(object.getMap(VACATION_DAYS_MAP));
            serverDataSupplier.constantParams.setWellFareBonusMap(object.getMap(WELL_FARE_MAP));
            serverDataSupplier.constantParams.setLivingExpensesRentLimitMap(object.getMap(LIVING_EXPENSES_RENT_LIMIT_MAP));
            Log.d(TAG, "fetchConstParams: DONE");
            serverDataSupplier.constReady = true;
            activityCallback.serverDone(MyActions.FETCH_CONST, MyResults.SUCCESS, (Exception) null);
            serverDataSupplier.firstRunConst = false;
        } else if (e != null) {
            activityCallback.serverDone(MyActions.FETCH_CONST, MyResults.FAILED, e);
            Log.e(TAG, "fetchConstParams: " + e.getMessage(), e);
        } else {
            Log.d(TAG, "fetchConstParams: const!=1");
            activityCallback.serverDone(MyActions.FETCH_CONST, MyResults.FAILED, (Exception) null);
        }
    }

    public void fetchAdvanceUserData(MyServerCallback listener) {
        Log.d(TAG, "fetchAdvanceUserData: start");
        MyServerCallback myServerCallback = new MyServerCallback(listener) {
            private final /* synthetic */ MyServerCallback f$1;

            {
                this.f$1 = r2;
            }

            public final void serverDone(MyActions myActions, MyResults myResults, Exception exc) {
                ServerDataSupplier.lambda$fetchAdvanceUserData$9(ServerDataSupplier.this, this.f$1, myActions, myResults, exc);
            }
        };
        fetchWorkerFromServer(myServerCallback);
        fetchPreferencesFromServer(myServerCallback);
        fetchPastRecordsFromServer(myServerCallback);
    }

    public static /* synthetic */ void lambda$fetchAdvanceUserData$9(ServerDataSupplier serverDataSupplier, MyServerCallback listener, MyActions innerAction, MyResults results, Exception e) {
        Log.d(TAG, "fetchAdvanceUserData: callback");
        if (serverDataSupplier.preferencesReady && serverDataSupplier.workerReady && serverDataSupplier.pastRecordReady) {
            Log.d(TAG, "fetchAdvanceUserData: ALL READY");
            serverDataSupplier.advancedUserDataReady = true;
            listener.serverDone(MyActions.FETCH_ADVANCED_USER_DATA, MyResults.SUCCESS, (Exception) null);
        }
    }

    public User getUser() {
        return this.user;
    }

    public Worker getWorker() {
        return this.worker;
    }

    public Preferences getPreferences() {
        return this.preferences;
    }

    public PastRecord getPastRecord() {
        return this.pastRecord;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public ConstantParams getConstantParams() {
        return this.constantParams;
    }

    public List<Event> getEvents() {
        return this.events;
    }

    public void tempSaveConstParams() {
        Log.d(TAG, "tempSaveConstParams: start");
        Map<String, Integer> wellMap = new HashMap<>();
        wellMap.put("1", 5);
        wellMap.put("2", 6);
        wellMap.put("3", 7);
        wellMap.put("4", 8);
        wellMap.put("5", 9);
        Map<String, Integer> vacMap = new HashMap<>();
        vacMap.put("0", 14);
        vacMap.put("1", 14);
        vacMap.put("2", 15);
        vacMap.put("3", 16);
        vacMap.put("4", 17);
        vacMap.put("5", 18);
        Map<String, Double> rentLimitMap = new HashMap<>();
        rentLimitMap.put(JERUSALEM, Double.valueOf(405.05d));
        rentLimitMap.put(TEL_AVIV, Double.valueOf(460.58d));
        rentLimitMap.put(HAIFA, Double.valueOf(307.08d));
        rentLimitMap.put(CENTER, Double.valueOf(307.08d));
        rentLimitMap.put(SOUTH, Double.valueOf(272.98d));
        rentLimitMap.put(NORTH, Double.valueOf(251.1d));
        ParseObject constParams = new ParseObject(CONST_CLASS);
        constParams.put(CREATED_AT, Long.valueOf(new GregorianCalendar().getTimeInMillis()));
        constParams.put(MIN_SALARY, 5300);
        constParams.put(MONTHLY_HOURS, 182);
        constParams.put(WELL_FARE_DAILY_PAY, 378);
        constParams.put(SICK_DAYS_PER_MONTH, Double.valueOf(1.5d));
        constParams.put(SICK_DAYS_LIMIT, 90);
        constParams.put(HOLIDAY_DAYS_PER_YEAR, 9);
        constParams.put(HOLIDAY_WORK_BONUS_PERCENT, Double.valueOf(1.5d));
        constParams.put(PENSION_PERCENT, Double.valueOf(0.06d));
        constParams.put(COMPENSATION_PERCENT, Double.valueOf(0.0833d));
        constParams.put(DAY_WORK_HOURS, 8);
        constParams.put(LIVING_EXPENSES_OTHERS_LIMIT, Double.valueOf(79.04d));
        constParams.put(FOOD_LIMIT_PERCENT_FROM_MIN_SALAry, Double.valueOf(0.1d));
        constParams.put(HEALTH_INSURANCE_LIMIT, Double.valueOf(134.6d));
        constParams.put(DAY_WORK_HOURS, 8);
        constParams.put(WELL_FARE_MAP, wellMap);
        constParams.put(VACATION_DAYS_MAP, vacMap);
        constParams.put(LIVING_EXPENSES_RENT_LIMIT_MAP, rentLimitMap);
        constParams.put(DAILY_MIN_PAY, 212);
        constParams.saveInBackground($$Lambda$ServerDataSupplier$54Ou43A3uri_xI5wl4_oMZfcgA.INSTANCE);
    }

    static /* synthetic */ void lambda$tempSaveConstParams$10(ParseException e) {
        if (e == null) {
            Log.d(TAG, "tempSaveConstParams: success");
            return;
        }
        Log.e(TAG, "tempSaveConstParams: " + e.getMessage(), e);
    }

    public void editOrSaveSpecificEventToServer(MyServerCallback listener, Event oldEvent, Event newEvent) {
        Log.d(TAG, "editOrSaveSpecificEventToServer: start");
        new ParseQuery(EVENTS_CLASS).whereEqualTo(USERNAME, this.user.getUsername()).whereEqualTo(CREATED_AT, Long.valueOf(oldEvent.getCreatedAt())).findInBackground(new FindCallback(newEvent, oldEvent, listener) {
            private final /* synthetic */ Event f$1;
            private final /* synthetic */ Event f$2;
            private final /* synthetic */ MyServerCallback f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final void done(List list, ParseException parseException) {
                ServerDataSupplier.lambda$editOrSaveSpecificEventToServer$12(ServerDataSupplier.this, this.f$1, this.f$2, this.f$3, list, parseException);
            }
        });
    }

    public static /* synthetic */ void lambda$editOrSaveSpecificEventToServer$12(ServerDataSupplier serverDataSupplier, Event newEvent, Event oldEvent, MyServerCallback listener, List objects, ParseException e) {
        ParseObject parseEvent;
        Log.d(TAG, "editOrSaveSpecificEventToServer: backFromServer FIND");
        if (e == null) {
            Log.d(TAG, "editOrSaveSpecificEventToServer: if");
            if (objects.size() == 1) {
                parseEvent = (ParseObject) objects.get(0);
            } else {
                parseEvent = new ParseObject(EVENTS_CLASS);
            }
            parseEvent.put(USERNAME, serverDataSupplier.user.getUsername());
            parseEvent.put(WorkerFragment.WORKER_ID, serverDataSupplier.worker.getWorkerIdNumber());
            parseEvent.put(CREATED_AT, Long.valueOf(newEvent.getCreatedAt()));
            parseEvent.put(DATE, Long.valueOf(newEvent.getDate()));
            parseEvent.put(EVENT_TYPE, newEvent.getEventType());
            parseEvent.put(MONEY_GIVEN, Double.valueOf(newEvent.getMoneyGiven()));
            parseEvent.put(MONEY_TYPE, newEvent.getMoneyType());
            parseEvent.saveInBackground(new SaveCallback(objects, oldEvent, newEvent, listener) {
                private final /* synthetic */ List f$1;
                private final /* synthetic */ Event f$2;
                private final /* synthetic */ Event f$3;
                private final /* synthetic */ MyServerCallback f$4;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                    this.f$4 = r5;
                }

                public final void done(ParseException parseException) {
                    ServerDataSupplier.lambda$null$11(ServerDataSupplier.this, this.f$1, this.f$2, this.f$3, this.f$4, parseException);
                }
            });
            return;
        }
        Log.d(TAG, "editOrSaveSpecificEventToServer: else2");
        listener.serverDone(MyActions.EDIT_AND_SAVE_EVENT, MyResults.FAILED, e);
    }

    public static /* synthetic */ void lambda$null$11(ServerDataSupplier serverDataSupplier, List objects, Event oldEvent, Event newEvent, MyServerCallback listener, ParseException e1) {
        Log.d(TAG, "editOrSaveSpecificEventToServer: backFromServer SAVE");
        if (e1 == null) {
            Log.d(TAG, "editOrSaveSpecificEventToServer: if2");
            if (objects.size() == 1) {
                serverDataSupplier.events.remove(oldEvent);
            }
            serverDataSupplier.events.add(newEvent);
            Log.d(TAG, "editOrSaveSpecificEventToServer: SUCCESS");
            listener.serverDone(MyActions.EDIT_AND_SAVE_EVENT, MyResults.SUCCESS, (Exception) null);
            return;
        }
        Log.d(TAG, "editOrSaveSpecificEventToServer: else1");
        listener.serverDone(MyActions.EDIT_AND_SAVE_EVENT, MyResults.FAILED, e1);
    }

    public void saveEventToServer(MyServerCallback listener, Event event) {
        Log.d(TAG, "saveEvent: start");
        CountDownTimer timer = initTimer(listener);
        timer.start();
        ParseObject parseEvent = new ParseObject(EVENTS_CLASS);
        parseEvent.put(USERNAME, this.user.getUsername());
        parseEvent.put(WorkerFragment.WORKER_ID, this.worker.getWorkerIdNumber());
        parseEvent.put(CREATED_AT, Long.valueOf(event.getCreatedAt()));
        parseEvent.put(DATE, Long.valueOf(event.getDate()));
        parseEvent.put(EVENT_TYPE, event.getEventType());
        parseEvent.put(MONEY_GIVEN, Double.valueOf(event.getMoneyGiven()));
        parseEvent.put(MONEY_TYPE, event.getMoneyType());
        parseEvent.saveInBackground(new SaveCallback(timer, event, listener) {
            private final /* synthetic */ CountDownTimer f$1;
            private final /* synthetic */ Event f$2;
            private final /* synthetic */ MyServerCallback f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final void done(ParseException parseException) {
                ServerDataSupplier.lambda$saveEventToServer$13(ServerDataSupplier.this, this.f$1, this.f$2, this.f$3, parseException);
            }
        });
    }

    public static /* synthetic */ void lambda$saveEventToServer$13(ServerDataSupplier serverDataSupplier, CountDownTimer timer, Event event, MyServerCallback listener, ParseException e) {
        timer.cancel();
        if (e == null) {
            serverDataSupplier.events.add(event);
            listener.serverDone(MyActions.SAVE_EVENT, MyResults.SUCCESS, (Exception) null);
            Log.d(TAG, "saveEventToServer: SUCCESS");
            return;
        }
        listener.serverDone(MyActions.SAVE_EVENT, MyResults.FAILED, e);
        Log.e(TAG, "saveEventToServer: " + e.getMessage(), e);
    }

    public void deleteEventFromServer(Event event, MyServerCallback listener) {
        Log.d(TAG, "deleteEventFromServer: start");
        CountDownTimer timer = initTimer(listener);
        timer.start();
        new ParseQuery(EVENTS_CLASS).whereEqualTo(USERNAME, event.getUsername()).whereEqualTo(WorkerFragment.WORKER_ID, event.getWorkerId()).whereEqualTo(CREATED_AT, Long.valueOf(event.getCreatedAt())).findInBackground(new FindCallback(timer, event, listener) {
            private final /* synthetic */ CountDownTimer f$1;
            private final /* synthetic */ Event f$2;
            private final /* synthetic */ MyServerCallback f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final void done(List list, ParseException parseException) {
                ServerDataSupplier.lambda$deleteEventFromServer$15(ServerDataSupplier.this, this.f$1, this.f$2, this.f$3, list, parseException);
            }
        });
    }

    public static /* synthetic */ void lambda$deleteEventFromServer$15(ServerDataSupplier serverDataSupplier, CountDownTimer timer, Event event, MyServerCallback listener, List objects, ParseException e) {
        timer.cancel();
        if (e == null && objects.size() == 1) {
            ((ParseObject) objects.get(0)).deleteInBackground(new DeleteCallback(event, listener) {
                private final /* synthetic */ Event f$1;
                private final /* synthetic */ MyServerCallback f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                public final void done(ParseException parseException) {
                    ServerDataSupplier.lambda$null$14(ServerDataSupplier.this, this.f$1, this.f$2, parseException);
                }
            });
        } else if (e != null) {
            listener.serverDone(MyActions.DELETE_EVENT, MyResults.FAILED, e);
            Log.e(TAG, "deleteEventFromServer: " + e.getMessage(), e);
        } else {
            listener.serverDone(MyActions.DELETE_EVENT, MyResults.FAILED, (Exception) null);
            Log.d(TAG, "deleteEventFromServer: NO EVENTS OR MORE THEN ONE...");
        }
    }

    public static /* synthetic */ void lambda$null$14(ServerDataSupplier serverDataSupplier, Event event, MyServerCallback listener, ParseException e1) {
        if (e1 == null) {
            Log.d(TAG, "deleteEventFromServer: DELETE SUCCESS");
            serverDataSupplier.events.remove(event);
            listener.serverDone(MyActions.DELETE_EVENT, MyResults.SUCCESS, (Exception) null);
            return;
        }
        listener.serverDone(MyActions.DELETE_EVENT, MyResults.FAILED, e1);
        Log.e(TAG, "deleteEventFromServer: " + e1.getMessage(), e1);
    }

    private CountDownTimer initTimer(MyServerCallback timerCallBack) {
        Log.d(TAG, "CountDownTimer timer started");
        final MyServerCallback myServerCallback = timerCallBack;
        return new CountDownTimer(SERVER_TOO_LONG_TIME, SERVER_TOO_LONG_TIME) {
            public void onTick(long millisUntilFinished) {
                Log.d(ServerDataSupplier.TAG, "onTick: ");
            }

            public void onFinish() {
                Log.d(ServerDataSupplier.TAG, "onFinish: Taking too long!");
                Exception tooLongException = new Exception("Too Long Server Interaction!");
                MyServerCallback myServerCallback = myServerCallback;
                if (myServerCallback != null) {
                    myServerCallback.serverDone(MyActions.TIMER_FINISHED, MyResults.FAILED, tooLongException);
                }
            }
        };
    }

    public void savePayReg(MyServerCallback payRegActivity, Bundle bundle) {
        Bundle bundle2 = bundle;
        Log.d(TAG, "savePayReg: ");
        CountDownTimer timer = initTimer(payRegActivity);
        timer.start();
        String username = this.user.getUsername();
        String workerID = this.worker.getWorkerIdNumber();
        long createdAt = new GregorianCalendar().getTimeInMillis();
        int pocketMoneyWeekOrMonth = bundle2.getInt(PayPrefFragment.POCKET_MONEY_WEEK_OR_MONTH);
        int nutritionWeekOrMonth = bundle2.getInt(PayPrefFragment.NUTRITION_WEEK_OR_MONTH);
        boolean isWork7days = bundle2.getBoolean(PayPrefFragment.WORK_7_DAYS);
        boolean isPocketMoneyPartOfPay = bundle2.getBoolean(PayPrefFragment.POCKET_MONEY_PART_OF_PAY);
        float saturdayBonus = bundle2.getFloat(PayPrefFragment.SATURDAY_BONUS);
        float totalPay = bundle2.getFloat(PayPrefFragment.TOTAL_PAY);
        float pocketMoney = bundle2.getFloat(PayPrefFragment.POCKET_MONEY);
        float nutritionPay = bundle2.getFloat(PayPrefFragment.NUTRITION_PAY);
        int dayOfPay = bundle2.getInt(DAY_OF_PAY);
        boolean isElderHouse = bundle2.getBoolean("isElderHouse");
        boolean isWellFareFromCompany = bundle2.getBoolean("wellFareFromCompany");
        float rentDeduction = bundle2.getFloat("rentDeduction");
        float livingExpensesDeduction = bundle2.getFloat("livingExpensesDeduction");
        float healthInsuranceDeduction = bundle2.getFloat("healthInsuranceDeduction");
        ParseObject parsePreferences = new ParseObject(PREFERENCES_CLASS);
        parsePreferences.put(USERNAME, username);
        parsePreferences.put(WorkerFragment.WORKER_ID, workerID);
        parsePreferences.put(CREATED_AT, Long.valueOf(createdAt));
        parsePreferences.put(PayPrefFragment.POCKET_MONEY_WEEK_OR_MONTH, Integer.valueOf(pocketMoneyWeekOrMonth));
        parsePreferences.put(PayPrefFragment.NUTRITION_WEEK_OR_MONTH, Integer.valueOf(nutritionWeekOrMonth));
        parsePreferences.put(PayPrefFragment.WORK_7_DAYS, Boolean.valueOf(isWork7days));
        parsePreferences.put(PayPrefFragment.POCKET_MONEY_PART_OF_PAY, Boolean.valueOf(isPocketMoneyPartOfPay));
        parsePreferences.put(PayPrefFragment.SATURDAY_BONUS, Float.valueOf(saturdayBonus));
        parsePreferences.put(PayPrefFragment.TOTAL_PAY, Float.valueOf(totalPay));
        parsePreferences.put(PayPrefFragment.POCKET_MONEY, Float.valueOf(pocketMoney));
        parsePreferences.put(PayPrefFragment.NUTRITION_PAY, Float.valueOf(nutritionPay));
        parsePreferences.put("isElderHouse", Boolean.valueOf(isElderHouse));
        parsePreferences.put("wellFareFromCompany", Boolean.valueOf(isWellFareFromCompany));
        parsePreferences.put("rentDeduction", Float.valueOf(rentDeduction));
        parsePreferences.put("livingExpensesDeduction", Float.valueOf(livingExpensesDeduction));
        parsePreferences.put("healthInsuranceDeduction", Float.valueOf(healthInsuranceDeduction));
        parsePreferences.put(DAY_OF_PAY, Integer.valueOf(dayOfPay));
        int vacationDays = bundle2.getInt(PastFragment.VACATION_DAYS);
        int sickDays = bundle2.getInt(PastFragment.SICK_DAYS);
        int holidays = bundle2.getInt(PastFragment.HOLIDAYS);
        int wellFareDaysPaid = bundle2.getInt(PastFragment.WELL_FARE_DAYS);
        float totalCompanyPay = bundle2.getFloat(PastFragment.TOTAL_COMPANY_PAY);
        boolean wasUsingCompany = bundle2.getBoolean(PastFragment.WAS_USING_COMPANY);
        ParseObject parsePastRecord = new ParseObject(PAST_RECORD_CLASS);
        parsePastRecord.put(USERNAME, username);
        parsePastRecord.put(WorkerFragment.WORKER_ID, workerID);
        parsePastRecord.put(CREATED_AT, Long.valueOf(createdAt));
        parsePastRecord.put(CREATED_AT, Long.valueOf(new GregorianCalendar().getTimeInMillis()));
        parsePastRecord.put(PastFragment.VACATION_DAYS, Integer.valueOf(vacationDays));
        parsePastRecord.put(PastFragment.SICK_DAYS, Integer.valueOf(sickDays));
        parsePastRecord.put(PastFragment.HOLIDAYS, Integer.valueOf(holidays));
        parsePastRecord.put(PastFragment.WELL_FARE_DAYS, Integer.valueOf(wellFareDaysPaid));
        parsePastRecord.put(PastFragment.TOTAL_COMPANY_PAY, Float.valueOf(totalCompanyPay));
        parsePastRecord.put(PastFragment.WAS_USING_COMPANY, Boolean.valueOf(wasUsingCompany));
        String workerFirstName = bundle2.getString(WorkerFragment.WORKER_FIRST_NAME);
        String workerLastName = bundle2.getString(WorkerFragment.WORKER_LAST_NAME);
        String workerEmail = bundle2.getString(WorkerFragment.WORKER_EMAIL);
        String country = bundle2.getString(WorkerFragment.WORKER_COUNTRY);
        long dateOfHire = bundle2.getLong(WorkerFragment.DATE_OF_HIRE);
        ParseObject parseWorker = new ParseObject(WORKERS_CLASS);
        parseWorker.put(USERNAME, username);
        parseWorker.put(WorkerFragment.WORKER_ID, workerID);
        parseWorker.put(WorkerFragment.WORKER_EMAIL, workerEmail != null ? workerEmail : "");
        parseWorker.put(WorkerFragment.WORKER_FIRST_NAME, workerFirstName != null ? workerFirstName : "");
        parseWorker.put(WorkerFragment.WORKER_LAST_NAME, workerLastName != null ? workerLastName : "");
        parseWorker.put(WorkerFragment.WORKER_COUNTRY, country != null ? country : "");
        parseWorker.put(WorkerFragment.DATE_OF_HIRE, Long.valueOf(dateOfHire));
        List<ParseObject> parseObjectList = new ArrayList<>();
        parseObjectList.add(parsePastRecord);
        parseObjectList.add(parsePreferences);
        parseObjectList.add(parseWorker);
        $$Lambda$ServerDataSupplier$Zs4Nlrh9_80kSAVGSpqeRwbeVis r61 = r0;
        ParseObject parseObject = parseWorker;
        ParseObject parseObject2 = parsePastRecord;
        ParseObject parseObject3 = parsePreferences;
        String str = workerID;
        String str2 = username;
        $$Lambda$ServerDataSupplier$Zs4Nlrh9_80kSAVGSpqeRwbeVis r0 = new SaveCallback(this, timer, createdAt, pocketMoneyWeekOrMonth, nutritionWeekOrMonth, isWork7days, isPocketMoneyPartOfPay, saturdayBonus, totalPay, pocketMoney, nutritionPay, isElderHouse, isWellFareFromCompany, rentDeduction, livingExpensesDeduction, healthInsuranceDeduction, dayOfPay, vacationDays, sickDays, holidays, wellFareDaysPaid, totalCompanyPay, wasUsingCompany, workerEmail, workerFirstName, workerLastName, country, dateOfHire, payRegActivity) {
            private final /* synthetic */ ServerDataSupplier f$0;
            private final /* synthetic */ CountDownTimer f$1;
            private final /* synthetic */ float f$10;
            private final /* synthetic */ boolean f$11;
            private final /* synthetic */ boolean f$12;
            private final /* synthetic */ float f$13;
            private final /* synthetic */ float f$14;
            private final /* synthetic */ float f$15;
            private final /* synthetic */ int f$16;
            private final /* synthetic */ int f$17;
            private final /* synthetic */ int f$18;
            private final /* synthetic */ int f$19;
            private final /* synthetic */ long f$2;
            private final /* synthetic */ int f$20;
            private final /* synthetic */ float f$21;
            private final /* synthetic */ boolean f$22;
            private final /* synthetic */ String f$23;
            private final /* synthetic */ String f$24;
            private final /* synthetic */ String f$25;
            private final /* synthetic */ String f$26;
            private final /* synthetic */ long f$27;
            private final /* synthetic */ MyServerCallback f$28;
            private final /* synthetic */ int f$3;
            private final /* synthetic */ int f$4;
            private final /* synthetic */ boolean f$5;
            private final /* synthetic */ boolean f$6;
            private final /* synthetic */ float f$7;
            private final /* synthetic */ float f$8;
            private final /* synthetic */ float f$9;

            {
                this.f$0 = r4;
                this.f$1 = r5;
                this.f$2 = r6;
                this.f$3 = r8;
                this.f$4 = r9;
                this.f$5 = r10;
                this.f$6 = r11;
                this.f$7 = r12;
                this.f$8 = r13;
                this.f$9 = r14;
                this.f$10 = r15;
                this.f$11 = r16;
                this.f$12 = r17;
                this.f$13 = r18;
                this.f$14 = r19;
                this.f$15 = r20;
                this.f$16 = r21;
                this.f$17 = r22;
                this.f$18 = r23;
                this.f$19 = r24;
                this.f$20 = r25;
                this.f$21 = r26;
                this.f$22 = r27;
                this.f$23 = r28;
                this.f$24 = r29;
                this.f$25 = r30;
                this.f$26 = r31;
                this.f$27 = r32;
                this.f$28 = r34;
            }

            public final void done(ParseException parseException) {
                ParseException parseException2 = parseException;
                ServerDataSupplier serverDataSupplier = this.f$0;
                ServerDataSupplier serverDataSupplier2 = serverDataSupplier;
                ServerDataSupplier.lambda$savePayReg$16(serverDataSupplier2, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11, this.f$12, this.f$13, this.f$14, this.f$15, this.f$16, this.f$17, this.f$18, this.f$19, this.f$20, this.f$21, this.f$22, this.f$23, this.f$24, this.f$25, this.f$26, this.f$27, this.f$28, parseException2);
            }
        };
        ParseObject.saveAllInBackground(parseObjectList, r61);
    }

    public static /* synthetic */ void lambda$savePayReg$16(ServerDataSupplier serverDataSupplier, CountDownTimer timer, long createdAt, int pocketMoneyWeekOrMonth, int nutritionWeekOrMonth, boolean isWork7days, boolean isPocketMoneyPartOfPay, float saturdayBonus, float totalPay, float pocketMoney, float nutritionPay, boolean isElderHouse, boolean isWellFareFromCompany, float rentDeduction, float livingExpensesDeduction, float healthInsuranceDeduction, int dayOfPay, int vacationDays, int sickDays, int holidays, int wellFareDaysPaid, float totalCompanyPay, boolean wasUsingCompany, String workerEmail, String workerFirstName, String workerLastName, String country, long dateOfHire, MyServerCallback payRegActivity, ParseException e) {
        ServerDataSupplier serverDataSupplier2 = serverDataSupplier;
        long j = createdAt;
        MyServerCallback myServerCallback = payRegActivity;
        ParseException parseException = e;
        timer.cancel();
        if (parseException == null) {
            serverDataSupplier2.preferences.setCreatedAt(j);
            serverDataSupplier2.preferences.setPocketMoneyWeekOrMonth(pocketMoneyWeekOrMonth);
            serverDataSupplier2.preferences.setNutritionPayWeekOrMonth(nutritionWeekOrMonth);
            serverDataSupplier2.preferences.setWork7days(isWork7days);
            serverDataSupplier2.preferences.setPocketMoneyPartOfPay(isPocketMoneyPartOfPay);
            serverDataSupplier2.preferences.setSaturdayBonus(saturdayBonus);
            serverDataSupplier2.preferences.setTotalPay(totalPay);
            serverDataSupplier2.preferences.setPocketMoney(pocketMoney);
            serverDataSupplier2.preferences.setNutritionPay(nutritionPay);
            serverDataSupplier2.preferences.setElderHouse(isElderHouse);
            serverDataSupplier2.preferences.setGettingFromCompany(isWellFareFromCompany);
            serverDataSupplier2.preferences.setRentDeduction(rentDeduction);
            serverDataSupplier2.preferences.setLivingExpensesDeduction(livingExpensesDeduction);
            serverDataSupplier2.preferences.setHealthInsuranceDeduction(healthInsuranceDeduction);
            serverDataSupplier2.preferences.setDayOfPay(dayOfPay);
            serverDataSupplier2.pastRecord.setCreatedAt(j);
            serverDataSupplier2.pastRecord.setVacationDays(vacationDays);
            serverDataSupplier2.pastRecord.setSickDays(sickDays);
            serverDataSupplier2.pastRecord.setHolidays(holidays);
            serverDataSupplier2.pastRecord.setWellFareDaysPaid(wellFareDaysPaid);
            serverDataSupplier2.pastRecord.setTotalCompanyPay(totalCompanyPay);
            serverDataSupplier2.pastRecord.setGotFromCompany(wasUsingCompany);
            serverDataSupplier2.worker.setWorkerEmail(workerEmail);
            serverDataSupplier2.worker.setWorkerFirstName(workerFirstName);
            serverDataSupplier2.worker.setWorkerLastName(workerLastName);
            serverDataSupplier2.worker.setCountry(country);
            serverDataSupplier2.worker.setDateOfHire(dateOfHire);
            serverDataSupplier2.advancedUserDataReady = true;
            Log.d(TAG, "savePayReg: SUCCESS");
            myServerCallback.serverDone(MyActions.SAVE_PAY_REG, MyResults.SUCCESS, (Exception) null);
            return;
        }
        int i = nutritionWeekOrMonth;
        boolean z = isWork7days;
        boolean z2 = isPocketMoneyPartOfPay;
        float f = saturdayBonus;
        float f2 = totalPay;
        float f3 = pocketMoney;
        float f4 = nutritionPay;
        boolean z3 = isElderHouse;
        boolean z4 = isWellFareFromCompany;
        long j2 = dateOfHire;
        Log.e(TAG, "savePayReg: " + e.getMessage(), parseException);
        myServerCallback.serverDone(MyActions.SAVE_PAY_REG, MyResults.FAILED, parseException);
    }

    public SharedPreferences getSharedPreferences() {
        return this.sharedPreferences;
    }
}
