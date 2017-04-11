package com.example.myq.mleaf;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;

import com.example.myq.fragment.FragmentDefaultMain;
import com.example.myq.fragment.LeftSlidingMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainActivity extends SlidingFragmentActivity implements View.OnClickListener {

    protected SlidingMenu leftRightSlidingMenu;
    private ImageButton ivTitleBtnLeft;
    private Fragment mContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLeftRightSlidingMenu();
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ivTitleBtnLeft = (ImageButton)this.findViewById(R.id.ivTitleBtnLeft);
        ivTitleBtnLeft.setOnClickListener(this);
    }

    private void initLeftRightSlidingMenu() {
        mContent = new FragmentDefaultMain();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent).commit();

        setBehindContentView(R.layout.main_left_layout);
        FragmentTransaction leftFragementTransaction = getSupportFragmentManager().beginTransaction();
        Fragment leftFrag = new LeftSlidingMenuFragment();
        leftFragementTransaction.replace(R.id.main_left_fragment, leftFrag);
        leftFragementTransaction.commit();

        // customize the SlidingMenu
        leftRightSlidingMenu = getSlidingMenu();
        leftRightSlidingMenu.setMode(SlidingMenu.LEFT);// 设置是左滑还是右滑，还是左右都可以滑，我这里只做了左滑
        leftRightSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 设置菜单宽度
        leftRightSlidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
        leftRightSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置手势模式
        leftRightSlidingMenu.setShadowDrawable(R.drawable.shadow);// 设置左菜单阴影图片
        leftRightSlidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
        leftRightSlidingMenu.setBehindScrollScale(0.333f);// 设置滑动时拖拽效果

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivTitleBtnLeft:
                leftRightSlidingMenu.showMenu();
                break;
            default:
                break;
        }

    }


    /**
     *    左侧菜单点击切换首页的内容
     */

    public void switchContent(Fragment to) {
     /*   mContent = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        getSlidingMenu().showContent();*/
        if (mContent != to) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(mContent).add(R.id.content_frame, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mContent = to;
        }
        showContent();
    }


}
