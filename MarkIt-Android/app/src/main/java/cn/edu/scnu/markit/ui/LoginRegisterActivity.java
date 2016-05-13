package cn.edu.scnu.markit.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.scnu.markit.R;
import cn.edu.scnu.markit.adapter.FragmentAdapter;
import cn.edu.scnu.markit.ui.fragment.LoginFragment;
import cn.edu.scnu.markit.ui.fragment.RegisterFragment;

/**
 * Created by jialin on 2016/4/29.
 */
public class LoginRegisterActivity extends FragmentActivity {
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;

    private ViewPager mPageVp;
    /**
     * Tab显示内容TextView
     */
    private TextView mTabLoginTv, mTabRegisterTv;
    /**
     * Tab的那个引导线
     */
    private ImageView mTabLineIv;
    /**
     * Fragment
     */
    private LoginFragment mLoginFg;
    private RegisterFragment mRegisterFg;

    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;

    /**
     * 标题栏的偏移量：offset
     * 标题栏的宽度：width
     * 标签页的宽度：offset + width + offset
     */
    private int tabLineOffset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_viewpager);

        initViews();
        initFragment();
        initTabLineWidth();

    }

    private void initViews() {


        mTabLoginTv = (TextView) this.findViewById(R.id.id_login_tv);
        mTabRegisterTv = (TextView) this.findViewById(R.id.id_register_tv);

        mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);

        mPageVp = (ViewPager) this.findViewById(R.id.viewPager);
    }

    private void initFragment() {
        mLoginFg = new LoginFragment();
        mRegisterFg = new RegisterFragment();
        mFragmentList.add(mLoginFg);
        mFragmentList.add(mRegisterFg);

        mFragmentAdapter = new FragmentAdapter(
                this.getSupportFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);
        mPageVp.setCurrentItem(0);

        mPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                        .getLayoutParams();

                Log.e("offset:", offset + "");
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记2个页面,
                 * 从左到右分别为0,1
                 * 0->1;  1->0
                 */


                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 2) + currentIndex
                            * (screenWidth / 2) + tabLineOffset);

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 2) + currentIndex
                            * (screenWidth / 2) + tabLineOffset);

                } /*else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                }*/
                mTabLineIv.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();    //每次选中都先重置textView颜色
                switch (position) {
                    case 0:
                        mTabLoginTv.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 1:
                        mTabRegisterTv.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
                currentIndex = position;        //记录当前页面
            }
        });

    }

    /**
     * 设置滑动条的宽度为屏幕的1/4(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = screenWidth / 4;


        mTabLineIv.setLayoutParams(lp);

        tabLineOffset = (screenWidth / 2 - screenWidth / 4) / 2;    //设置tabLine偏移量


    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        mTabLoginTv.setTextColor(getResources().getColor(R.color.text_gray));

        mTabRegisterTv.setTextColor(getResources().getColor(R.color.text_gray));
    }


}
