package com.example.myq.mleaf;

import java.io.Serializable;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import com.example.myq.service.SyncHttp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class NewsDetailsActivity extends Activity {
	
	private final int FINISH = 0;
	
	private ViewFlipper mNewsBodyFlipper;
	private LayoutInflater mNewsBodyInflater;
	int count;
	private int mPosition = 0;
	private int mNid;
	private ArrayList<HashMap<String, Object>> mNewsData;
	private TextView mNewsDetails;
	private float mStartX;
	//private Button mNewsdetailsTitlebarComm;// 新闻回复数
	private int mCursor;
	
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			System.out.println("Handler:" + Thread.currentThread().getId());
			switch (msg.arg1)
			{
			case FINISH:
				// 把获取到的新闻显示到界面上
				mNewsDetails.setText(msg.obj.toString());
				break;
			}
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newsdetails);
		
		NewsDetailsTitleBarOnClickListener newsDetailsTitleBarOnClickListener = new NewsDetailsTitleBarOnClickListener();
		//上一篇新闻
		Button newsDetailsTitlebarPref = (Button) findViewById(R.id.newsdetails_titlebar_previous);
		newsDetailsTitlebarPref.setOnClickListener(newsDetailsTitleBarOnClickListener);
		// 下一篇新闻
		Button newsDetailsTitlebarNext = (Button) findViewById(R.id.newsdetails_titlebar_next);
		newsDetailsTitlebarNext.setOnClickListener(newsDetailsTitleBarOnClickListener);
		//新闻回复Button
		//mNewsdetailsTitlebarComm = (Button) findViewById(R.id.newsdetails_titlebar_comments);
		//mNewsdetailsTitlebarComm.setOnClickListener(newsDetailsTitleBarOnClickListener);
		//获取传递的数据
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 设置标题栏名称
		String categoryName = bundle.getString("categoryName");
		TextView titleBarTitle = (TextView) findViewById(R.id.newsdetails_titlebar_title);
		titleBarTitle.setText(categoryName);
		// 获取点击位置
		mCursor = mPosition = bundle.getInt("position");
		// 获取新闻集合
		Serializable s = bundle.getSerializable("newsDate");
		mNewsData = (ArrayList<HashMap<String, Object>>) s;
		// 动态创建新闻视图，并赋值
		mNewsBodyInflater = getLayoutInflater();
		inflateView(0);
	}
	
	
	private void showPrevious(){
		if (mPosition>0){
			mPosition--;
			//记录当前新闻编号
			HashMap<String, Object> hashMap = mNewsData.get(mPosition);
			mNid = (Integer) hashMap.get("nid");
			if (mCursor > mPosition){
				mCursor = mPosition;
				inflateView(0);
				System.out.println(mNewsBodyFlipper.getChildCount());
				mNewsBodyFlipper.showNext();// 显示下一页
			}
			mNewsBodyFlipper.setInAnimation(this, R.anim.push_right_in);// 定义下一页进来时的动画
			mNewsBodyFlipper.setOutAnimation(this, R.anim.push_right_out);// 定义当前页出去的动画
			mNewsBodyFlipper.showPrevious();// 显示上一页
		}
		else{
			Toast.makeText(this, R.string.no_pre_news, Toast.LENGTH_SHORT).show();
		}
		System.out.println(mCursor +";"+mPosition);
	}
		
	private void showNext(){
		if (mPosition<mNewsData.size()-1){
			//设置下一屏动画
			mNewsBodyFlipper.setInAnimation(this, R.anim.push_left_in);
			mNewsBodyFlipper.setOutAnimation(this, R.anim.push_left_out);
			mPosition++;
			if (mPosition >= mNewsBodyFlipper.getChildCount()){
				inflateView(mNewsBodyFlipper.getChildCount());
			}
            // 显示下一屏
			mNewsBodyFlipper.showNext();
		}
		else{
			Toast.makeText(this, R.string.no_next_news, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 处理NewsDetailsTitleBar点击事件
	 */
	class NewsDetailsTitleBarOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v){
			switch (v.getId()){
			// 上一条新闻
			case R.id.newsdetails_titlebar_previous:
				showPrevious();
				break;
			// 下一条新闻
			case R.id.newsdetails_titlebar_next:
				showNext();
				break;
			case R.id.newsdetails_titlebar_comments:
				Intent intent = new Intent(NewsDetailsActivity.this, CommentsActivity.class);
				startActivity(intent);
				break;
			}

		}
	}
	
	private OnTouchListener  newsBodyOnTouchListener = new OnTouchListener (){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()){
			// 手指按下
			case MotionEvent.ACTION_DOWN:
				// 记录起始坐标
				mStartX = event.getX();
				break;
			// 手指抬起
			case MotionEvent.ACTION_UP:
				// 往左滑动
				if (event.getX() < mStartX){
					showNext();
					}
				// 往右滑动
				else if (event.getX() > mStartX){
					showPrevious();
					}
				break;
			}
			return true;
		}
	};
	
	private void inflateView(int index)
	{
		// 动态创建新闻视图，并赋值
		View newsBodyLayout = mNewsBodyInflater.inflate(R.layout.news_body, null);
		// 获取点击新闻基本信息
		HashMap<String, Object> hashMap = mNewsData.get(mPosition);
		// 新闻标题
		TextView newsTitle = (TextView) newsBodyLayout.findViewById(R.id.news_body_title);
		newsTitle.setText(hashMap.get("newslist_item_title").toString());
		// 发布时间和出处
		TextView newsPtimeAndSource = (TextView) newsBodyLayout.findViewById(R.id.news_body_ptime_source);
		newsPtimeAndSource.setText(hashMap.get("newslist_item_ptime").toString() + "    " + hashMap.get("newslist_item_source").toString());
		// 新闻编号
		mNid = (Integer) hashMap.get("nid");
		// 新闻回复数
		//mNewsdetailsTitlebarComm.setText(hashMap.get("newslist_item_comments") + "跟帖");
	
		// 把新闻视图添加到Flipper中
		mNewsBodyFlipper = (ViewFlipper) findViewById(R.id.news_body_flipper);
		mNewsBodyFlipper.addView(newsBodyLayout,index);
	
		// 给新闻Body添加触摸事件
		mNewsDetails = (TextView) newsBodyLayout.findViewById(R.id.news_body_details);
		mNewsDetails.setOnTouchListener(newsBodyOnTouchListener);
	
		// 启动线程
		new UpdateNewsThread().start();
	}
	
	private class UpdateNewsThread extends Thread
	{
		@Override
		public void run()
		{
			System.out.println("Thread:" + Thread.currentThread().getId());
			// 从网络上获取新闻
			String newsBody = getNewsBody();
			Message msg = mHandler.obtainMessage();
			msg.arg1 = FINISH;
			msg.obj = newsBody;
			mHandler.sendMessage(msg);
		}
	}
	
	
	/**
	 * 获取新闻详细信息
	 * 
	 * @return
	 */
	private String getNewsBody()
	{
		String retStr = "网络连接失败，请稍后再试";
		SyncHttp syncHttp = new SyncHttp();
		String url = "http://111.114.116.28:8080/web/getNews";
		String params = "nid=" + mNid;
		try
		{
			String retString = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(retString);
			// 获取返回码，0表示成功
			int retCode = jsonObject.getInt("ret");
			if (0 == retCode)
			{
				JSONObject dataObject = jsonObject.getJSONObject("data");
				JSONObject newsObject = dataObject.getJSONObject("news");
				retStr = newsObject.getString("body");
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return retStr;
	}
}
