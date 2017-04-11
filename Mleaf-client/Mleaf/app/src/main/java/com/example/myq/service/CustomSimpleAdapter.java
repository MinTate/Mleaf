package com.example.myq.service;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.example.myq.mleaf.R;

import java.util.List;
import java.util.Map;
/**
 * 适配器
 * @author MYQ
 * @time 2016/3/7.
 */

public class CustomSimpleAdapter extends SimpleAdapter {
	public CustomSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to){
		super(context, data, resource, from, to);
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		View v = super.getView(position, convertView, parent);
		//更新第一个TextView的背景
		
		if(position == 0){
			TextView categoryTitle = (TextView) v;
			categoryTitle.setBackgroundResource(R.drawable.categorybar_item_background);
			categoryTitle.setTextColor(0xffffffff);
		}
		return v;
	}
	
	
}
