package com.example.outpatient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.model.Reminder;
import com.outpatient.storeCat.model.Task;
import com.outpatient.storeCat.service.DBAccessImpl;
import com.outpatient.sysUtil.service.DateTimePicker;
import com.outpatient.sysUtil.service.DateTimePicker.ICustomDateTimeListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditTaskActivity extends Activity implements
		ICustomDateTimeListener {

	private DateTimePicker dateTimePicker;
	private Button confirmBtn;

	// reference to the elements in order of appearance
	private EditText task_title;
	private EditText notes_edit;

	private TextView tv_interval;
	private TextView task_description;
	private CheckBox remind_check;
	private LinearLayout remind_area;
	private Button start_date_button;
	private CheckBox routine_check;
	private LinearLayout routine_area;

	private Spinner routine_frequency;
	private Spinner routine_interval;
	private Spinner plan_select;
	private Spinner task_type;

	private Button end_date_button;

	// private RadioButton selectTimes;
	// private RadioButton selectDays;
	//
	// private RadioGroup selectTimeDayGroup;

	// private NumberPicker frequencyPicker;
	// private NumberPicker intervalPicker;

	// holds information of the current editing task
	private Task editingTask;
	private Reminder editingTaskReminder;

	private boolean isRemind = false;
	private boolean isRoutine = false;

	private String which_date_setting;

	// stores the current date
	private Date start_date;
	private Date end_date;

	private int ReturnCode = 0;

	private int passed_tid = 0;
	private int pid = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_task);

		confirmBtn = (Button) findViewById(R.id.confirm_button);

		task_title = (EditText) findViewById(R.id.task_title);
		tv_interval = (TextView) findViewById(R.id.tv_interval);
		task_description = (TextView) findViewById(R.id.task_description);
		remind_check = (CheckBox) findViewById(R.id.remind_check);
		remind_area = (LinearLayout) findViewById(R.id.remind_area);
		start_date_button = (Button) findViewById(R.id.start_date);
		routine_check = (CheckBox) findViewById(R.id.routine_check);
		routine_area = (LinearLayout) findViewById(R.id.routine_area);

		plan_select = (Spinner) findViewById(R.id.plan_select);

		routine_frequency = (Spinner) findViewById(R.id.routine_frequency);
		routine_interval = (Spinner) findViewById(R.id.routine_interval);
		// frequencyPicker = (NumberPicker) findViewById(R.id.frequencyPicker);
		// intervalPicker = (NumberPicker) findViewById(R.id.intervalPicker);
		task_type = (Spinner) findViewById(R.id.task_type);

		end_date_button = (Button) findViewById(R.id.end_date);
		// selectTimes = (RadioButton) findViewById(R.id.radiobutton_daily);
		// selectDays = (RadioButton) findViewById(R.id.radiobutton_repeatday);
		// selectTimeDayGroup = (RadioGroup)
		// findViewById(R.id.selectTimesOrDaysGroup);

		notes_edit = (EditText) findViewById(R.id.notes_edit);

		if (getIntent().getExtras() != null)
			passed_tid = getIntent().getExtras().getInt("tid", 0);

		inital(passed_tid);
		addListener();

	}

	private void addListener() {
		// Radio button checked listener for Repeating TODO change to two picker
		routine_frequency
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						String frequency = routine_frequency.getItemAtPosition(
								position).toString();
						if (frequency.equals("Hourly"))
							tv_interval.setText("hour(s)");
						if (frequency.equals("Daily"))
							tv_interval.setText("day(s)");
						if (frequency.equals("Weekly"))
							tv_interval.setText("week(s)");
						if (null != editingTaskReminder) {
							editingTaskReminder.setRepeatingDays(position + 1);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}

				});

		routine_interval
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (null != editingTaskReminder) {
							editingTaskReminder.setRepeatingTimes(position + 1);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}

				});

		// selectTimeDayGroup
		// .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		// {
		//
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		// // TODO Auto-generated method stub
		//
		// // get selected radio button from radioGroup
		// int selectedId = selectTimeDayGroup
		// .getCheckedRadioButtonId();
		//
		// RadioButton selectedButton = (RadioButton) findViewById(selectedId);
		//
		// if (selectedButton != null) {
		//
		// if (selectedButton == selectTimes) {
		//
		// Log.v("debugtag", "clicked times");
		// routine_times.setEnabled(true);
		// routine_days.setEnabled(false);
		//
		// } else if (selectedButton == selectDays) {
		//
		// Log.v("debugtag", "clicked days");
		// routine_times.setEnabled(false);
		// routine_days.setEnabled(true);
		//
		// }
		//
		// } else {
		// Log.v("schoice", "NONE QUESTION");
		// }
		//
		// }
		//
		// });

		// listener for the check box to enable remind
		remind_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {
					remind_area.setVisibility(View.VISIBLE);
					isRemind = true;
				} else {
					remind_area.setVisibility(View.GONE);
					isRemind = false;
				}

			}
		});

		// TODO When the plan list done
		plan_select.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String planName = plan_select.getItemAtPosition(position)
						.toString();
				pid = DBAccessImpl.getInstance(getApplicationContext())
						.GetPlanByName(planName).getPid();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});

		confirmBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String taskTitleText = task_title.getText().toString();

				// error control for task title
				if (taskTitleText.trim().length() == 0) {
					Toast.makeText(EditTaskActivity.this,
							"Please enter the task title.", Toast.LENGTH_SHORT)
							.show();
				} else {

					// setting the task title
					editingTask.setName(taskTitleText);

					String taskType = task_type.getSelectedItem().toString();

					int typeID = 1;

					// setting the task type
					if (taskType.equals("General"))
						typeID = 1;
					if (taskType.equals("Medication"))
						typeID = 2;
					if (taskType.equals("Appointment/Contact"))
						typeID = 3;

					editingTask.setTaskType(typeID);
					editingTask.setNotes(notes_edit.getText().toString());
					editingTask.setPid(pid);

					// error control for task title
					if (taskTitleText.length() > 1) {

						// there is a remind
						if (isRemind) {

							if (end_date.after(start_date)) {

								if (0 == editingTask.getTid()) {
									// insert the task to database
									int new_tid = DBAccessImpl.getInstance(
											getApplicationContext())
											.InsertTask(editingTask);
									editingTask.setTid(new_tid);
								} else {
									// updating the task
									DBAccessImpl.getInstance(
											getApplicationContext())
											.UpdateTask(editingTask);
								}

								Reminder editingTaskReminder = new Reminder();
								// there is a remind
								if (isRemind) {

									if (!end_date.after(start_date)) {

										Toast.makeText(
												EditTaskActivity.this,
												"End Date has to be after Start Date.",
												Toast.LENGTH_SHORT).show();
									} else {
										// THE FOLLOWING GET'S PARAMETERS FOR
										// THE
										// NEW REMIND

										editingTaskReminder.setTid(editingTask
												.getTid());

										int routineInt = (isRoutine) ? 1 : 0;

										editingTaskReminder
												.setIsRoutine(routineInt);

										// IF SET ROUTINE, THEN UPDATE REMINDER
										// WITH
										// TIMES AND DAYS

										if (isRoutine) {

											// TODO change to two picker
											// taskReminder.setRepeatingDays(1);
											// taskReminder.setRepeatingTimes(1);
											// if (selectTimes.isChecked())
											// taskReminder.setRepeatingTimes(Integer
											// .parseInt(routine_times
											// .getSelectedItem()
											// .toString()));
											//
											// else if (selectDays.isChecked())
											// taskReminder.setRepeatingDays(Integer
											// .parseInt(routine_days
											// .getSelectedItem()
											// .toString()));

										}

										if (start_date.getTime() != 0)
											editingTaskReminder
													.setStartTime(start_date
															.getTime());

										if (end_date.getTime() != 0)
											editingTaskReminder
													.setEndTime(end_date
															.getTime());

									}

									// it means we create a new reminder
									if (0 == editingTaskReminder.getRid()) {
										// INSERT THE NEW REMINDER
										DBAccessImpl.getInstance(
												getApplicationContext())
												.InsertReminder(
														editingTaskReminder);
									} else {
										DBAccessImpl.getInstance(
												getApplicationContext())
												.UpdateReminder(
														editingTaskReminder);
									}

								} else
									Toast.makeText(
											EditTaskActivity.this,
											"End Date has to be after Start Date.",
											Toast.LENGTH_SHORT).show();

							}

						}

					} else
						Toast.makeText(EditTaskActivity.this,
								"Please enter the task title.",
								Toast.LENGTH_SHORT).show();

					Log.v("debugtag", "edited task");

					// will close this activity and lauch main activity
					Intent resultIntent = new Intent(EditTaskActivity.this,
							TaskFragment.class);
					setResult(Activity.RESULT_OK, resultIntent);

					// close this activity
					finish();

				}

			}
		});

		// listener for the check box to enable remind
		routine_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {
					routine_area.setVisibility(View.VISIBLE);
					isRoutine = true;
				} else {
					routine_area.setVisibility(View.GONE);
					isRoutine = false;
				}

				// onCheckedChanged implementation
			}
		});

		start_date_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				dateTimePicker = new DateTimePicker(EditTaskActivity.this,
						EditTaskActivity.this);
				dateTimePicker.set24HourFormat(true);
				dateTimePicker.showDialog();
				which_date_setting = "start";

			}
		});

		end_date_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				dateTimePicker = new DateTimePicker(EditTaskActivity.this,
						EditTaskActivity.this);
				dateTimePicker.set24HourFormat(true);
				dateTimePicker.showDialog();
				which_date_setting = "end";

			}
		});

	}

	// initial the entity and controller
	private void inital(int tid) {

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> frequency_adapter = ArrayAdapter
				.createFromResource(this, R.array.frequency_list,
						android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> interval_adapter = ArrayAdapter
				.createFromResource(this, R.array.interval_list,
						android.R.layout.simple_spinner_item);

		// Specify the layout to use when the list of choices appears
		frequency_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		interval_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		routine_frequency.setAdapter(frequency_adapter);

		// Apply the adapter to the spinner
		routine_interval.setAdapter(interval_adapter);

		remind_area.setVisibility(View.GONE);
		routine_area.setVisibility(View.GONE);

		// set the two spinners disabled first
		// routine_times.setEnabled(false);
		// routine_days.setEnabled(false);

		// SETTING UP THE TASK TYPE SPINNER

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> taskType_Adapter = ArrayAdapter
				.createFromResource(this, R.array.task_type_list,
						android.R.layout.simple_spinner_item);

		// Specify the layout to use when the list of choices appears
		taskType_Adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		task_type.setAdapter(taskType_Adapter);

		Log.v("debugtag", "tid opened:" + passed_tid);

		// if tid!=0 means we are updating
		if (0 != passed_tid) {
			ReturnCode = TaskFragment.EDIT_TASK_RESULT;
			editingTask = DBAccessImpl.getInstance(getApplicationContext())
					.describeTask(passed_tid);
			
		} else
			ReturnCode = TaskFragment.ADD_TASK_RESULT;

		if (editingTask != null) {
			// if updating, put the value into the controller
			pid = editingTask.getPid();
			
			Log.v("debugtag","retrieved= tid"+passed_tid);
			
			// SETTING UP THE PLAN SELECTOR by existing pid
			List<String> spinnerArray = new ArrayList<String>();

			List<Plan> currentPlan = DBAccessImpl.getInstance(
					getApplicationContext()).queryShowPlanList();

			int curPos = 0;
			for (int i = 0; i < currentPlan.size(); i++) {
				
				if (currentPlan.get(i).getPid() == pid)curPos = i;
				spinnerArray.add(currentPlan.get(i).getName());
				
			}
			
			// Important: getTaskType()-1 because it's kept 1,2,3 in database
			// while in the spinnerArray it's referenced by 0,1,2 
			task_type.setSelection(editingTask.getTaskType()-1);
			notes_edit.setText(editingTask.getNotes());
			
			ArrayAdapter<String> plan_spinner_adapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item, spinnerArray);
			
			plan_select.setAdapter(plan_spinner_adapter);
			plan_select.setSelection(curPos);

			// set name
			task_title.setText(editingTask.getName());

			// set description
			if (editingTask.getDes() != null && "" != editingTask.getDes())
				task_description.setText(editingTask.getDes());

			// Check if has reminder
			Reminder editingTaskReminder = DBAccessImpl.getInstance(
					getApplicationContext()).getReminderByTid(
					editingTask.getTid());

			// set reminder
			if (editingTaskReminder != null) {
				remind_check.setChecked(true);
				remind_area.setVisibility(View.VISIBLE);
				long sdate = editingTaskReminder.getStartTime();
				long edate = editingTaskReminder.getEndTime();

				// set startTime
				start_date_button.setText(getDate(sdate, "yyyy-MM-dd hh:mm a"));
				end_date_button.setText(getDate(edate, "yyyy-MM-dd hh:mm a"));
				routine_frequency.setSelection(editingTaskReminder
						.getRepeatingDays() - 1);
				routine_interval.setSelection(editingTaskReminder
						.getRepeatingTimes() - 1);

				int isRoutine = editingTaskReminder.getIsRoutine();

				// Check and set routine
				if (isRoutine == 1) {

					// check the routine check box
					routine_check.setChecked(true);
					routine_area.setVisibility(View.VISIBLE);

					// set repeating TODO change to two list
					// int xTimesDaily = taskReminder.getRepeatingTimes();
					// int onceXDays = taskReminder.getRepeatingTimes();
					//
					// if (xTimesDaily == 1) {
					// selectTimes.setSelected(true);
					// } else if (onceXDays == 1) {
					// selectDays.setSelected(true);
					// }

				} else {
					routine_check.setChecked(false);
					routine_area.setVisibility(View.GONE);
				}
			} else {
				editingTaskReminder = new Reminder();
				remind_check.setChecked(false);
				remind_area.setVisibility(View.GONE);
			}
		} else {
			// in this case means we creating task
			// and inital the editingTask
			editingTask = new Task();

		}
	}

	@Override
	public void onSet(Calendar calendarSelected, Date dateSelected, int year,
			String monthFullName, String monthShortName, int monthNumber,
			int date, String weekDayFullName, String weekDayShortName,
			int hour24, int hour12, int min, int sec, String AM_PM) {
		// TODO Auto-generated method stub

		// setting the start date
		if (which_date_setting.equals("start")) {

			start_date_button.setText(DateFormat.format("yyyy-MM-dd hh:mm a",
					dateSelected));
			start_date = dateSelected;

			// else if setting the end date
		} else if (which_date_setting.equals("end")) {

			end_date_button.setText(DateFormat.format("yyyy-MM-dd hh:mm a",
					dateSelected));
			end_date = dateSelected;

		}
	}

	/**
	 * Return date in specified format.
	 * 
	 * @param milliSeconds
	 *            Date in milliseconds
	 * @param dateFormat
	 *            Date format
	 * @return String representing date in specified format
	 */
	public static String getDate(long milliSeconds, String dateFormat) {
		// Create a DateFormatter object for displaying date in specified
		// format.
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

		// Create a calendar object that will convert the date and time value in
		// milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

}
