package com.example.yuvallehman.myapplication.desktop.desktop;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.desktop.DesktopActivity;
import com.example.yuvallehman.myapplication.helpers.Calculations;
import com.example.yuvallehman.myapplication.simple_java_classes.Event;
import java.util.GregorianCalendar;
import java.util.List;

public class EditEventAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private String eventType;
    private List<Event> list;
    private CallBack listener;

    public interface CallBack {
        void delete(Event event);
    }

    public EditEventAdapter(Context context2, List<Event> list2, CallBack listener2) {
        this.context = context2;
        this.list = list2;
        this.listener = listener2;
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.edit_event_item, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        if (this.list.size() > 0) {
            this.eventType = this.list.get(0).getEventType();
        }
        if (i == 0) {
            viewHolder.numberTV.setVisibility(4);
            viewHolder.deleteB.setVisibility(4);
            viewHolder.dateTV.setText(this.context.getString(R.string.date));
            viewHolder.dateTV.setTypeface((Typeface) null, 1);
            viewHolder.createdAtTV.setText(this.context.getString(R.string.created_at));
            viewHolder.createdAtTV.setTypeface((Typeface) null, 1);
            viewHolder.amountTV.setText(this.context.getString(R.string.amount));
            viewHolder.amountTV.setTypeface((Typeface) null, 1);
            String str = this.eventType;
            if (str != null && !str.equals(DesktopActivity.eventTypeListUnique[2]) && !this.eventType.equals(DesktopActivity.eventTypeListUnique[6])) {
                viewHolder.amountTV.setVisibility(8);
                return;
            }
            return;
        }
        Event event = this.list.get(i - 1);
        viewHolder.numberTV.setText(String.valueOf(i));
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(event.getDate());
        viewHolder.dateTV.setText(Calculations.displayDate(this.context, calendar.getTimeInMillis()));
        calendar.setTimeInMillis(event.getCreatedAt());
        viewHolder.createdAtTV.setText(Calculations.displayDate(this.context, calendar.getTimeInMillis()));
        if (event.getEventType().equals(DesktopActivity.eventTypeListUnique[2]) || event.getEventType().equals(DesktopActivity.eventTypeListUnique[6])) {
            viewHolder.amountTV.setText(String.valueOf(event.getMoneyGiven()));
        } else {
            viewHolder.amountTV.setVisibility(8);
        }
        viewHolder.deleteB.setOnClickListener(new View.OnClickListener(event) {
            private final /* synthetic */ Event f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                EditEventAdapter.this.listener.delete(this.f$1);
            }
        });
    }

    public int getItemCount() {
        return this.list.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amountTV;
        TextView createdAtTV;
        TextView dateTV;
        Button deleteB;
        TextView numberTV;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.numberTV = (TextView) itemView.findViewById(R.id.editNumber);
            this.dateTV = (TextView) itemView.findViewById(R.id.editDate);
            this.createdAtTV = (TextView) itemView.findViewById(R.id.editCreatedAt);
            this.amountTV = (TextView) itemView.findViewById(R.id.editAmountTV);
            this.deleteB = (Button) itemView.findViewById(R.id.editDelete);
        }
    }
}
