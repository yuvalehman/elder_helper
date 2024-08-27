package com.example.yuvallehman.myapplication.desktop.paycheck;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yuvallehman.myapplication.R;
import com.example.yuvallehman.myapplication.desktop.paycheck.PaycheckRecyclerAdapter;
import com.example.yuvallehman.myapplication.simple_java_classes.PayItem;
import java.util.List;

public class PaycheckRecyclerAdapter extends RecyclerView.Adapter<PayViewHolder> {
    private static final String TAG = "PaycheckRecyclerAdapter";
    private PaycheckRecyclerInterface callback;
    private SparseBooleanArray checkList;
    private Context context;
    private List<PayItem> list;

    interface PaycheckRecyclerInterface {
        void checkListChanged(int i, boolean z);
    }

    PaycheckRecyclerAdapter(PaycheckRecyclerInterface callback2, Context context2, List<PayItem> list2, SparseBooleanArray checkList2) {
        Log.d(TAG, "PaycheckRecyclerAdapter: constructor");
        this.context = context2;
        this.list = list2;
        this.checkList = checkList2;
        this.callback = callback2;
    }

    public void onBindViewHolder(@NonNull PayViewHolder holder, int position) {
        ViewGroup.LayoutParams tempParams = holder.layout.getLayoutParams();
        PayItem item = this.list.get(position);
        boolean b = this.checkList.get(position);
        holder.titleTV.setText(item.getTitle());
        holder.valueTV.setText(item.getValue());
        holder.checkBox.setChecked(b);
        holder.layout.setOnClickListener(new View.OnClickListener(holder, position) {
            private final /* synthetic */ PaycheckRecyclerAdapter.PayViewHolder f$1;
            private final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void onClick(View view) {
                PaycheckRecyclerAdapter.lambda$onBindViewHolder$0(PaycheckRecyclerAdapter.this, this.f$1, this.f$2, view);
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener(position, holder) {
            private final /* synthetic */ int f$1;
            private final /* synthetic */ PaycheckRecyclerAdapter.PayViewHolder f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void onClick(View view) {
                PaycheckRecyclerAdapter.this.callback.checkListChanged(this.f$1, this.f$2.checkBox.isChecked());
            }
        });
        if (!PaycheckFragment.SHOW_VALUES_ONLY) {
            tempParams.height = -2;
        } else if (Integer.parseInt(item.getValue()) == 0) {
            tempParams.height = 0;
        } else {
            tempParams.height = -2;
        }
    }

    public static /* synthetic */ void lambda$onBindViewHolder$0(@NonNull PaycheckRecyclerAdapter paycheckRecyclerAdapter, PayViewHolder holder, int position, View v) {
        holder.checkBox.setChecked(!holder.checkBox.isChecked());
        paycheckRecyclerAdapter.callback.checkListChanged(position, holder.checkBox.isChecked());
    }

    @NonNull
    public PayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PayViewHolder(LayoutInflater.from(this.context).inflate(R.layout.paycheck_recycler_item, parent, false));
    }

    public int getItemCount() {
        return this.list.size();
    }

    class PayViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        View layout;
        TextView titleTV;
        TextView valueTV;

        PayViewHolder(@NonNull View itemView) {
            super(itemView);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.payChipView);
            this.titleTV = (TextView) itemView.findViewById(R.id.payTitleView);
            this.valueTV = (TextView) itemView.findViewById(R.id.payValueView);
            this.layout = itemView;
        }
    }
}
