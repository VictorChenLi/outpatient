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

package com.outpatient.fragment.infopage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ExpandingLayout extends RelativeLayout {


    private OnSizeChangedListener mSizeChangedListener;
    private int mExpandedHeight = -1;

    public ExpandingLayout(Context context) {
        super(context);
    }

    public ExpandingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        if (mExpandedHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mExpandedHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        mExpandedHeight = h;
        //Notifies the list data object corresponding to this layout that its size has changed.
        mSizeChangedListener.onSizeChanged(h);
    }

    public int getExpandedHeight() {
        return mExpandedHeight;
    }

    public void setExpandedHeight(int expandedHeight) {
        mExpandedHeight = expandedHeight;
    }

    public void setSizeChangedListener(OnSizeChangedListener listener) {
        mSizeChangedListener = listener;
    }
}
