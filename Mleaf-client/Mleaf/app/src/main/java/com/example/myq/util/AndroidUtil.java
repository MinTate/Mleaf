package com.example.myq.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class AndroidUtil {

	// 返回一个列表对话框
	public static AlertDialog.Builder getListDialogBuilder(Context context,
			String[] items, String title, OnClickListener clickListener) {
		return new AlertDialog.Builder(context).setTitle(title).setItems(items, clickListener);
		
	}
}
