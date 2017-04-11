package com.example.myq.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.myq.mleaf.MainActivity;
import com.example.myq.mleaf.R;
import com.example.myq.mleaf.RoundedImageView;

/**
 * 主要控制左边按钮点击事件
 * @author Administrator
 *
 */
public class LeftSlidingMenuFragment extends Fragment implements OnClickListener{
	private View functionBtnLayout;
	private View usBtnLayout;
	private View settingBtnLayout;
	private View helpBtnLayout;
	private RoundedImageView roundedImageView;
     @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    }
     
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	 View view = inflater.inflate(R.layout.main_left_fragment, container,false);
		 functionBtnLayout = view.findViewById(R.id.functionBtnLayout);
		 functionBtnLayout.setOnClickListener(this);
		 usBtnLayout = view.findViewById(R.id.usBtnLayout);
		 usBtnLayout.setOnClickListener(this);
		 helpBtnLayout = view.findViewById(R.id.helpBtnLayout);
		 helpBtnLayout.setOnClickListener(this);
		 settingBtnLayout = view.findViewById(R.id.settingBtnLayout);
		 settingBtnLayout.setOnClickListener(this);
		 roundedImageView = (RoundedImageView)view.findViewById(R.id.headImageView);
		 roundedImageView.setOnClickListener(this);
		 return view;
    }

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		switch (v.getId()) {
			case R.id.functionBtnLayout:
				newContent = new FragmentDefaultMain();
				functionBtnLayout.setSelected(true);
				usBtnLayout.setSelected(false);
				helpBtnLayout.setSelected(false);
				settingBtnLayout.setSelected(false);
				break;
			case R.id.usBtnLayout:
				newContent = new FragmentUs();
				functionBtnLayout.setSelected(false);
				usBtnLayout.setSelected(true);
				helpBtnLayout.setSelected(false);
				settingBtnLayout.setSelected(false);
				break;
			case R.id.helpBtnLayout:
				newContent = new FragmentHelp();
				functionBtnLayout.setSelected(false);
				usBtnLayout.setSelected(false);
				helpBtnLayout.setSelected(true);
				settingBtnLayout.setSelected(false);
				break;
			case R.id.settingBtnLayout:
				newContent = new FragmentSetting();
				functionBtnLayout.setSelected(false);
				usBtnLayout.setSelected(false);
				helpBtnLayout.setSelected(false);
				settingBtnLayout.setSelected(true);
				break;
			default:
				break;
		}
		if (newContent != null)
			switchFragment(newContent);
		
	}
	
	
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		
			MainActivity ra = (MainActivity) getActivity();
			ra.switchContent(fragment);
		
	}
}
