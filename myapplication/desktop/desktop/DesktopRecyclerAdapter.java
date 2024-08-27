package com.example.yuvallehman.myapplication.desktop.desktop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.desktop.DesktopActivity;
import com.example.yuvallehman.myapplication.desktop.DesktopViewModel;
import com.example.yuvallehman.myapplication.server_side_functions.ServerDataSupplier;

public class DesktopRecyclerAdapter extends RecyclerView.Adapter<DesktopRecyclerViewHolder> {
    private static final String TAG = "DesktopRecyclerAdapter";
    private Context context;
    private int itemCount;
    private DesktopRecyclerOnItemClickListener mListener;
    private ServerDataSupplier sds = ServerDataSupplier.getInstance();

    public interface DesktopRecyclerOnItemClickListener {
        void onEditButtonPressed(int i);

        void onPlusButtonPressed(int i);
    }

    DesktopRecyclerAdapter(Context context2, DesktopRecyclerOnItemClickListener listener) {
        this.context = context2;
        this.mListener = listener;
        this.itemCount = DesktopActivity.eventsTypeForDisplay != null ? DesktopActivity.eventsTypeForDisplay.length - 1 : 0;
    }

    @NonNull
    public DesktopRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DesktopRecyclerViewHolder(LayoutInflater.from(this.context).inflate(R.layout.desktop_recycler_layout, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull DesktopRecyclerViewHolder viewHolder, int position) {
        int pos = position;
        viewHolder.eventTypeTV.setText(DesktopActivity.eventsTypeForDisplay[pos]);
        Button button = viewHolder.plusB;
        button.setTag("plusB - " + pos);
        int value = 0;
        switch (pos) {
            case 0:
                value = DesktopViewModel.saturdayWorked;
                break;
            case 1:
                value = DesktopViewModel.holidaysWorked;
                break;
            case 2:
                value = (int) (DesktopViewModel.allowance + DesktopViewModel.convalescencePaid + DesktopViewModel.othersPaid + ((double) DesktopViewModel.holidayPaid) + ((double) DesktopViewModel.saturdayPaid) + DesktopViewModel.nutritionPaid);
                break;
            case 3:
                value = DesktopViewModel.vacationDaysTaken;
                break;
            case 4:
                value = DesktopViewModel.holidaysTaken;
                break;
            case 5:
                value = DesktopViewModel.sickDaysTaken;
                break;
            case 6:
                value = (int) DesktopViewModel.companyPay;
                break;
        }
        viewHolder.daysUsedTV.setText(String.valueOf(value));
        if (value != 0) {
            viewHolder.editB.setVisibility(0);
        } else {
            viewHolder.editB.setVisibility(4);
        }
        viewHolder.plusB.setOnClickListener(new View.OnClickListener(pos) {
            private final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                DesktopRecyclerAdapter.lambda$onBindViewHolder$0(DesktopRecyclerAdapter.this, this.f$1, view);
            }
        });
        viewHolder.editB.setOnClickListener(new View.OnClickListener(pos) {
            private final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                DesktopRecyclerAdapter.lambda$onBindViewHolder$1(DesktopRecyclerAdapter.this, this.f$1, view);
            }
        });
        if (this.sds.getPreferences().isGettingFromCompany()) {
            this.itemCount = DesktopActivity.eventsTypeForDisplay.length;
        } else {
            this.itemCount = DesktopActivity.eventsTypeForDisplay.length - 1;
        }
    }

    public static /* synthetic */ void lambda$onBindViewHolder$0(DesktopRecyclerAdapter desktopRecyclerAdapter, int pos, View v) {
        DesktopRecyclerOnItemClickListener desktopRecyclerOnItemClickListener = desktopRecyclerAdapter.mListener;
        if (desktopRecyclerOnItemClickListener != null) {
            desktopRecyclerOnItemClickListener.onPlusButtonPressed(pos);
        }
    }

    public static /* synthetic */ void lambda$onBindViewHolder$1(DesktopRecyclerAdapter desktopRecyclerAdapter, int pos, View v) {
        DesktopRecyclerOnItemClickListener desktopRecyclerOnItemClickListener = desktopRecyclerAdapter.mListener;
        if (desktopRecyclerOnItemClickListener != null) {
            desktopRecyclerOnItemClickListener.onEditButtonPressed(pos);
        }
    }

    public int getItemCount() {
        return this.itemCount;
    }

    static class DesktopRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView daysUsedTV;
        Button editB;
        TextView eventTypeTV;
        Button plusB;

        DesktopRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.eventTypeTV = (TextView) itemView.findViewById(R.id.desktopRecEventTypeTV);
            this.daysUsedTV = (TextView) itemView.findViewById(R.id.desktopRecyclerUsedDaysTV);
            this.plusB = (Button) itemView.findViewById(R.id.desktopRecButton);
            this.editB = (Button) itemView.findViewById(R.id.desktopRecEditB);
        }
    }
}
