package com.example.outpatient;

import java.util.Calendar;
import java.util.Date;

import com.example.outpatient.DateTimePicker.ICustomDateTimeListener;

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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditTaskActivity extends Activity implements ICustomDateTimeListener{

	private DateTimePicker dateTimePicker;
	private Button confirmBtn;
	
	//reference to the elements in order of appearance
	private TextView task_title;
	private TextView task_description;
	private CheckBox remind_check;
	private ScrollView remind_area;
	private Button start_date;
	private CheckBox routine_check;
	private LinearLayout routine_area;
	private Spinner routine_times;
	private Spinner routine_days;
	private Button end_date;
	private RadioButton selectTimes;
	private RadioButton selectDays;
	
	
	private boolean isRemind = false;
	private boolean isRoutine = false;
	
	//these are the data read from the user input ui
	private String start_date_text;
	private String end_date_text;
	private String which_date_setting;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);
        
        confirmBtn = (Button)findViewById(R.id.confirm_button);
        
        task_title = (TextView)findViewById(R.id.task_title);
        task_description = (TextView)findViewById(R.id.task_description);
        remind_check = (CheckBox)findViewById(R.id.remind_check);
        remind_area = (ScrollView)findViewById(R.id.remind_area_scroll);
        start_date = (Button)findViewById(R.id.start_date);
        routine_check = (CheckBox)findViewById(R.id.routine_check);
        routine_area = (LinearLayout)findViewById(R.id.routine_area);
        routine_times = (Spinner)findViewById(R.id.routine_times);
        routine_days = (Spinner)findViewById(R.id.routine_days);
        end_date = (Button)findViewById(R.id.end_date);
        selectTimes = (RadioButton)findViewById(R.id.radiobutton_daily);
        selectDays = (RadioButton)findViewById(R.id.radiobutton_repeatday);
        
        
	    // Create an ArrayAdapter using the string array and a default spinner layout
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	            R.array.routine_list, android.R.layout.simple_spinner_item);
	    
	    // Specify the layout to use when the list of choices appears
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    
	    // Apply the adapter to the spinner
	    routine_times.setAdapter(adapter);
	    
	    // Apply the adapter to the spinner
	    routine_days.setAdapter(adapter);
	    
        remind_area.setVisibility(View.GONE);
        routine_area.setVisibility(View.GONE);
       
        
        confirmBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				// will close this activity and lauch main activity
				Intent resultIntent = new Intent(EditTaskActivity.this, MainActivity.class);
				setResult(Activity.RESULT_OK, resultIntent);
				
				// close this activity
				finish();
				
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
        
        start_date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				dateTimePicker = new DateTimePicker(EditTaskActivity.this, EditTaskActivity.this);
			    dateTimePicker.set24HourFormat(true);
				dateTimePicker.showDialog();
				which_date_setting = "start";
				
			}
		});
        
        
        end_date.setOnClickListener(new View.OnClickListener() {
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
				
				start_date.setText(DateFormat.format("yyyy-MM-dd hh:mm",dateSelected));

				
			// else if setting the end date
			}else if(which_date_setting.equals("end")){
				
				end_date.setText(dateSelected.toString());
				
			}
			
		
		
		
		
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}


}
