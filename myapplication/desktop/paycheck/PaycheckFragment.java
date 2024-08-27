package com.example.yuvallehman.myapplication.desktop.paycheck;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.desktop.DesktopActivity;
import com.example.yuvallehman.myapplication.desktop.DesktopViewModel;
import com.example.yuvallehman.myapplication.desktop.paycheck.PaycheckRecyclerAdapter;
import com.example.yuvallehman.myapplication.interfaces.FragStates;
import com.example.yuvallehman.myapplication.server_side_functions.ServerDataSupplier;
import com.example.yuvallehman.myapplication.simple_java_classes.PayItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaycheckFragment extends Fragment implements View.OnClickListener, PaycheckRecyclerAdapter.PaycheckRecyclerInterface, CompoundButton.OnCheckedChangeListener {
    private static final long BUTTON_DELAY = 3000;
    private static final String CAREGIVER_REPORT = "careGiverReport";
    private static final String EMPLOYER_REPORT = "employerReport";
    private static final long FADE_DELAY = 1000;
    private static final long HIDE_FAB_DELAY = 2000;
    static boolean SHOW_VALUES_ONLY = false;
    private static final String TAG = "PaycheckFragment";
    private DesktopActivity activity;
    private PaycheckRecyclerAdapter adapter;
    private RadioButton caregiverRadio;
    private SparseBooleanArray checkList = new SparseBooleanArray();
    private DesktopViewModel desktopViewModel;
    private final int downFADID = R.id.downFAB;
    private FloatingActionButton downloadFAB;
    private RadioButton employerRadio;
    private boolean firstClick = true;
    private Handler handler;
    private Runnable hideButtonsRunnable;
    private FloatingActionButton menuFAB;
    private final int menuFADID = R.id.menuFAB;
    private List<PayItem> payItemList = new ArrayList();
    private FloatingActionButton pdfFAB;
    private final int pdfFADID = R.id.pdfFAB;
    private RadioBoxesState radioState = RadioBoxesState.EMPLOYER;
    private ServerDataSupplier sds;
    private FloatingActionButton shareFAB;
    private final int shareFADID = R.id.shareFAB;
    private Runnable showFabMenuRunnable;

    enum RadioBoxesState {
        EMPLOYER,
        CAREGIVER
    }

    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
        ((ActionBar) Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar())).setDisplayHomeAsUpEnabled(true);
        this.activity = (DesktopActivity) getActivity();
        this.handler = new Handler();
        this.hideButtonsRunnable = new Runnable() {
            public final void run() {
                PaycheckFragment.this.hideButtons();
            }
        };
        this.sds = ServerDataSupplier.getInstance();
        this.desktopViewModel = (DesktopViewModel) ViewModelProviders.of((FragmentActivity) this.activity).get(DesktopViewModel.class);
        this.adapter = new PaycheckRecyclerAdapter(this, context, this.payItemList, this.checkList);
        this.showFabMenuRunnable = new Runnable() {
            public final void run() {
                PaycheckFragment.lambda$onAttach$0(PaycheckFragment.this);
            }
        };
    }

    public static /* synthetic */ void lambda$onAttach$0(PaycheckFragment paycheckFragment) {
        paycheckFragment.menuFAB.setVisibility(0);
        paycheckFragment.menuFAB.animate().alpha(100.0f).setDuration(FADE_DELAY);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_paycheck, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        this.activity.setTitle(R.string.paycheck_title);
        DesktopActivity.fragState = FragStates.MAKE_PAYCHECK;
        this.menuFAB = (FloatingActionButton) view.findViewById(R.id.menuFAB);
        this.shareFAB = (FloatingActionButton) view.findViewById(R.id.shareFAB);
        this.downloadFAB = (FloatingActionButton) view.findViewById(R.id.downFAB);
        this.pdfFAB = (FloatingActionButton) view.findViewById(R.id.pdfFAB);
        this.caregiverRadio = (RadioButton) view.findViewById(R.id.cargiverradio);
        this.employerRadio = (RadioButton) view.findViewById(R.id.employerradio);
        NestedScrollView nestedScrollView = (NestedScrollView) view.findViewById(R.id.paycheckNestedScrollView);
        this.caregiverRadio.setTag("caregiverRadio");
        this.employerRadio.setTag("employerRadio");
        this.caregiverRadio.setOnCheckedChangeListener(this);
        this.employerRadio.setOnCheckedChangeListener(this);
        ((RadioButton) view.findViewById(R.id.showallradio)).setOnCheckedChangeListener(this);
        ((RadioButton) view.findViewById(R.id.showvaluesradio)).setOnCheckedChangeListener(this);
        this.menuFAB.setOnClickListener(this);
        this.shareFAB.setOnClickListener(this);
        this.downloadFAB.setOnClickListener(this);
        this.pdfFAB.setOnClickListener(this);
        this.activity.navigationBar.getMenu().findItem(R.id.desknavpaycheckitem).setChecked(true);
        this.payItemList.clear();
        this.payItemList.addAll(this.desktopViewModel.createPaycheckDisplayList());
        loadChecks();
        ((RecyclerView) view.findViewById(R.id.paycheckRecyclerView)).setAdapter(this.adapter);
        if (Build.VERSION.SDK_INT >= 23) {
            nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                public final void onScrollChange(View view, int i, int i2, int i3, int i4) {
                    PaycheckFragment.lambda$onViewCreated$1(PaycheckFragment.this, view, i, i2, i3, i4);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$onViewCreated$1(PaycheckFragment paycheckFragment, View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        paycheckFragment.handler.removeCallbacks(paycheckFragment.showFabMenuRunnable);
        paycheckFragment.shareFAB.setVisibility(4);
        paycheckFragment.downloadFAB.setVisibility(4);
        paycheckFragment.pdfFAB.setVisibility(4);
        paycheckFragment.hideButtons();
        paycheckFragment.handler.postDelayed(paycheckFragment.showFabMenuRunnable, HIDE_FAB_DELAY);
        paycheckFragment.menuFAB.setVisibility(4);
        paycheckFragment.menuFAB.setAlpha(0.0f);
    }

    /* access modifiers changed from: private */
    public void hideButtons() {
        Log.d(TAG, "hideButtons: ");
        this.shareFAB.animate().setDuration(FADE_DELAY).alpha(0.0f).x(this.menuFAB.getX());
        this.downloadFAB.animate().setDuration(FADE_DELAY).alpha(0.0f).x(this.menuFAB.getX());
        this.pdfFAB.animate().setDuration(FADE_DELAY).alpha(0.0f).x(this.menuFAB.getX());
        this.firstClick = true;
    }

    private void loadChecks() {
        if (this.employerRadio.isChecked()) {
            loadChecksFromPref(EMPLOYER_REPORT);
        } else if (this.caregiverRadio.isChecked()) {
            loadChecksFromPref(CAREGIVER_REPORT);
        }
    }

    public void checkListChanged(int pos, boolean isChecked) {
        if (this.checkList.size() > pos) {
            this.checkList.put(pos, isChecked);
            Log.d(TAG, "checkListChanged: checkList.put(" + pos + ", " + isChecked + ")");
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.downFAB) {
            Log.d(TAG, "onClick: 3");
        } else if (id == R.id.menuFAB) {
            Log.d(TAG, "onClick: 1");
            if (this.firstClick) {
                this.shareFAB.setVisibility(0);
                this.downloadFAB.setVisibility(0);
                this.pdfFAB.setVisibility(0);
                this.shareFAB.animate().x(this.menuFAB.getX() - ((float) this.menuFAB.getWidth())).alpha(100.0f).setDuration(FADE_DELAY);
                this.downloadFAB.animate().x(this.menuFAB.getX() - ((float) (this.menuFAB.getWidth() * 2))).alpha(100.0f).setDuration(FADE_DELAY);
                this.pdfFAB.animate().x(this.menuFAB.getX() - ((float) (this.menuFAB.getWidth() * 3))).alpha(100.0f).setDuration(FADE_DELAY);
                this.handler.postDelayed(this.hideButtonsRunnable, BUTTON_DELAY);
                this.firstClick = false;
                return;
            }
            this.handler.removeCallbacks(this.hideButtonsRunnable);
            hideButtons();
        } else if (id == R.id.pdfFAB) {
            Log.d(TAG, "onClick: 4");
        } else if (id == R.id.shareFAB) {
            Log.d(TAG, "onClick: 2");
        }
    }

    public void onPause() {
        saveCheckList();
        super.onPause();
    }

    private void saveCheckList() {
        String key;
        String booleanListString = "";
        if (this.radioState == RadioBoxesState.EMPLOYER) {
            key = EMPLOYER_REPORT;
        } else {
            key = CAREGIVER_REPORT;
        }
        for (int i = 0; i < this.checkList.size(); i++) {
            booleanListString = booleanListString.concat(this.checkList.get(i) ? "t" : "f").concat(";");
        }
        this.sds.getSharedPreferences().edit().putString(key, booleanListString).apply();
        Log.d(TAG, "saveCheckList: " + key + " ; " + booleanListString);
    }

    private void loadChecksFromPref(String key) {
        this.checkList.clear();
        String report = this.sds.getSharedPreferences().getString(key, "");
        Log.d(TAG, "loadChecksFromPref: " + key + " ; " + report);
        String[] reportTextSplit = (report == null || report.equals("")) ? new String[this.payItemList.size()] : report.split(";");
        for (int i = 0; i < reportTextSplit.length; i++) {
            String aReportTextSplit = reportTextSplit[i];
            this.checkList.append(i, aReportTextSplit != null && aReportTextSplit.equals("t"));
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String key;
        if (isChecked) {
            saveCheckList();
            Log.d(TAG, "onCheckedChanged: start");
            if (buttonView.getId() == R.id.cargiverradio || buttonView.getId() == R.id.employerradio) {
                if (buttonView.getId() == R.id.cargiverradio) {
                    this.radioState = RadioBoxesState.CAREGIVER;
                    key = CAREGIVER_REPORT;
                } else {
                    this.radioState = RadioBoxesState.EMPLOYER;
                    key = EMPLOYER_REPORT;
                }
                loadChecksFromPref(key);
            } else {
                SHOW_VALUES_ONLY = buttonView.getId() != R.id.showallradio;
            }
            this.adapter.notifyDataSetChanged();
        }
    }

    public void refreshPayList() {
        Log.d(TAG, "refreshPayList: ");
        this.desktopViewModel.sortEventsFromServer();
        this.payItemList.clear();
        this.payItemList.addAll(this.desktopViewModel.createPaycheckDisplayList());
        this.adapter.notifyDataSetChanged();
    }
}
