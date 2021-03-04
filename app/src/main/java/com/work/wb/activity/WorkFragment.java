package com.work.wb.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.work.wb.R;
import com.work.wb.entity.Role;
import com.work.wb.entity.Staff;
import com.work.wb.plugins.SerachSelectDialog;
import com.work.wb.util.Utils;
import com.work.wb.util.model.ProjectRoleModel;
import com.work.wb.util.model.WorkChargeModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WorkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WorkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "title";
    private GridView gridView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView imageView;
    private TextView tvUserName;
    private OnFragmentInteractionListener mListener;
    private TextView tvProjectName;
    Map<String,ProjectRoleModel> projects =new HashMap<>();
    public WorkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkFragment newInstance(String param1, String param2) {
        WorkFragment fragment = new WorkFragment();
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private SimpleAdapter sim_adapter;
    private List<Map<String, Object>> data_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work, container, false);
        Staff user  = (Staff) Utils.readObject(getContext(),"currentUser",Staff.class);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(user.getName());


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
                alert.setTitle("选择项目切换");
                alert.setRefreshVisibility(View.GONE);
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

        gridView = (GridView) view.findViewById(R.id.gridView1);
        data_list = new ArrayList<Map<String, Object>>();
        initView();
        Log.e("WorkFragment","onCreateView");



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {

                TextView textView = v.findViewById(R.id.text);
                Intent intent=new Intent(getActivity(),ProgramActivity.class);
                if(textView.getText().toString().equals("人工")){
                    intent.putExtra("title","填写人工计费单");
                }else if(textView.getText().toString().equals("机械")){
                    intent.putExtra("title","填写机械计费单");
                }else if(textView.getText().toString().equals("材料")){
                    intent.putExtra("title","填写材料单");
                }else if(textView.getText().toString().equals("加油")){
                    intent =new Intent(getActivity(),OilActivity.class);
                }else if(textView.getText().toString().equals("报销")){
                    intent=new Intent(getActivity(),ExpenseActivity.class);
                }
                getActivity().overridePendingTransition(R.anim.open_enter, R.anim.open_exit);
                startActivity(intent);
            }
        });
        return view;
    }
    public void updateView(){
        ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getContext(),"currentProject",ProjectRoleModel.class);
        try{
            tvProjectName.setText(project.getProject().getName());
            initView();
        }catch (Exception e){}
    }
    private void initView(){
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        data_list.clear();
        ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getContext(),"currentProject",ProjectRoleModel.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map = new HashMap<String, Object>();
        map.put("image",  R.mipmap.expense144);
        map.put("text", "报销");
        data_list.add(map);
        for(Role role:project.getRoles()){
            if(role.getId()==8){
                map = new HashMap<String, Object>();
                map.put("image",  R.mipmap.material_title);
                map.put("text", "材料");
                data_list.add(map);
                continue;
            }else if(role.getId()==9){
                map = new HashMap<String, Object>();
                map.put("image", R.mipmap.labor_title);
                map.put("text", "人工");
                data_list.add(map);
                map = new HashMap<String, Object>();
                map.put("image",  R.mipmap.mechainc_title);
                map.put("text", "机械");
                data_list.add(map);
                continue;
            }else if(role.getId()==10){
                map = new HashMap<String, Object>();
                map.put("image",  R.mipmap.oil_title);
                map.put("text", "加油");
                data_list.add(map);
                continue;
            }
        }
        sim_adapter = new SimpleAdapter(getContext(), data_list, R.layout.grid_item, from, to);
        gridView.setAdapter(sim_adapter);
        sim_adapter.notifyDataSetChanged();
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
