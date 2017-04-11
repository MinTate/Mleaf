package com.example.myq.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myq.mleaf.R;

/**
 * 朋友圈页面
 * @author Administrator
 *
 */
public class FragmentHelp extends Fragment {
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_help, container, false);
    	return view;
    }
}
