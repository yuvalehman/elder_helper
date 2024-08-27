package com.example.yuvallehman.myapplication.desktop.desktop;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.desktop.DesktopActivity;
import com.example.yuvallehman.myapplication.desktop.DesktopViewModel;
import com.example.yuvallehman.myapplication.desktop.desktop.DesktopRecyclerAdapter;
import com.example.yuvallehman.myapplication.interfaces.FragStates;
import com.example.yuvallehman.myapplication.interfaces.MyActions;
import com.example.yuvallehman.myapplication.interfaces.MyResults;
import com.example.yuvallehman.myapplication.interfaces.MyServerCallback;
import com.example.yuvallehman.myapplication.server_side_functions.ServerDataSupplier;
import java.util.Objects;

public class DesktopFragment extends Fragment implements MyServerCallback, DesktopRecyclerAdapter.DesktopRecyclerOnItemClickListener {
    public static final String START_LOGGING_DATE = "startLoggingDate";
    private static final String TAG = "DesktopFragment";
    private DesktopActivity activity;
    public DesktopRecyclerAdapter desktopRecyclerAdapter;
    private DesktopViewModel desktopViewModel;
    private DialogFragment dialogFragment;
    private FragmentManager fragmentManager;
    private ServerDataSupplier sds;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_desktop, container, false);
    }

    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach: start");
        super.onAttach(context);
        if (getActivity() != null) {
            this.activity = (DesktopActivity) getActivity();
        }
        if (getFragmentManager() != null) {
            this.fragmentManager = getFragmentManager();
        }
        this.sds = ServerDataSupplier.getInstance();
        this.desktopViewModel = (DesktopViewModel) ViewModelProviders.of((FragmentActivity) this.activity).get(DesktopViewModel.class);
        this.desktopRecyclerAdapter = new DesktopRecyclerAdapter(context, this);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: start");
        super.onViewCreated(view, savedInstanceState);
        DesktopActivity.fragState = FragStates.DESKTOP;
        ((RecyclerView) view.findViewById(R.id.desktopListView)).setAdapter(this.desktopRecyclerAdapter);
        if (!this.sds.eventsReady) {
            this.sds.fetchEvensFromServer(this);
        }
        ((ActionBar) Objects.requireNonNull(this.activity.getSupportActionBar())).setDisplayHomeAsUpEnabled(false);
        this.activity.navigationBar.getMenu().findItem(R.id.desknavdeskitem).setChecked(true);
        refreshEventList();
        Log.d(TAG, "onCreate: end");
    }

    private void openAddEventActivity(String eventType, int position) {
        Log.d(TAG, "openAddEventActivity: ");
        Bundle args = new Bundle();
        args.putString(ServerDataSupplier.EVENT_TYPE, eventType);
        args.putInt(AddEventFragment.POSITION, position);
        this.dialogFragment = new AddEventFragment();
        this.dialogFragment.setArguments(args);
        this.dialogFragment.show(this.fragmentManager, "eventDialog");
    }

    public void onPlusButtonPressed(int position) {
        openAddEventActivity(DesktopActivity.eventTypeListUnique[position], position);
    }

    public void onEditButtonPressed(int pos) {
        this.activity.openEditEventFragment(pos);
    }

    public void refreshEventList() {
        this.desktopViewModel.sortEventsFromServer();
        this.desktopRecyclerAdapter.notifyDataSetChanged();
    }

    public void addEventFinished() {
        Log.d(TAG, "addEventFinished: ");
        this.dialogFragment.dismiss();
        refreshEventList();
    }

    public void serverDone(MyActions actions, MyResults results, Exception e) {
        Log.d(TAG, "serverDone: start");
        if (MyActions.FETCH_EVENTS == actions && MyResults.SUCCESS == results) {
            if (this.sds.getEvents() != null) {
                new Handler().post(new Runnable() {
                    public final void run() {
                        DesktopFragment.lambda$serverDone$0(DesktopFragment.this);
                    }
                });
            }
        } else if (MyActions.EDIT_AND_SAVE_EVENT == actions) {
            Log.d(TAG, "serverDone: EDIT_AND_SAVE_EVENT");
        }
    }

    public static /* synthetic */ void lambda$serverDone$0(DesktopFragment desktopFragment) {
        Log.d(TAG, "serverDone: in the handler");
        desktopFragment.refreshEventList();
    }
}
