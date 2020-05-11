package com.example.test.viewModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.ViewActivities;

import java.util.ArrayList;
import java.util.Map;

public class ActivityDetailsList extends ArrayAdapter<Map<String,String>> {
    private final Context context;
    private final ArrayList<Map<String,String>> values;

    public ActivityDetailsList(Context context, ArrayList<Map<String,String>> values) {
        super(context, R.layout.activity_list_template, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_list_template, parent, false);
        TextView activityName = (TextView) rowView.findViewById(R.id.lblActivityName);
        TextView activityDate = (TextView) rowView.findViewById(R.id.lblActivityDate);
        TextView activityTime = (TextView) rowView.findViewById(R.id.lblActivityTime);
        TextView activityReporter = (TextView) rowView.findViewById(R.id.lblReporter);
        ImageButton delete = (ImageButton) rowView.findViewById(R.id.btnDeleteActivity);
        Map<String,String> map =(Map<String,String>) values.get(position);
            String Name = String.valueOf(map.get("activityName"));
            String Date = String.valueOf(map.get("date"));
            String Time = String.valueOf(map.get("time"));
            String Reporter = String.valueOf(map.get("reporter"));
            activityName.setText(Name);
            activityDate.setText(Date);
            activityTime.setText(Time);
            activityReporter.setText(Reporter);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewActivities viewActivities = new ViewActivities();
                    viewActivities.deleteActivity(map.get("id"));
                }
            });

        return rowView;
    }
}
