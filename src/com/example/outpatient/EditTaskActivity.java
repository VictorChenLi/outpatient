package com.example.outpatient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.model.Reminder;
import com.outpatient.storeCat.model.Task;
import com.outpatient.storeCat.service.DBAccessImpl;
import com.outpatient.sysUtil.model.Constant;
import com.outpatient.sysUtil.model.GlobalVar;
import com.outpatient.sysUtil.service.DateTimePicker;
import com.outpatient.sysUtil.service.DateTimePicker.ICustomDateTimeListener;

import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditTaskActivity extends Activity implements ICustomDateTimeListener{

	private DateTimePicker dateTimePicker;
	private Button confirmBtn;
	
	//reference to the elements in order of appearance
	private EditText task_title;
	private EditText notes_edit;
	
	private TextView task_description;
	private CheckBox remind_check;
	private LinearLayout remind_area;
	private Button start_date_button;
	private CheckBox routine_check;
	private LinearLayout routine_area;
	
	private Spinner routine_times;
	private Spinner routine_days;
	private Spinner plan_select;
	private Spinner task_type;
	
	private Button end_date_button;
	
	private RadioButton selectTimes;
	private RadioButton selectDays;
	
	private RadioGroup selectTimeDayGroup;
	
	//holds information of the current editing task
	private Task editingTask;
	
	private boolean isRemind = false;
	private boolean isRoutine = false;
	
	private String which_date_setting;
	
	//stores the current date
	private Date start_date;
	private Date end_date;
	
	private int passed_tid = 0;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);
        
        confirmBtn = (Button)findViewById(R.id.confirm_button);
        
        task_title = (EditText)findViewById(R.id.task_title);
        task_description = (TextView)findViewById(R.id.task_description);
        remind_check = (CheckBox)findViewById(R.id.remind_check);
        remind_area = (LinearLayout)findViewById(R.id.remind_area);
        start_date_button = (Button)findViewById(R.id.start_date);
        routine_check = (CheckBox)findViewById(R.id.routine_check);
        routine_area = (LinearLayout)findViewById(R.id.routine_area);
        
        plan_select = (Spinner)findViewById(R.id.plan_select);
        routine_times = (Spinner)findViewById(R.id.routine_times);
        routine_days = (Spinner)findViewById(R.id.routine_days);
        task_type = (Spinner)findViewById(R.id.task_type);
        
        end_date_button = (Button)findViewById(R.id.end_date);
        selectTimes = (RadioButton)findViewById(R.id.radiobutton_daily);
        selectDays = (RadioButton)findViewById(R.id.radiobutton_repeatday);
        selectTimeDayGroup = (RadioGroup)findViewById(R.id.selectTimesOrDaysGroup);
        
        notes_edit = (EditText)findViewById(R.id.notes_edit);
        
	    // Create an ArrayAdapter using the string array and a default spinner layout
	    ArrayAdapter<CharSequence> times_adapter = ArrayAdapter.createFromResource(this,
	            R.array.routine_list, android.R.layout.simple_spinner_item);
	    
	    // Specify the layout to use when the list of choices appears
	    times_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    
	    // Apply the adapter to the spinner
	    routine_times.setAdapter(times_adapter);
	    
	    // Apply the adapter to the spinner
	    routine_days.setAdapter(times_adapter);
	    
        remind_area.setVisibility(View.GONE);
        routine_area.setVisibility(View.GONE);
        
        //set the two spinners disabled first
        routine_times.setEnabled(false);
        routine_days.setEnabled(false);
        
        
        //SETTING UP THE PLAN SELECTOR
        
        List<String> spinnerArray =  new ArrayList<String>();
        	
    	List<Plan> currentPlan = DBAccessImpl.getInstance(getApplicationContext()).queryShowPlanList();
    	
    		for(int i = 0; i < currentPlan.size(); i++){
    		
    			spinnerArray.add(i+1+". "+currentPlan.get(i));
    		}
    	
        
        ArrayAdapter<String> plan_spinner_adapter = new ArrayAdapter<String>(
        	    this, android.R.layout.simple_spinner_item, spinnerArray);
        
        
        times_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plan_select.setAdapter(plan_spinner_adapter);
        
        
        
        // SETTING UP THE TASK TYPE SPINNER
        
        // Create an ArrayAdapter using the string array and a default spinner layout
	    ArrayAdapter<CharSequence> taskType_Adapter = ArrayAdapter.createFromResource(this,
	            R.array.task_type_list, android.R.layout.simple_spinner_item);
	    
	    // Specify the layout to use when the list of choices appears
	    taskType_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    
	    // Apply the adapter to the spinner
	    task_type.setAdapter(taskType_Adapter);
	    
	    
        
        // THE FOLLOWING CODE IS FOR EDITING TO LOAD PREVIOUS STATUS
        if(getIntent().getExtras()!=null)passed_tid = getIntent().getExtras().getInt("tid",0);
        
        Log.v("debugtag", "tid opened:"+passed_tid);
        
        editingTask = DBAccessImpl.getInstance(getApplicationContext()).describeTask(passed_tid);
        
        
	        if(editingTask!=null){
	        	
	        task_title.setText(editingTask.getName());
	        
	        //error control
	        if(editingTask.getDes()!=null)if(editingTask.getDes().length()>1)task_description.setText(editingTask.getDes());
	        
	        //checking if the task has a reminder
	        Reminder taskReminder = DBAccessImpl.getInstance(getApplicationContext()).getReminderByTid(passed_tid);
	        
	        if(taskReminder!=null)remind_check.setChecked(true);
	        else {remind_check.setChecked(false);}
        
	        //The following only happens when a task HAVE a reminder
	        if(taskReminder!=null){
	        	
	        	remind_area.setVisibility(View.VISIBLE);
	        	long date = taskReminder.getStartTime(); 
	        	
	        	start_date_button.setText(getDate(82233213123L, "yyyy-MM-dd hh:mm a"));
	        	
	        	int isRoutine = taskReminder.getIsRoutine();
		        	
	        		//if there's routine saved
	        		if(isRoutine==1){
		        		
	        			//check the routine check box
	        			routine_check.setChecked(true);
	        			routine_area.setVisibility(View.VISIBLE);
	        			
	        			//if it's X times daily
	        			int xTimesDaily = taskReminder.getRepeatingTimes();
	        			int onceXDays = taskReminder.getRepeatingTimes();
	        			
	        			if(xTimesDaily==1){
	        				
	        				selectTimes.setSelected(true);
	        				
	        			}
	        			else if(onceXDays==1){
	        				
	        				selectDays.setSelected(true);
	        				
	        			}
	        			
		        	}else{routine_check.setChecked(false);}
	        	
	        	}
	        
	        }else{
	        	
	        //initiate current date and time
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
	        String currentDateandTime = sdf.format(new Date());
	        start_date_button.setText(currentDateandTime);    
		        
		        try {
					start_date = sdf.parse(currentDateandTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
	        }
	        
	        
	        
	      //Radio button checked listener
	        selectTimeDayGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					
					// get selected radio button from radioGroup
					int selectedId = selectTimeDayGroup.getCheckedRadioButtonId();
					
					RadioButton selectedButton = (RadioButton)findViewById(selectedId);
					
					if(selectedButton!=null){
						
						if(selectedButton == selectTimes){
								
							Log.v("debugtag","clicked times");
							routine_times.setEnabled(true);
							routine_days.setEnabled(false);
							
							
						}else if (selectedButton == selectDays){
							
							Log.v("debugtag","clicked days");
							routine_times.setEnabled(false);
							routine_days.setEnabled(true);
							
						}
						
					}else{Log.v("schoice","NONE QUESTION");}
					
					
					
				}
	      
	        });
	        
	        
	        
	        
	        
        
        confirmBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//didn't pass tid then add new task
				if(passed_tid == 0){
				
				Task addedTask = new Task();
				
				String taskTitleText = task_title.getText().toString();
				
				
				// Try Catch to handle exception
				try{
				
				//error control for task title
				if(taskTitleText.length()>1){
					
					//setting the task title
					addedTask.setName(taskTitleText);
					
					String taskType = task_type.getSelectedItem().toString();
					
					int typeID = 1;
					
					//setting the task type
					if(taskType.equals("General"))typeID = 1;
					if(taskType.equals("Medication"))typeID = 2;
					if(taskType.equals("Appointment/Contact"))typeID = 3;
					
					addedTask.setTaskType(typeID);
					addedTask.setNotes(notes_edit.getText().toString());
					
					//there is a remind
					if(isRemind){
						
						if(end_date.after(start_date)){
							
							// insert the task to database 
							int new_tid = DBAccessImpl.getInstance(getApplicationContext()).InsertTask(addedTask);
							
							// THE FOLLOWING GET'S PARAMETERS FOR THE NEW REMIND
							Reminder taskReminder = new Reminder();
							taskReminder.setRid(new_tid);
							
							int routineInt = (isRoutine) ? 1 : 0;
									
							taskReminder.setIsRoutine(routineInt);
								
							
								//IF SET ROUTINE, THEN UPDATE REMINDER WITH TIMES AND DAYS
							
								if(isRoutine){
									
									//if has routine then check the input box
									if(selectTimes.isChecked())
										taskReminder.setRepeatingTimes(Integer.parseInt(routine_times.getSelectedItem().toString()));
									
									else if(selectDays.isChecked())
										taskReminder.setRepeatingDays(Integer.parseInt(routine_days.getSelectedItem().toString()));
									
								}
							
							if(start_date.getTime()!= 0)taskReminder.setStartTime(start_date.getTime());
							
							if(end_date.getTime()!= 0) taskReminder.setEndTime(end_date.getTime());
							
							// INSERT THE NEW REMINDER
							DBAccessImpl.getInstance(getApplicationContext()).InsertReminder(taskReminder);
						
						}else{
							
							Toast.makeText(EditTaskActivity.this, "End Date has to be after Start Date.", Toast.LENGTH_SHORT).show();
							
							}
							
						}else{
						
						// NO REMINDE JUST INSERT THE TASK
						DBAccessImpl.getInstance(getApplicationContext()).InsertTask(addedTask);
						
						}
					}else{
						Toast.makeText(EditTaskActivity.this, "Please enter the task title.", Toast.LENGTH_SHORT).show();
						}
					
						// will close this activity and lauch main activity
						Intent resultIntent = new Intent(EditTaskActivity.this, MainActivity.class);
						setResult(Activity.RESULT_OK, resultIntent);
						
						// close this activity
						finish();
					
				
					}catch(Exception e){
						
						//Failed to add
						// will close this activity and lauch main activity
						Intent resultIntent = new Intent(EditTaskActivity.this, MainActivity.class);
						
						// close this activity
						finish();
					}
					
				// HANDLE THE EDIT TASK ACTIVITY
				}else {
					
					
					// Try Catch to handle exception
					try{
						
						Task updatedTask = DBAccessImpl.getInstance(getApplicationContext()).describeTask(passed_tid);
						
						String taskTitleText = task_title.getText().toString();
						
					//error control for task title
					if(taskTitleText.length()>1){
						
						//setting the task title
						updatedTask.setName(taskTitleText);
						
						String taskType = task_type.getSelectedItem().toString();
						
						int typeID = 1;
						
						//setting the task type
						if(taskType.equals("General"))typeID = 1;
						if(taskType.equals("Medication"))typeID = 2;
						if(taskType.equals("Appointment/Contact"))typeID = 3;
						
						updatedTask.setTaskType(typeID);
						updatedTask.setNotes(notes_edit.getText().toString());
						
						//CHECK IF HAS REMIND
						Reminder testRmd = DBAccessImpl.getInstance(getApplicationContext()).getReminderByTid(passed_tid);
						
						Log.v("debugtag",testRmd.toString());
						Log.v("debugtag",updatedTask.toString());
						
						if(testRmd!=null)isRemind = true;else {isRemind = false;}
						
						//there is a remind
						if(isRemind){
							
							if(end_date.after(start_date)){
								
								// insert the task to database 
								DBAccessImpl.getInstance(getApplicationContext()).UpdateTask(updatedTask);
								
								// THE FOLLOWING GET'S PARAMETERS FOR THE NEW REMIND
								Reminder taskReminder = DBAccessImpl.getInstance(getApplicationContext()).getReminderByTid(passed_tid);
								
								int routineInt = (isRoutine) ? 1 : 0;
										
								taskReminder.setIsRoutine(routineInt);
								
									//IF SET ROUTINE, THEN UPDATE REMINDER WITH TIMES AND DAYS
								
									if(isRoutine){
										
										//if has routine then check the input box
										if(selectTimes.isChecked())
											taskReminder.setRepeatingTimes(Integer.parseInt(routine_times.getSelectedItem().toString()));
										
										else if(selectDays.isChecked())
											taskReminder.setRepeatingDays(Integer.parseInt(routine_days.getSelectedItem().toString()));
										
									}
								
								if(start_date.getTime()!= 0)taskReminder.setStartTime(start_date.getTime());
								
								if(end_date.getTime()!= 0) taskReminder.setEndTime(end_date.getTime());
								
								// INSERT THE NEW REMINDER
								DBAccessImpl.getInstance(getApplicationContext()).InsertReminder(taskReminder);
							
							}else{
								
								Toast.makeText(EditTaskActivity.this, "End Date has to be after Start Date.", Toast.LENGTH_SHORT).show();
								
								}
								
							}else{
							
								// insert the task to database 
								DBAccessImpl.getInstance(getApplicationContext()).UpdateTask(updatedTask);
							
							}
						}else{
							Toast.makeText(EditTaskActivity.this, "Please enter the task title.", Toast.LENGTH_SHORT).show();
							}
						
								// will close this activity and lauch main activity
								Intent resultIntent = new Intent(EditTaskActivity.this, MainActivity.class);
								setResult(Activity.RESULT_OK, resultIntent);
								
								// close this activity
								finish();
						
						}catch(Exception e){
							
							//Failed to add
							// will close this activity and lauch main activity
							Intent resultIntent = new Intent(EditTaskActivity.this, MainActivity.class);
							
							// close this activity
							finish();
						}
					
				}
				
				
			}
		});
        
        
        //listener for the check box to enable remind
        remind_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {

            		if(isChecked){remind_area.setVisibility(View.VISIBLE);isRemind = true;}
            		else {remind_area.setVisibility(View.GONE);isRemind = false;}
            	
            	
                // onCheckedChanged implementation
            }
        });
        
        //listener for the check box to enable remind
        routine_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {

            		if(isChecked){routine_area.setVisibility(View.VISIBLE);isRoutine = true;}
            		else {routine_area.setVisibility(View.GONE);isRoutine = false;}
            	
                // onCheckedChanged implementation
            }
        });
        
        start_date_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				dateTimePicker = new DateTimePicker(EditTaskActivity.this, EditTaskActivity.this);
			    dateTimePicker.set24HourFormat(true);
				dateTimePicker.showDialog();
				which_date_setting = "start";
				
			}
		});
        
        
        end_date_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				dateTimePicker = new DateTimePicker(EditTaskActivity.this, EditTaskActivity.this);
			    dateTimePicker.set24HourFormat(true);
				dateTimePicker.showDialog();
		        which_date_setting = "end";
				
			}
		});
        
    }

	@Override
	public void onSet(Calendar calendarSelected, Date dateSelected, int year,
			String monthFullName, String monthShortName, int monthNumber,
			int date, String weekDayFullName, String weekDayShortName,
			int hour24, int hour12, int min, int sec, String AM_PM) {
			// TODO Auto-generated method stub
			
			//setting the start date
			if(which_date_setting.equals("start")){
				
				start_date_button.setText(DateFormat.format("yyyy-MM-dd hh:mm a",dateSelected));
				start_date = dateSelected;
				
			// else if setting the end date
			}else if(which_date_setting.equals("end")){
				
				end_date_button.setText(DateFormat.format("yyyy-MM-dd hh:mm a",dateSelected));
				end_date = dateSelected;
				
			}
	}
	
	
	/**
	 * Return date in specified format.
	 * @param milliSeconds Date in milliseconds
	 * @param dateFormat Date format 
	 * @return String representing date in specified format
	 */
	public static String getDate(long milliSeconds, String dateFormat)
	{
	    // Create a DateFormatter object for displaying date in specified format.
	    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

	    // Create a calendar object that will convert the date and time value in milliseconds to date. 
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTimeInMillis(milliSeconds);
	     return formatter.format(calendar.getTime());
	}
	
	

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}


}
