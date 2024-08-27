package com.example.yuvallehman.myapplication.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yuvallehman.myapplication.R;
import java.util.List;

public class ConformationAdapter extends RecyclerView.Adapter<ConfirmationViewHolder> {
    private static final String TAG = "ConformationAdapter";
    private List<String[]> list;

    public ConformationAdapter(List<String[]> list2) {
        this.list = list2;
    }

    @NonNull
    public ConfirmationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ConfirmationViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.confirmation_recycler_item, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ConfirmationViewHolder viewHolder, int i) {
        viewHolder.title.setText(this.list.get(i)[0]);
        viewHolder.value.setText(this.list.get(i)[1]);
    }

    public int getItemCount() {
        List<String[]> list2 = this.list;
        if (list2 != null) {
            return list2.size();
        }
        return 0;
    }

    static class ConfirmationViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView value;

        ConfirmationViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.confirmationItemTitle);
            this.value = (TextView) itemView.findViewById(R.id.confirmationItmeValue);
        }
    }
}
