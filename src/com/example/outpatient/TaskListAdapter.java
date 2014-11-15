package com.example.outpatient;

import java.util.ArrayList;

import com.outpatient.storeCat.model.Reminder;
import com.outpatient.storeCat.model.Task;
import com.outpatient.storeCat.service.DBAccessImpl;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
 
public class TaskListAdapter extends ArrayAdapter<Task> {
 
        private final Context context;
        private final ArrayList<Task> itemsArrayList;
        private DBAccessImpl dbhandler;
 
        public TaskListAdapter(Context context, ArrayList<Task> itemsArrayList) {
 
            super(context, R.layout.listview_item, itemsArrayList);
 
            this.context = context;
            this.itemsArrayList = itemsArrayList;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
 
            // 1. Create inflater 
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.listview_item, parent, false);
 
            // 3. Get the two text view from the rowView
            TextView labelView = (TextView) rowView.findViewById(R.id.label);
            TextView valueView = (TextView) rowView.findViewById(R.id.value);
            
            // 4. Set the text for textView 
            labelView.setText(itemsArrayList.get(position).getName());
            
            int tid = itemsArrayList.get(position).getTid();
            
            // try to get the reminder for that task
            try{
            Reminder reminder = dbhandler.getReminderByTid(tid);
            
            if(reminder!=null)valueView.setText(reminder.toString());
            
            }catch(Exception e){
            	
            	Log.v("debugtag",e.toString());
            	
            }
            
 
            // 5. return rowView
            return rowView;
        }
}