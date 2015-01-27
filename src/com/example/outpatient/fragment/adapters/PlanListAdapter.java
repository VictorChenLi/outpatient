package com.example.outpatient.fragment.adapters;

import java.util.ArrayList;

import com.example.outpatient.R;
import com.example.outpatient.R.id;
import com.example.outpatient.R.layout;
import com.outpatient.storeCat.model.Plan;
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

public class PlanListAdapter extends ArrayAdapter<Plan> {

	private final Context context;
	private ArrayList<Plan> planArrayList;
	private DBAccessImpl dbhandler;
	private LayoutInflater inflater;
	private SparseBooleanArray mSelectedItemsIds;

	public PlanListAdapter(Context context, ArrayList<Plan> itemsArrayList) {

		super(context, R.layout.plan_item, itemsArrayList);

		this.context = context;
		this.planArrayList = itemsArrayList;

		mSelectedItemsIds = new SparseBooleanArray();
		dbhandler = DBAccessImpl.getInstance(context);
	}

	public PlanListAdapter(Context context) {
		super(context, R.layout.plan_item);

		this.context = context;
		this.planArrayList = this.generateData();
	}

	public void refreshPlanList(ArrayList<Plan> newList) {
		this.planArrayList = newList;
		this.clear();
		this.addAll(planArrayList);
		this.notifyDataSetChanged();
	}

	public void refreshPlanList() {
		this.planArrayList = this.generateData();
		this.clear();
		this.addAll(planArrayList);
		this.notifyDataSetChanged();
	}

	private ArrayList<Plan> generateData() {
		ArrayList<Plan> planList;
		DBAccessImpl dbAccessImpl = DBAccessImpl.getInstance(context);
		planList = (ArrayList<Plan>) dbAccessImpl.queryShowPlanList();

		return planList;
	}

	private class ViewHolder {
		TextView tv_planName;
		TextView tv_planId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {

			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.plan_item, parent, false);

			// save the holder at view
			holder.tv_planName = (TextView) convertView
					.findViewById(R.id.plan_name);
			holder.tv_planId = (TextView) convertView
					.findViewById(R.id.plan_id);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// Set the text for textView
		holder.tv_planName.setText(planArrayList.get(position).getName());
		holder.tv_planId.setText(String.valueOf(planArrayList.get(position)
				.getPid()));

		// 5. return rowView
		return convertView;
	}

	@Override
	public void remove(Plan object) {
		planArrayList.remove(object);
		dbhandler.deletPlan(object.getPid());
		notifyDataSetChanged();
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