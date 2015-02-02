/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.outpatient.fragment.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.outpatient.R;
import com.outpatient.fragment.infopage.ExpandableListItem;
import com.outpatient.fragment.infopage.ExpandingLayout;
import com.outpatient.storeCat.model.Info;
import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.model.Task;
import com.outpatient.storeCat.service.DBAccessImpl;
import com.outpatient.sysUtil.model.GlobalVar;

/**
 * This is a custom array adapter used to populate the listview whose items will
 * expand to display extra content in addition to the default display.
 */
public class InfoListAdapter extends ArrayAdapter<ExpandableListItem> {

    private final int CELL_DEFAULT_HEIGHT = 100;
    private List<ExpandableListItem> mData;
    private int mLayoutViewResourceId=R.layout.info_cellitem;
    private final Context context;

    public InfoListAdapter(Context context, int layoutViewResourceId, List<ExpandableListItem> data) {
        super(context, layoutViewResourceId, data);
        this.context = context;
        mData = data;
        mLayoutViewResourceId = layoutViewResourceId;
    }
    
    public InfoListAdapter(Context context)
    {
    	super(context, R.layout.info_cellitem);
    	this.context = context;
    	mData = this.generateData();
    	this.addAll(mData);
    	
    }
    
    public void refreshInfoList(){
    	mData = this.generateData();
    	this.clear();
 		this.addAll(mData);
 		this.notifyDataSetChanged();
    }
    
    private ArrayList<ExpandableListItem> generateData(){
    	
    	ArrayList<Plan> currentPlans= (ArrayList<Plan>) DBAccessImpl.getInstance(context).queryShowPlanList();
    	
    	ArrayList<Info> infoToShow = new ArrayList<Info>();
    	
    	ArrayList<ExpandableListItem> infoList = new ArrayList<ExpandableListItem>();
        
    	for (int i = 0; i < currentPlans.size() ; i ++){
    		
    		if(GlobalVar.plan_info_list.get(currentPlans.get(i).getPid())!=null)
    		infoToShow.addAll(GlobalVar.plan_info_list.get(currentPlans.get(i).getPid()));
    	}
    	
    	
    	for(int i = 0; i < infoToShow.size(); i++){
    		
    		infoList.add(new ExpandableListItem(infoToShow.get(i).getQue(),CELL_DEFAULT_HEIGHT, infoToShow.get(i).getAns()));
    		
    	}
    	return infoList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ExpandableListItem object = mData.get(position);

        if(convertView == null) {
        	LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(mLayoutViewResourceId, parent, false);
        }

        LinearLayout linearLayout = (LinearLayout)(convertView.findViewById(
                R.id.item_linear_layout));
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams
                (AbsListView.LayoutParams.MATCH_PARENT, object.getCollapsedHeight());
        linearLayout.setLayoutParams(linearLayoutParams);

        TextView titleView = (TextView)convertView.findViewById(R.id.title_view);
        TextView textView = (TextView)convertView.findViewById(R.id.text_view);

        titleView.setText(object.getTitle());
        textView.setText(object.getText());

        convertView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT));

        ExpandingLayout expandingLayout = (ExpandingLayout)convertView.findViewById(R.id.expanding_layout);
        expandingLayout.setExpandedHeight(object.getExpandedHeight());
        expandingLayout.setSizeChangedListener(object);

        if (!object.isExpanded()) {
            expandingLayout.setVisibility(View.GONE);
        } else {
            expandingLayout.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    /**
     * Crops a circle out of the thumbnail photo.
     */
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Config.ARGB_8888);

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);

        int halfWidth = bitmap.getWidth()/2;
        int halfHeight = bitmap.getHeight()/2;

        canvas.drawCircle(halfWidth, halfHeight, Math.max(halfWidth, halfHeight), paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


}