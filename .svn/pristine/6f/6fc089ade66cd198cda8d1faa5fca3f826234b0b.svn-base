package com.xiezhiai.wechatplugin.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.ui.base.BaseFragment;
import com.xiezhiai.wechatplugin.widget.CommonTopBar;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;

/**
 * Created by shijiwei on 2018/10/19.
 *
 * @Desc:
 */
public class GroupHelperFragment extends BaseFragment {

    private RadioGroup rgTab;
    private ViewPager viewPager;

    private ArrayList<BaseFragment> mFragments = new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_grouphelper;
    }

    @Override
    public void initialData(Bundle savedInstanceState) {

        mFragments.add(new GroupSendMessageFragment());
        mFragments.add(new GroupReplySettingFragment());
    }

    @Override
    public void initialView(View view) {

        rgTab = view.findViewById(R.id.rg_tab);
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new GroupHelperPagerAdapter(getChildFragmentManager()));
    }

    @Override
    public void initialEvn() {

        rgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_group_send_msg:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_group_reply_setting:
                        viewPager.setCurrentItem(1);
                        break;
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                rgTab.check(i == 0 ? R.id.rb_group_send_msg : R.id.rb_group_reply_setting);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onHttpStart(int what) {

    }

    @Override
    public void onHttpSucceed(int what, Response response) {

    }

    @Override
    public void onHttpFinish(int what) {

    }

    private class GroupHelperPagerAdapter extends FragmentPagerAdapter {

        public GroupHelperPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
