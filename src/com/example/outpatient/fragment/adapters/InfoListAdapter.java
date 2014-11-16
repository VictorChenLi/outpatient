package com.example.outpatient.fragment.adapters;

import java.util.ArrayList;

import com.example.outpatient.R;
import com.outpatient.storeCat.model.Info;
import com.outpatient.storeCat.service.DBAccessImpl;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
 
public class InfoListAdapter extends ArrayAdapter<Info> {
 
        private final Context context;
        private ArrayList<Info> infoArrayList;
        private DBAccessImpl dbhandler;
 
        public InfoListAdapter(Context context, ArrayList<Info> itemsArrayList) {
 
            super(context, R.layout.info_item,itemsArrayList);
 
            this.context = context;
            this.infoArrayList = itemsArrayList;
        }
 
        
        public void refreshList(ArrayList<Info> newList){
        	
        	infoArrayList = newList;
        	notifyDataSetChanged();
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
 
            // 1. Create inflater 
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.info_item, parent, false);
 
            // 3. Get the two text view from the rowView
            TextView labelView = (TextView) rowView.findViewById(R.id.label);
            TextView valueView = (TextView) rowView.findViewById(R.id.value);
            
            // 4. Set the text for textView 
            labelView.setText(infoArrayList.get(position).getQue());
            
            
            // 5. return rowView
            return rowView;
        }
}