package ru.tnkv.ecapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReportBlockAdapter extends ArrayAdapter<ReportBlock> {

    public ReportBlockAdapter(Context context, ReportBlock[] arr) {
        super(context, R.layout.report_block, arr);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ReportBlock report = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.report_block, null);
        }

// Заполняем адаптер
        ((TextView) convertView.findViewById(R.id.report_date)).setText(report.date);
        ((TextView) convertView.findViewById(R.id.report_appname)).setText(String.valueOf(report.appName));
        ((TextView) convertView.findViewById(R.id.report_location)).setText(String.valueOf(report.location));
        ((TextView) convertView.findViewById(R.id.report_exception)).setText(String.valueOf(report.excType));
        ((TextView) convertView.findViewById(R.id.report_id)).setText(String.valueOf(report.reportID));

        return convertView;
    }
}