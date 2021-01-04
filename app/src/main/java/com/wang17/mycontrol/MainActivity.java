package com.wang17.mycontrol;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.wang17.mycontrol.fragment.ControlFragment;
import com.wang17.mycontrol.fragment.TvControlFragment;
import com.wang17.mycontrol.model.DataContext;
import com.wang17.mycontrol.model.Setting;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    // 视图变量
    private ViewPager mViewPager;
    // 值变量
    private ViewPagerAdapter mViewPagerAdapter;
    private List<Fragment> fragmentList;
    private TabLayout tabLayout_menu;
    private DataContext mDataContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        init(savedInstanceState);
    }


    private void init(Bundle savedInstanceState) {
        try {
            mDataContext = new DataContext(this);

            fragmentList = new ArrayList<>();
            tabLayout_menu = findViewById(R.id.tabLayout_menu);

            fragmentList.add(new TvControlFragment());
            tabLayout_menu.addTab(tabLayout_menu.newTab().setText("电视"));
//            fragmentList.add(new ControlFragment());
//            tabLayout_menu.addTab(tabLayout_menu.newTab().setText("遥控"));

            tabLayout_menu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    try {
                        mViewPager.setCurrentItem(tab.getPosition());
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, fragmentList.get(tab.getPosition())).commit();
                        mDataContext.editSetting(Setting.KEYS.main_start_page_index, tab.getPosition());
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });


            mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            mViewPager = findViewById(R.id.viewPage_content);
            mViewPager.setOffscreenPageLimit(3);
            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.addTouchables(null);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    tabLayout_menu.getTabAt(position).select();
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    switch (state) {
                        case ViewPager.SCROLL_STATE_IDLE:
                            break;
                        case ViewPager.SCROLL_STATE_DRAGGING:
                            break;
                        case ViewPager.SCROLL_STATE_SETTLING:
                            break;
                        default:
                            break;
                    }
                }
            });

            int position = Integer.parseInt(mDataContext.getSetting(Setting.KEYS.main_start_page_index, 0).getString());
            if (position >= tabLayout_menu.getTabCount())
                position = 0;
            tabLayout_menu.getTabAt(position).select();

        } catch (NumberFormatException e) {
        }
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
    //endregion


}
