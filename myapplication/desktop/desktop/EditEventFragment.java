package com.example.yuvallehman.myapplication.desktop.desktop;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.desktop.DesktopActivity;
import com.example.yuvallehman.myapplication.desktop.desktop.EditEventAdapter;
import com.example.yuvallehman.myapplication.interfaces.FragStates;
import com.example.yuvallehman.myapplication.interfaces.MyActions;
import com.example.yuvallehman.myapplication.interfaces.MyResults;
import com.example.yuvallehman.myapplication.interfaces.MyServerCallback;
import com.example.yuvallehman.myapplication.server_side_functions.ServerDataSupplier;
import com.example.yuvallehman.myapplication.simple_java_classes.Event;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

public class EditEventFragment extends Fragment implements EditEventAdapter.CallBack, MyServerCallback {
    private static final String TAG = "EditEventFragment";
    DesktopActivity activity;
    EditEventAdapter adapter;
    GregorianCalendar calendar;
    Context context;
    Event eventHolder;
    String eventType;
    String eventTypeForDisplay;
    TextView eventTypeTV;
    List<Event> fullEventList;
    List<Event> localList;
    int position;
    RecyclerView recyclerView;
    ServerDataSupplier serverDataSupplier;
    int year;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_event, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DesktopActivity.fragState = FragStates.EDIT;
        if (getArguments() != null) {
            this.position = getArguments().getInt(AddEventFragment.POSITION, -1);
            this.eventType = DesktopActivity.eventTypeListUnique[this.position];
            this.year = DesktopActivity.chosenYear;
            this.eventTypeForDisplay = DesktopActivity.eventsTypeForDisplay[this.position];
            this.eventTypeTV = (TextView) view.findViewById(R.id.editEventType);
            this.recyclerView = (RecyclerView) view.findViewById(R.id.editEventRecView);
            this.eventTypeTV.setText(this.eventTypeForDisplay);
            this.serverDataSupplier = ServerDataSupplier.getInstance();
            List<Event> list = this.localList;
            if (list == null) {
                this.localList = new ArrayList();
            } else {
                list.clear();
            }
            GregorianCalendar calendar2 = new GregorianCalendar();
            this.fullEventList = this.serverDataSupplier.getEvents();
            List<Event> list2 = this.fullEventList;
            if (list2 != null) {
                for (Event event : list2) {
                    if (this.eventType.equals(event.getEventType())) {
                        calendar2.setTimeInMillis(event.getDate());
                        this.localList.add(event);
                    }
                }
            }
            this.adapter = new EditEventAdapter(this.context, this.localList, this);
            this.recyclerView.setAdapter(this.adapter);
            ((ActionBar) Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar())).setDisplayHomeAsUpEnabled(true);
            return;
        }
        DesktopActivity desktopActivity = this.activity;
        if (desktopActivity != null) {
            desktopActivity.onBackPressed();
            Toast.makeText(this.context, "Problem...", 1).show();
        }
    }

    public void onAttach(Context context2) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context2);
        this.context = context2;
        this.calendar = new GregorianCalendar();
        if (getActivity() != null) {
            this.activity = (DesktopActivity) getActivity();
        }
    }

    public void serverDone(MyActions action, MyResults results, Exception e) {
        if (action == MyActions.DELETE_EVENT) {
            Log.d(TAG, "serverDone: delete event");
            if (MyResults.SUCCESS != results) {
                this.localList.add(this.eventHolder);
                this.adapter.notifyDataSetChanged();
                Snackbar.make((View) this.recyclerView, (CharSequence) "Error with the internet connection, try again later...", 0).show();
            }
        }
    }

    public void delete(Event event) {
        Log.d(TAG, "delete: ");
        this.eventHolder = event;
        this.localList.remove(this.eventHolder);
        this.adapter.notifyDataSetChanged();
        this.serverDataSupplier.deleteEventFromServer(event, this);
    }
}
