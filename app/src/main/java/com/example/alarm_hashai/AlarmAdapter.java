// AlarmAdapter.java
package com.example.alarm_hashai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private List<AlarmItem> alarmList;
    private OnAlarmActionListener listener;

    public interface OnAlarmActionListener {
        void onCancelAlarm(AlarmItem alarmItem);
        void onToggleAlarm(AlarmItem alarmItem, boolean isEnabled);
    }

    public AlarmAdapter(List<AlarmItem> alarmList, OnAlarmActionListener listener) {
        this.alarmList = alarmList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        AlarmItem alarmItem = alarmList.get(position);

        holder.tvTime.setText(alarmItem.getFormattedTime());
        holder.tvAudioFile.setText(alarmItem.getAudioFileName());

        // Format and display scheduled date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String scheduledDate = dateFormat.format(new Date(alarmItem.getScheduledTime()));
        holder.tvScheduledDate.setText("Scheduled for: " + scheduledDate);

        holder.switchEnabled.setChecked(alarmItem.isEnabled());
        holder.switchEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onToggleAlarm(alarmItem, isChecked);
            }
        });

        holder.btnCancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancelAlarm(alarmItem);
            }
        });

        // Change appearance based on enabled state
        float alpha = alarmItem.isEnabled() ? 1.0f : 0.5f;
        holder.tvTime.setAlpha(alpha);
        holder.tvAudioFile.setAlpha(alpha);
        holder.tvScheduledDate.setAlpha(alpha);
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvAudioFile;
        TextView tvScheduledDate;
        SwitchMaterial switchEnabled;
        Button btnCancel;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvAudioFile = itemView.findViewById(R.id.tvAudioFile);
            tvScheduledDate = itemView.findViewById(R.id.tvScheduledDate);
            switchEnabled = itemView.findViewById(R.id.switchEnabled);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}