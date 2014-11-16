package com.example.outpatient.fragment.adapters;

import java.util.ArrayList;

import com.example.outpatient.R;
import com.example.outpatient.R.id;
import com.example.outpatient.R.layout;
import com.outpatient.storeCat.model.Reminder;
import com.outpatient.storeCat.model.Task;
import com.outpatient.storeCat.service.DBAccessImpl;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class TaskListAdapter extends ArrayAdapter<Task> {
 
        private final Context context;
        private ArrayList<Task> tasksArrayList;
        private DBAccessImpl dbhandler;
        private LayoutInflater inflater;
        private SparseBooleanArray mSelectedItemsIds;
 
        public TaskListAdapter(Context context, ArrayList<Task> itemsArrayList) {
 
            super(context, R.layout.task_item, itemsArrayList);
 
            this.context = context;
            this.tasksArrayList = itemsArrayList;
        }
 
        
        public void refreshTaskList(ArrayList<Task> newList){
        	
        	tasksArrayList = newList;
        	notifyDataSetChanged();
        }
        
        private class ViewHolder {
            TextView title;
            TextView notion;
            ImageView icon;
            ImageView flag;
        }
     
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            
            if (view == null) {
            	
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.task_item, null);
                
                // Locate the TextViews in listview_item.xml
                
                holder.title = (TextView) view.findViewById(R.id.title);
                holder.notion = (TextView) view.findViewById(R.id.notion);
                holder.icon = (ImageView) view.findViewById(R.id.icon);
                
                // Locate the ImageView in listview_item.xml
                
                holder.flag = (ImageView) view.findViewById(R.id.flag);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            // Capture position and set to the TextViews
            holder.title.setText(tasksArrayList.get(position).getName());
            try{
            Reminder reminder = DBAccessImpl.getInstance(context).getReminderByTid(tasksArrayList.get(position).getTid());
            if(reminder!=null)holder.notion.setText(reminder.toString());
            }catch (Exception e){}
            
            
            // haven't yet set the flags and icons
            
//          holder.icon.setText(worldpopulationlist.get(position).getPopulation());
//          // Capture position and set to the ImageView
//          holder.flag.setImageResource(worldpopulationlist.get(position).getFlag());
//            
            return view;
        }
        
        
        @Override
        public void remove(Task object) {
        	tasksArrayList.remove(object);
            notifyDataSetChanged();
        }
     
        public ArrayList<Task> getTaskList() {
            return tasksArrayList;
        }
     
        public void toggleSelection(int position) {
            selectView(position, !mSelectedItemsIds.get(position));
        }
     
        public void removeSelection() {
            mSelectedItemsIds = new SparseBooleanArray();
            notifyDataSetChanged();
        }
     
        public void selectView(int position, boolean value) {
            if (value)
                mSelectedItemsIds.put(position, value);
            else
                mSelectedItemsIds.delete(position);
            notifyDataSetChanged();
        }
     
        public int getSelectedCount() {
            return mSelectedItemsIds.size();
        }
     
        public SparseBooleanArray getSelectedIds() {
            return mSelectedItemsIds;
        }
}