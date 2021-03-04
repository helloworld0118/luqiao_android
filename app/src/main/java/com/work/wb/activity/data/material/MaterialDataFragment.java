package com.work.wb.activity.data.material;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.work.wb.R;
import com.work.wb.activity.data.labor.LaborAllDayDataActivity;
import com.work.wb.activity.data.labor.LaborDataDetailActivity;
import com.work.wb.adapter.MaterialListAdapter;
import com.work.wb.adapter.MechaincListAdapter;
import com.work.wb.entity.LaborCost;
import com.work.wb.entity.MaterialReceived;
import com.work.wb.entity.MechanicsPrice;
import com.work.wb.entity.Role;
import com.work.wb.entity.Staff;
import com.work.wb.util.NetWorkUtils;
import com.work.wb.util.Utils;
import com.work.wb.util.model.MaterialModel;
import com.work.wb.util.model.ProjectRoleModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MaterialDataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MaterialDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MaterialDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView listViewData;
    private TextView tvDataTitleName;
    private TextView allDataBtn;
    private TextView tvAllSize;
    private TextView tvAllPrice;
    private MaterialListAdapter adapter;
    private List<MaterialReceived> list=new ArrayList<>();
    private LinearLayout ll_index;
    public MaterialDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OilDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MaterialDataFragment newInstance(String param1, String param2) {
        MaterialDataFragment fragment = new MaterialDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();
        updateData(false);
    }
    public void updateData(boolean clean){
        try {
            if(NetWorkUtils.isNetworkConnected(getContext())&&clean){
                Utils.cleanShardDataByKey(getContext(),"material_today_time");
            }
            new Thread(today_runnable).start();
        } catch (Exception e) {
            Log.e("YuanGuanHan", e.getMessage());
        }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_data, container, false);
        tvDataTitleName = view.findViewById(R.id.tv_data_title_name);
        tvDataTitleName.setText("材料单");
        listViewData = view.findViewById(R.id.listView_data);
        tvAllSize = view.findViewById(R.id.tv_all_size);
        tvAllPrice = view.findViewById(R.id.tv_all_price);
        adapter = new MaterialListAdapter(list,getContext());
        allDataBtn = view.findViewById(R.id.all_data_btn);
        listViewData.setAdapter(adapter);
        updateData(false);
        allDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MaterialAllDayDataActivity.class);
                startActivity(intent);
            }
        });

        listViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                MaterialReceived model=(MaterialReceived) listViewData.getItemAtPosition(arg2);

                Intent intent = new Intent(getActivity(),MaterialDataDetailActivity.class);
                intent.putExtra("model",model);
                startActivity(intent);
            }
        });
        return view;
    }
    Runnable today_runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            String lastTime = (String) Utils.readObject(getContext(),"material_today_time",String.class);
            if((lastTime!=null&&!"".equals(lastTime)&&Utils.getCompareDate(lastTime)/1000/60<=5)||!NetWorkUtils.isNetworkConnected(getContext())){
                String result = (String) Utils.readObject(getContext(),"material_today_data",String.class);
                data.putString("data",result);
                msg.setData(data);
                today_handler.sendMessage(msg);
            }else{
                Map<String,String> params = new HashMap<>();
                Staff user  = (Staff) Utils.readObject(getContext(),"currentUser",Staff.class);
                ProjectRoleModel project = (ProjectRoleModel) Utils.readObject(getContext(),"currentProject",ProjectRoleModel.class);
                params.put("code",(String) Utils.readObject(getContext(),"code",String.class));
                params.put("date",Utils.getCurrentDate());
                params.put("project",project.getProject().getId()+"");
                StringBuffer roles = new StringBuffer();
                for(Role role:project.getRoles()){
                    roles.append(role.getId()+",");
                }
                roles.deleteCharAt(roles.length()-1);
                params.put("roles",roles.toString());
                params.put("staff",user.getId()+"");
                try {
                    String result = Utils.httpGet("api/materialReceived/list",params,getContext());
                    Log.e("result=========",result);
                    data.putString("data",result);
                    msg.setData(data);
                    today_handler.sendMessage(msg);
                } catch (IOException e) {
                    Log.e("YuanGuanHan", "error========"+e.getMessage());
                }
            }
        }
    };



    Handler today_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("data");
            if(null!=result){
                try{
                    list = new Gson().fromJson(result,new TypeToken<List<MaterialReceived>>() {}.getType());
                    int allPrice=0;
                    for (MaterialReceived entity:list) {
                        allPrice+=entity.getFreightPrice()+entity.getMaterialPrice();
                    }
                    tvAllSize.setText(list.size()+"");
                    tvAllPrice.setText(allPrice+"");
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();

                    Utils.saveObject(getContext(),System.currentTimeMillis(),"material_today_time");
                    Utils.saveObject(getContext(),result,"material_today_data");
                }catch (Exception e){
                    Toast.makeText(getContext(), "数据读取出错",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    };






    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
       //getActivity().requestDisallowInterceptTouchEvent(false);
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
