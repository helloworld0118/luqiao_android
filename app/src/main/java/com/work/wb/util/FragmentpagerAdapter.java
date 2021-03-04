package com.work.wb.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by bing.wang on 2018/1/21.
 */

public class FragmentpagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;
    private List<String> titles;
    public FragmentpagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
        this.titles = titles;
    }
    public void setFragments(List<Fragment> list){
        this.list = list;
    }
    public void setTitles(List<String> titles){
        this.titles = titles;
    }
    public FragmentpagerAdapter(FragmentManager fm, List<Fragment> list, List<String> titles) {
        super(fm);
        this.list = list;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
