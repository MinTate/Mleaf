package com.example.myq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myq.mleaf.LeafActivity;
import com.example.myq.mleaf.NewsActivity;
import com.example.myq.mleaf.R;
import com.example.myq.mleaf.VerifyActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 易信页面
 * @author Administrator
 *
 */
public class FragmentDefaultMain extends Fragment implements OnClickListener{
	private ViewPager mViewPaper;
	private List<ImageView> images;
	private List<View> dots;
	private int currentItem;
	//记录上一次点的位置
	private int oldPosition = 0;
	//存放图片的id
	private int[] imageIds = new int[]{
			R.drawable.a,
			R.drawable.b,
			R.drawable.c,
			R.drawable.d,
	};
	//存放图片的标题
	private String[]  titles = new String[]{
			"红枫",
			"芋头",
			"葡萄",
			"银杏",
	};
	private TextView title;
	private Button news,leaf,flower,cloud;
	private ViewPagerAdapter adapter;
	private ScheduledExecutorService scheduledExecutorService;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
		/*加载界面*/
    	View view = inflater.inflate(R.layout.fragment_function, container, false);

		/*获取控件*/
		mViewPaper = (ViewPager) view.findViewById(R.id.vp);
		news=(Button)view.findViewById(R.id.news);
		leaf=(Button)view.findViewById(R.id.leaf);
		flower=(Button)view.findViewById(R.id.flower);
		cloud=(Button)view.findViewById(R.id.cloud);


		/*显示的图片*/
		images = new ArrayList<ImageView>();
		for(int i = 0; i < imageIds.length; i++){
			ImageView imageView = new ImageView(getActivity());
			imageView.setBackgroundResource(imageIds[i]);
			images.add(imageView);
		}
		/*显示的小点*/
		dots = new ArrayList<View>();
		dots.add(view.findViewById(R.id.dot_0));
		dots.add(view.findViewById(R.id.dot_1));
		dots.add(view.findViewById(R.id.dot_2));
		dots.add(view.findViewById(R.id.dot_3));

		/*获取标题控件 并初始化*/
		title = (TextView) view.findViewById(R.id.title);
		title.setText(titles[0]);

		adapter = new ViewPagerAdapter();
		mViewPaper.setAdapter(adapter);

		mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				title.setText(titles[position]);
				dots.get(position).setBackgroundResource(R.drawable.dot_focused);
				dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);

				oldPosition = position;
				currentItem = position;
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});

		/*添加按钮点击事件*/
		news.setOnClickListener(this);
		leaf.setOnClickListener(this);
		flower.setOnClickListener(this);
		cloud.setOnClickListener(this);
		return view;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.news:
				Intent newsItn = new Intent(getActivity(), NewsActivity.class);
				startActivity(newsItn);
				break;
			case R.id.leaf:
				Intent leafItn = new Intent(getActivity(), LeafActivity.class);
				startActivity(leafItn);
				break;
			case R.id.flower:

				break;
			case R.id.cloud:

				break;
			default:
				break;
		}
	}


	public   void onActivityResult(int requestCode, int resultCode, Intent data) {

		
	}
	/**
	 * 自定义Adapter
	 * @author MYQ
	 *
	 */
	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			// TODO Auto-generated method stub
//			super.destroyItem(container, position, object);
//			view.removeView(view.getChildAt(position));
//			view.removeViewAt(position);
			view.removeView(images.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			// TODO Auto-generated method stub
			view.addView(images.get(position));
			return images.get(position);
		}

	}
	/**
	 * 利用线程池定时执行动画轮播
	 */
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleWithFixedDelay(
				new ViewPageTask(),
				2,
				2,
				TimeUnit.SECONDS);
	}


	private class ViewPageTask implements Runnable{

		@Override
		public void run() {
			currentItem = (currentItem + 1) % imageIds.length;
			mHandler.sendEmptyMessage(0);
		}
	}

	/**
	 * 接收子线程传递过来的数据
	 */
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			mViewPaper.setCurrentItem(currentItem);
		};
	};
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
