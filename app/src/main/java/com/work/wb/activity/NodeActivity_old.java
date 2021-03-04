package com.work.wb.activity;





import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.work.wb.R;
import com.work.wb.activity.node.GaiBanHanFragment;
import com.work.wb.activity.node.XiangHanFragment;
import com.work.wb.activity.node.YuanGuanHanFragment;
import com.work.wb.util.FragmentpagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NodeActivity_old extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTableLayout;
    private YuanGuanHanFragment yuanGuanHanFragment;
    private GaiBanHanFragment gaiBanHanFragment;
    private XiangHanFragment xiangHanFragment;

    private List<Fragment> fragments = new ArrayList<>();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_refresh:
                refreshData();
                break;
        }
        return true;
    }

    private void  refreshData(){
        Fragment fragment = fragments.get(mViewPager.getCurrentItem());
        if(fragment instanceof YuanGuanHanFragment){
            ( (YuanGuanHanFragment)fragment).updateData();
        }else if(fragment instanceof GaiBanHanFragment){
            ( (GaiBanHanFragment)fragment).updateData();
        }else if(fragment instanceof XiangHanFragment){
            ( (XiangHanFragment)fragment).updateData();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);
        mViewPager =  findViewById(R.id.main_viewpager);
        Intent intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        String title = intent.getStringExtra("title");
        toolbar.setTitle(title);
        toolbar.setSubtitle("选择桩号");
        setSupportActionBar(toolbar);

        yuanGuanHanFragment= YuanGuanHanFragment.newInstance("",title);
        gaiBanHanFragment = GaiBanHanFragment.newInstance("",title);
        xiangHanFragment =XiangHanFragment.newInstance("",title);
        fragments.add(yuanGuanHanFragment);
        fragments.add(gaiBanHanFragment);
        fragments.add(xiangHanFragment);
        List<String> titles = new ArrayList<>();
        titles.add("圆管涵");
        titles.add("盖板涵");
        titles.add("箱涵");
        FragmentpagerAdapter adapter = new FragmentpagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewPager.setAdapter(adapter);
        mTableLayout = (TabLayout) findViewById(R.id.main_tab);
        mTableLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTableLayout.setTabMode(TabLayout.MODE_FIXED);
        mTableLayout.setupWithViewPager(mViewPager);

    }
}
