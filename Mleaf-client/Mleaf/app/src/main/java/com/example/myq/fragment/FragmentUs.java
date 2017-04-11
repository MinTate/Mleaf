package com.example.myq.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myq.mleaf.R;

/**
 * 关于我们页面
 * @author MYQ
 *
 */
public class FragmentUs extends Fragment {
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_us, container, false);
    	return view;
    }
}
