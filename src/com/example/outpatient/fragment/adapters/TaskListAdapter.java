package com.example.outpatient.fragment.adapters;

import java.util.ArrayList;

import com.example.outpatient.R;
import com.example.outpatient.R.id;
import com.example.outpatient.R.layout;
import com.outpatient.storeCat.model.Reminder;
import com.outpatient.storeCat.model.Task;
import com.outpatient.storeCat.service.DBAccessImpl;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
            
            mSelectedItemsIds = new SparseBooleanArray();
            
            
        }
        
        public TaskListAdapter(Context context) {
        	 
            super(context, R.layout.task_item);
 
            this.context = context;
            this.tasksArrayList = generateData();
        }
 
        
        public void refreshTaskList(ArrayList<Task> newList){
        	
        	tasksArrayList.clear();
        	tasksArrayList.addAll(newList);
        	this.notifyDataSetChanged();
        }
        
        private ArrayList<Task> generateData(){
            
            //read the tasks from database and add them to the list
        	ArrayList<Task> task_list ;
            dbhandler = DBAccessImpl.getInstance(context);
            
            //query all the tasks to be shown in the task list 
            task_list = (ArrayList<Task>) dbhandler.queryShowTaskList();
            
            return task_list;
        }
        
        private class ViewHolder {
            TextView title;
            TextView notion;
            ImageView icon;
            ImageView flag;
        }
     
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            
            if (convertView == null) {
            	
                holder = new ViewHolder();

                convertView = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
  
                // Locate the TextViews in listview_item.xml
                
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.notion = (TextView) convertView.findViewById(R.id.notion);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                
                // Locate the ImageView in listview_item.xml
                
                holder.flag = (ImageView) convertView.findViewById(R.id.flag);
                convertView.setTag(holder);
                
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // Capture position and set to the TextViews
            holder.title.setText(tasksArrayList.get(position).getName());
            
            int taskType = tasksArrayList.get(position).getTaskType();
            
            Drawable general_task_icon = context.getResources().getDrawable(R.drawable.general_task);   
            Drawable medication_task_icon= context.getResources().getDrawable(R.drawable.medication_task);   
            Drawable appointment_task_icon= context.getResources().getDrawable(R.drawable.contact_task);
            Drawable has_reminder= context.getResources().getDrawable(R.drawable.flag);
            
            if(taskType == 1)holder.icon.setBackground(medication_task_icon);
            else if(taskType == 2)holder.icon.setBackground(general_task_icon);
            else if(taskType == 3)holder.icon.setBackground(appointment_task_icon);
            
            
            try{
            	
	            Reminder reminder = DBAccessImpl.getInstance(context).getReminderByTid(tasksArrayList.get(position).getTid());
	            
	            if(reminder!=null && reminder.toNotion()!="NOREMINDER"){
	            	
	            	// setting the text for the reminder
	            	holder.notion.setText(reminder.toNotion());
	            	
	            	// setting the image of the reminder
	            	holder.flag.setBackground(has_reminder);
	            
	            }else {
	            	
	            	// setting the text for the reminder
	            	holder.notion.setText("");
	            	
	            }
	            
            }catch (Exception e){}
         
//          holder.icon.setText(worldpopulationlist.get(position).getPopulation());
//          // Capture position and set to the ImageView
//          holder.flag.setImageResource(worldpopulationlist.get(position).getFlag());
//            
            return convertView;
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