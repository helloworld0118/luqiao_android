package com.work.wb.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.work.wb.R;
import com.work.wb.activity.settings.StaffInfoActivity;
import com.work.wb.activity.settings.UpdatePwdActivity;
import com.work.wb.plugins.SerachSelectDialog;
import com.work.wb.util.Utils;
import com.work.wb.util.model.ProjectRoleModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "title";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RelativeLayout  rlUpdatePwd;
    private RelativeLayout  rlStaffInfo;
    private RelativeLayout  rlLogout;
    private OnFragmentInteractionListener mListener;
    Map<String,ProjectRoleModel> projects =new HashMap<>();
    private TextView tvProjectName;
    public SettingsFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("设置");

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
                            }

                        }
                    }
                });
                SerachSelectDialog mDialog = alert.show();
                mDialog.setDialogWindowAttr(0.8,0.5,getActivity());
            }
        });
        rlUpdatePwd = view.findViewById(R.id.rl_update_password);
        rlStaffInfo= view.findViewById(R.id.rl_staff_info);
        rlLogout= view.findViewById(R.id.rl_logout);

        rlUpdatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getActivity(),UpdatePwdActivity.class);
                startActivity(intent);
            }
        });
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Utils.cleanShardData(getContext());
                //Utils.cleanShardDataByKey(getContext(),"code");
                Utils.cleanShardDataByKey(getContext(),"token");
                Utils.cleanShardDataByKey(getContext(),"currentProject");
                Utils.cleanShardDataByKey(getContext(),"currentUser");
                Utils.cleanShardDataByKey(getContext(),"lastLoginTime");
                Intent intent =  new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        rlStaffInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getActivity(),StaffInfoActivity.class);
                startActivity(intent);
            }
        });
        List<ProjectRoleModel> list = (List<ProjectRoleModel>) Utils.readObject(getContext(),"projectRoles",new TypeToken<List<ProjectRoleModel>>() {}.getType());

        for(ProjectRoleModel entity:list){

            projects.put(entity.getProject().getName(),entity);
        }
        return view;
    }
    public void updateView(){
        ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getContext(),"currentProject",ProjectRoleModel.class);
        try{
            tvProjectName.setText(project.getProject().getName());
        }catch (Exception e){}
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
