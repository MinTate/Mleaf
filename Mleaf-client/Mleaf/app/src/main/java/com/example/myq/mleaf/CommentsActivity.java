package com.example.myq.mleaf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 *@author coolszy
 *@date 2012-3-22
 *@blog http://blog.92coding.com
 */
public class CommentsActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comments);

		List<HashMap<String, String>> comments = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 10; i++)
		{
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("commentator_from", "连云港朋友");
			hashMap.put("comment_ptime", "2012-03-22 20:21:22");
			hashMap.put("comment_content", "这样视频真的太好的！");
			comments.add(hashMap);
		}
		SimpleAdapter commentsAdapter = new SimpleAdapter(this, comments, R.layout.comments_list_item, new String[]
		{ "commentator_from", "comment_ptime", "comment_content" }, new int[]
		{ R.id.commentator_from, R.id.comment_ptime, R.id.comment_content });

		ListView commentsList = (ListView) findViewById(R.id.comments_list);
		commentsList.setAdapter(commentsAdapter);
	}
}
