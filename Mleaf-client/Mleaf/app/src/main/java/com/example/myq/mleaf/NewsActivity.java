package com.example.myq.mleaf;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import com.example.myq.model.Category;
import com.example.myq.service.SyncHttp;
import com.example.myq.service.CustomSimpleAdapter;
import com.example.myq.util.DensityUtil;
import com.example.myq.util.StringUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NewsActivity extends Activity {
    private final int COLUMNWIDTHPX = 1080;
    private final int FLINGVELOCITYPX = 950;
    private final int NEWSCOUNT = 5; //返回新闻数目
    private final int SUCCESS = 0;//加载成功
	private final int NONEWS = 1;//该栏目下没有新闻
	private final int NOMORENEWS = 2;//该栏目下没有更多VEL新闻
	private final int LOADERROR = 3;//加载失败
    
	private int mColumnWidthDip;
    private int mFlingVelocityDip;
    private ArrayList<HashMap<String, Object>> mNewsData;
    private ListView mNewsList;
    private int mCid;
    private String mCatName;
    private SimpleAdapter mNewsListAdapter;
    private LayoutInflater mInflater;
	private Button mTitlebarRefresh;
	private ProgressBar mLoadnewsProgress;
	private Button mLoadMoreBtn;
	
	private LoadNewsAsyncTask loadNewsAsyncTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_main);
		
		mInflater = getLayoutInflater();
		
		mNewsData = new ArrayList<HashMap<String,Object>>();
		mNewsList = (ListView)findViewById(R.id.newslist);
		
		mTitlebarRefresh = (Button)findViewById(R.id.titlebar_refresh);
		mLoadnewsProgress = (ProgressBar)findViewById(R.id.loadnews_progress);
		mTitlebarRefresh.setOnClickListener(loadMoreListener);
		
		//把px转换成dip
		mColumnWidthDip = DensityUtil.px2dip(this, COLUMNWIDTHPX);
		mFlingVelocityDip = DensityUtil.px2dip(this, FLINGVELOCITYPX);
		
		
		//获取新闻分类
		String[] categoryArray = getResources().getStringArray(R.array.categories);
		//把新闻分类保存到List中
		final List<HashMap<String, Category>> categories = new ArrayList<HashMap<String, Category>>();
		//分割新闻类型字符串
		for(int i=0;i<categoryArray.length;i++)
		{
			String[] temp = categoryArray[i].split("[|]");
			if (temp.length==2)
			{
				int cid = StringUtil.String2Int(temp[0]);
				String title = temp[1];
				Category type = new Category(cid, title);
				HashMap<String, Category> hashMap = new HashMap<String, Category>();
				hashMap.put("category_title", type);
				categories.add(hashMap);
			}
		}
		
		//默认选中的新闻分类
        mCid = 1;
        mCatName ="关于我们";
        //创建Adapter，指明映射字段
		CustomSimpleAdapter categoryAdapter = new CustomSimpleAdapter(this, categories, R.layout.category_title,new String[]{"category_title"}, new int[]{R.id.category_title});
		
		
		GridView category = new GridView(this);
		category.setColumnWidth(300);//每个单元格宽度
		category.setNumColumns(GridView.AUTO_FIT);//单元格数目
		category.setGravity(Gravity.CENTER);//设置对其方式
		//设置单元格选择是背景色为透明，这样选择时就不现实黄色背景
		//category.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		int width = mColumnWidthDip * categories.size();
		LayoutParams params = new LayoutParams(width, LayoutParams.FILL_PARENT);
		//更新category宽度和高度，这样category在一行显示
		category.setLayoutParams(params);
		
		//设置适配器
		category.setAdapter(categoryAdapter);
		//把category加入到容器中
		LinearLayout categoryList = (LinearLayout) findViewById(R.id.category_layout);
		categoryList.addView(category);
		//添加单元格点击事件
		category.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				TextView categoryTitle;
				//恢复每个单元格背景色
				for (int i = 0; i < parent.getChildCount(); i++){
					categoryTitle = (TextView) (parent.getChildAt(i));
					categoryTitle.setBackgroundDrawable(null);
					categoryTitle.setTextColor(0XFFADB2AD);
				}
				//设置选择单元格的背景色
				categoryTitle = (TextView) (parent.getChildAt(position));
				categoryTitle.setBackgroundResource(R.drawable.categorybar_item_background);
				categoryTitle.setTextColor(0XFFFFFFFF);
				//获取选中的新闻分类id
				mCid = categories.get(position).get("category_title").getCid();
				mCatName = categories.get(position).get("category_title").getTitle();
				//获取该栏目下新闻

				loadNewsAsyncTask = new LoadNewsAsyncTask();
				loadNewsAsyncTask.execute(mCid,0,true);
			}
		});
		
		// 箭头
		final HorizontalScrollView categoryScrollView = (HorizontalScrollView)findViewById(R.id.category_scrollview);
		Button categoryArrowRight = (Button)findViewById(R.id.category_arrow_right);
		categoryArrowRight.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				categoryScrollView.fling(mFlingVelocityDip);
			}
		});
		//获取指定栏目的新闻列表
		getSpeCateNews(mCid,mNewsData,0,true);
		mNewsListAdapter = new SimpleAdapter(this, mNewsData, R.layout.newslist_item, 
				                        new String[]{"newslist_item_title", "newslist_item_digest",
												"newslist_item_source", "newslist_item_ptime"},
				                        new int[]{R.id.newslist_item_title, R.id.newslist_item_digest,
												R.id.newslist_item_source, R.id.newslist_item_ptime});
		View loadMoreLayout = mInflater.inflate(R.layout.loadmore, null);
		mNewsList.addFooterView(loadMoreLayout);
		mNewsList.setAdapter(mNewsListAdapter);
		mNewsList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				Intent intent = new Intent(NewsActivity.this, NewsDetailsActivity.class);
				//把需要的信息放到Intent中;
				intent.putExtra("categoryName", mCatName);
				intent.putExtra("newsDate", mNewsData);
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
	    
		mLoadMoreBtn = (Button)findViewById(R.id.loadmore_btn);
		mLoadMoreBtn.setOnClickListener(loadMoreListener);
		
		loadNewsAsyncTask = new LoadNewsAsyncTask();
		loadNewsAsyncTask.execute(mCid,0,true);
	}

	/**
	 * 获取指定类型的新闻列表
	 * @param cid 类型ID
	 * @param newsList 保存新闻信息的集合
	 * @param startnid 分页
	 * @param firstTimes	是否第一次加载
	 */
	private int getSpeCateNews(int cid,List<HashMap<String, Object>> newsList,int startnid,Boolean firstTimes){
		if (firstTimes){
			//如果是第一次，则清空集合里数据
			newsList.clear();
		}
		//请求URL和字符串
		String url = "http://111.114.116.28:8080/web/getSpecifyCategoryNews";
		String params = "startnid="+startnid+"&count="+NEWSCOUNT+"&cid="+cid;
		SyncHttp syncHttp = new SyncHttp();
		try{
			//以Get方式请求，并获得返回结果
			String retStr = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(retStr);
			//获取返回码，0表示成功
			int retCode = jsonObject.getInt("ret");
			if (0==retCode){
				JSONObject dataObject = jsonObject.getJSONObject("data");
				//获取返回数目
				int totalnum = dataObject.getInt("totalnum");
				if (totalnum>0){
					//获取返回新闻集合
					JSONArray newslist = dataObject.getJSONArray("newslist");
					for(int i=0;i<newslist.length();i++){
						JSONObject newsObject = (JSONObject)newslist.opt(i); 
						HashMap<String, Object> hashMap = new HashMap<String, Object>();
						hashMap.put("nid", newsObject.getInt("nid"));
						hashMap.put("newslist_item_title", newsObject.getString("title"));
						hashMap.put("newslist_item_digest", newsObject.getString("digest"));
						hashMap.put("newslist_item_source", newsObject.getString("source"));
						hashMap.put("newslist_item_ptime", newsObject.getString("ptime"));
						newsList.add(hashMap);
					}
					return SUCCESS;
				}
				else{
					if (firstTimes){
						return NONEWS;
					}
					else{
						return NOMORENEWS;
					}
				}
			}
			else{
				return LOADERROR;
			}
		} catch (Exception e){
			e.printStackTrace();
			return LOADERROR;
		}
	}
	
	private OnClickListener loadMoreListener = new OnClickListener(){
		@Override
		public void onClick(View v){
			loadNewsAsyncTask = new LoadNewsAsyncTask();
			switch (v.getId()){
			case R.id.loadmore_btn:
				loadNewsAsyncTask.execute(mCid,mNewsData.size(),false);
				break;
			case R.id.titlebar_refresh:
				loadNewsAsyncTask.execute(mCid,0,true);
				break;
			}
			
		}
	};
	
	private class LoadNewsAsyncTask extends AsyncTask<Object, Integer, Integer>{
		
		@Override
		protected void onPreExecute(){
			//隐藏刷新按钮
			mTitlebarRefresh.setVisibility(View.GONE);
			//显示进度条
			mLoadnewsProgress.setVisibility(View.VISIBLE); 
			//设置LoadMore Button 显示文本
			mLoadMoreBtn.setText(R.string.loadmore_txt);
		}

		@Override
		protected Integer doInBackground(Object... params){
			return getSpeCateNews((Integer)params[0],mNewsData,(Integer)params[1],(Boolean)params[2]);
		}

		@Override
		protected void onPostExecute(Integer result){
			//根据返回值显示相关的Toast
			switch (result){
			case NONEWS:
				Toast.makeText(NewsActivity.this, R.string.no_news, Toast.LENGTH_LONG).show();
			break;
			case NOMORENEWS:
				Toast.makeText(NewsActivity.this, R.string.no_more_news, Toast.LENGTH_LONG).show();
				break;
			case LOADERROR:
				Toast.makeText(NewsActivity.this, R.string.load_news_failure, Toast.LENGTH_LONG).show();
				break;
			}
			mNewsListAdapter.notifyDataSetChanged();
			//显示刷新按钮
			mTitlebarRefresh.setVisibility(View.VISIBLE);
			//隐藏进度条
			mLoadnewsProgress.setVisibility(View.GONE); 
			//设置LoadMore Button 显示文本
			mLoadMoreBtn.setText(R.string.loadmore_btn);
		}
	}
}