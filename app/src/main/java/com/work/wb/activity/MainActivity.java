package com.work.wb.activity;

import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.design.widget.BottomNavigationView;

import com.work.wb.R;
import com.work.wb.util.FragmentpagerAdapter;
import com.work.wb.util.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private NoScrollViewPager viewPager;
    private MenuItem prevMenuItem;
    private BottomNavigationView navigation;
    private MyDataFragment myDataFragment;
    private WorkFragment workFragment;
    private SettingsFragment settingsFragment;
    private List<Fragment> fragments = new ArrayList<>();
    FragmentpagerAdapter adapter;

    public MainActivity() {
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                viewPager.setCurrentItem(0);
                myDataFragment.updateView();
                workFragment.updateView();
                settingsFragment.updateView();
                return true;
            case R.id.navigation_dashboard:
                myDataFragment.updateView();
                workFragment.updateView();
                settingsFragment.updateView();
                viewPager.setCurrentItem(1);
                return true;
            case R.id.navigation_notifications:
                myDataFragment.updateView();
                workFragment.updateView();
                settingsFragment.updateView();
                viewPager.setCurrentItem(2);
                return true;
        }
        return false;
    }
};
    }

    private void initFragment(){
        ActionBar supportActionBar = getSupportActionBar();
        myDataFragment = MyDataFragment.newInstance("0","MyDataFragment");
        workFragment = WorkFragment.newInstance("1","workFragment");
        settingsFragment = SettingsFragment.newInstance("2","settingsFragment");
        fragments.add(myDataFragment);
        fragments.add(workFragment);
        fragments.add(settingsFragment);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = (NoScrollViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initFragment();
        setupViewPager(viewPager);


    }

    private void refreshBar(String title){
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(title);
        actionbar.setDisplayHomeAsUpEnabled(false);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter= new FragmentpagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
       // refreshBar("张三");
        viewPager.setCurrentItem(1);
    }
}
