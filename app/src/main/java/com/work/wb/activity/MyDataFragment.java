package com.work.wb.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.work.wb.R;
import com.work.wb.activity.data.expense.ExpenseDataFragment;
import com.work.wb.activity.data.labor.LaborDataFragment;
import com.work.wb.activity.data.material.MaterialDataFragment;
import com.work.wb.activity.data.mechainc.MechaincDataFragment;
import com.work.wb.activity.data.oil.OilDataFragment;
import com.work.wb.entity.Role;
import com.work.wb.plugins.SerachSelectDialog;
import com.work.wb.util.FragmentpagerAdapter;
import com.work.wb.util.Utils;
import com.work.wb.util.model.ProjectRoleModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyDataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "title";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ViewPager mViewPager;
    private TabLayout mTableLayout;
    private ExpenseDataFragment expenseDataFragment;
    private LaborDataFragment lobarDataFragment;
    private MaterialDataFragment materialDataFragment;
    private MechaincDataFragment mechiancDataFragment;
    private OilDataFragment oilDataFragment;
    private TextView tvProjectName;
    Map<String,ProjectRoleModel> projects =new HashMap<>();
    List<Fragment> fragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    private FragmentpagerAdapter adapter;
    public MyDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyDataFragment newInstance(String param1, String param2) {
        MyDataFragment fragment = new MyDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refreshData(true);
                break;
        }
        return true;
    }
    private void refreshData(boolean clean){
     try{
         Fragment fragment = fragments.get(mViewPager.getCurrentItem());
         if(fragment instanceof LaborDataFragment){
             ((LaborDataFragment)fragment).updateData(clean);
         }else if(fragment instanceof ExpenseDataFragment){
             ((ExpenseDataFragment)fragment).updateData(clean);
         }else if(fragment instanceof MaterialDataFragment){
             ((MaterialDataFragment)fragment).updateData(clean);
         }else if(fragment instanceof MechaincDataFragment){
             ((MechaincDataFragment)fragment).updateData(clean);
         }else if(fragment instanceof OilDataFragment){
             ((OilDataFragment)fragment).updateData(clean);
         }
     }catch (Exception e){
            Log.e("myData",e.getMessage());
     }
    }


    public void updateView(){
        refreshData(false);
        ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getContext(),"currentProject",ProjectRoleModel.class);
        try{
            tvProjectName.setText(project.getProject().getName());
            initView();
        }catch (Exception e){}
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_refresh, menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_data, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("我的数据");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getContext(),"currentProject",ProjectRoleModel.class);
        tvProjectName=view.findViewById(R.id.tv_projectName);
        tvProjectName.setText(project.getProject().getName());
        tvProjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(getActivity());
                List<String> names = new ArrayList<String>();
                names.addAll(projects.keySet());
                alert.setListData(names);
                alert.setRefreshVisibility(View.GONE);
                alert.setTitle("选择项目切换");
                alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
                    @Override
                    public void onSelected(String info) {
                        if(null!=info&&!"".equals(info)){
                            if(!tvProjectName.getText().toString().equals(info)){
                                tvProjectName.setText(info);
                                Utils.saveObject(getContext(),projects.get(info),"currentProject");
                                initView();
                            }

                        }
                    }
                });
                SerachSelectDialog mDialog = alert.show();
                mDialog.setDialogWindowAttr(0.8,0.5,getActivity());
            }
        });
        mViewPager = view.findViewById(R.id.main_viewpager);
        mTableLayout = view.findViewById(R.id.main_tab);
        mTableLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTableLayout.setTabMode(TabLayout.MODE_FIXED);
        mTableLayout.setupWithViewPager(mViewPager);
        adapter = new FragmentpagerAdapter(getChildFragmentManager(),fragments,titles);
        mViewPager.setAdapter(adapter);
        initView();


        return view;
    }
    private void initView(){
        fragments.clear();
        titles.clear();
        mViewPager.clearDisappearingChildren();
        ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getContext(),"currentProject",ProjectRoleModel.class);
        if(null==expenseDataFragment) expenseDataFragment = ExpenseDataFragment.newInstance("","");
        fragments.add(expenseDataFragment);
        titles.add("报销");
        for(Role role:project.getRoles()){
           if(role.getId()==8){
                if(null==materialDataFragment)materialDataFragment = MaterialDataFragment.newInstance("","");
                fragments.add(materialDataFragment);
                titles.add("材料");
                continue;
            }else if(role.getId()==9){
                if(null==lobarDataFragment)lobarDataFragment = LaborDataFragment.newInstance("","");
                if(null==mechiancDataFragment)mechiancDataFragment = MechaincDataFragment.newInstance("","");
                fragments.add(lobarDataFragment);
                fragments.add(mechiancDataFragment);
                titles.add("人工");
                titles.add("机械");
                continue;
            }else if(role.getId()==10){
                if(null==oilDataFragment)oilDataFragment = OilDataFragment.newInstance("","");
                fragments.add(oilDataFragment);
                titles.add("加油");
                continue;
            }
        }
        adapter.setFragments(fragments);
        adapter.setTitles(titles);
        adapter.notifyDataSetChanged();
        if(titles.size()==0){
           mViewPager.setCurrentItem(FragmentPagerAdapter.POSITION_NONE);
        }
        List<ProjectRoleModel> list = (List<ProjectRoleModel>) Utils.readObject(getContext(),"projectRoles",new TypeToken<List<ProjectRoleModel>>() {}.getType());

        for(ProjectRoleModel entity:list){
            projects.put(entity.getProject().getName(),entity);
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
