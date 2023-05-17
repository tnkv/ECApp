package ru.tnkv.ecapp.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.tnkv.ecapp.R;

public class ReportBlockAdapter extends RecyclerView.Adapter<ReportBlockAdapter.ViewClass> {
    // Добавление репортов на экран
    final ArrayList<Long> reportId;
    final ArrayList<String> reportDate;
    final ArrayList<String> reportAppname;
    final ArrayList<String> reportLocation;
    final ArrayList<String> reportException;
    public ReportBlockAdapter(ArrayList<Long> reportId,
                              ArrayList<String> reportDate,
                              ArrayList<String> reportAppname,
                              ArrayList<String> reportLocation,
                              ArrayList<String> reportException) {
        this.reportId = reportId;
        this.reportDate = reportDate;
        this.reportAppname = reportAppname;
        this.reportLocation = reportLocation;
        this.reportException = reportException;
    }

    @NonNull
    @Override
    public ViewClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report, parent, false);
        return new ViewClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewClass holder, int position) {
        // Прописываю каждому CardView нужные значения
        holder.reportId.setText(String.valueOf(reportId.get(position)));
        holder.reportDate.setText(reportDate.get(position));
        holder.reportAppname.setText(reportAppname.get(position));
        holder.reportLocation.setText(reportLocation.get(position));
        holder.reportException.setText(reportException.get(position));
    }

    @Override
    public int getItemCount() {
        // Получаю количество репортов
        return reportId.size();
    }


    public class ViewClass extends RecyclerView.ViewHolder {
        final TextView reportId;
        final TextView reportDate;
        final TextView reportAppname;
        final TextView reportLocation;
        final TextView reportException;
        public ViewClass(@NonNull View itemView) {
            super(itemView);
            reportId = itemView.findViewById(R.id.report_id);
            reportDate = itemView.findViewById(R.id.report_date);
            reportAppname = itemView.findViewById(R.id.report_appname);
            reportLocation = itemView.findViewById(R.id.report_location);
            reportException = itemView.findViewById(R.id.report_exception);
        }
    }

}