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

package com.example.outpatient.infopage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.example.outpatient.R;
import com.outpatient.storeCat.model.Info;
import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.service.DBAccessImpl;
import com.outpatient.sysUtil.model.GlobalVar;

/**
 * This activity creates a listview whose items can be clicked to expand and show
 * additional content.
 *
 * In this specific demo, each item in a listview displays an image and a corresponding
 * title. These two items are centered in the default (collapsed) state of the listview's
 * item. When the item is clicked, it expands to display text of some varying length.
 * The item persists in this expanded state (even if the user scrolls away and then scrolls
 * back to the same location) until it is clicked again, at which point the cell collapses
 * back to its default state.
 */
public class InfoExpandingFragment extends Fragment {

    private final int CELL_DEFAULT_HEIGHT = 100;

	// shared preference used to retrieve app status
	private SharedPreferences appinfo = null;
	
	private CustomArrayAdapter infoListAdapter; 
	private ArrayList<Info> infoList;
	private ListView info_listview;
    private List<ExpandableListItem> mData;
    private ExpandingListView mListView;
    

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.info_listfragment, container, false);
        
        generateData();
        
        infoListAdapter = new CustomArrayAdapter(getActivity(), R.layout.info_cellitem, mData);
        
        mListView = (ExpandingListView)rootView.findViewById(R.id.main_list_view);
        mListView.setAdapter(infoListAdapter);
        mListView.setDivider(null);
        
        return rootView;
    }
    
    
    private void generateData(){
    	
    	ArrayList<Plan> currentPlans= (ArrayList<Plan>) DBAccessImpl.getInstance(getActivity()).queryShowPlanList();
    	
    	ArrayList<Info> infoToShow = new ArrayList<Info>();
    	
        mData = new ArrayList<ExpandableListItem>();
        
    	for (int i = 0; i < currentPlans.size() ; i ++){
    		
    		if(GlobalVar.plan_info_list.get(currentPlans.get(i).getPid())!=null)
    		infoToShow.addAll(GlobalVar.plan_info_list.get(currentPlans.get(i).getPid()));
    	}
    	
    	infoList = infoToShow;
    	
    	for(int i = 0; i < infoList.size(); i++){
    		
    		mData.add(new ExpandableListItem(infoList.get(i).getQue(),CELL_DEFAULT_HEIGHT, infoList.get(i).getAns()));
    		
    	}
    }
    
    
    
}