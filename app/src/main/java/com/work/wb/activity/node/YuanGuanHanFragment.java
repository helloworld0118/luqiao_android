package com.work.wb.activity.node;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.work.wb.R;
import com.work.wb.activity.ProcedureActivity;
import com.work.wb.adapter.NodeListAdapter;
import com.work.wb.util.Utils;
import com.work.wb.util.model.NodeModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link YuanGuanHanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link YuanGuanHanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YuanGuanHanFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "title";
    private static final String TAG = "YuanGuanHanFragment";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView lvNodeView;
    private List<NodeModel> data=new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    public YuanGuanHanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YangGuanHanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YuanGuanHanFragment newInstance(String param1, String param2) {
        YuanGuanHanFragment fragment = new YuanGuanHanFragment();
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
    public void updateData(){
        try {
            Utils.cleanShardDataByKey(getContext(),"yuanguanhua_date");
            new Thread(runnable).start();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private NodeListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_han, container, false);
        lvNodeView = view.findViewById(R.id.yuan_guan_han_list);


        adapter = new NodeListAdapter(data,getContext());
        lvNodeView.setAdapter(adapter);
        lvNodeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                NodeModel model=(NodeModel) lvNodeView.getItemAtPosition(arg2);

                Intent intent = new Intent(getActivity(),ProcedureActivity.class);
                intent.putExtra("title", mParam2);
                intent.putExtra("program", "圆管涵");
                intent.putExtra("node", model.getNode());
                startActivity(intent);
            }
        });
        try {
            new Thread(runnable).start();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return view;
    }
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();

            String lastDate = (String) Utils.readObject(getContext(),"yuanguanhua_date",String.class);
            if(lastDate!=null&&!"".equals(lastDate)&&Utils.getCurrentDate().equals(lastDate)){
                String result = (String) Utils.readObject(getContext(),"yuanguanhua_nodes",String.class);
                data.putString("data",result);
                msg.setData(data);
                handler.sendMessage(msg);
            }else {
                Map<String,String> params = new HashMap<>();
                params.put("code",(String) Utils.readObject(getContext(),"code",String.class));
                params.put("type","1");
                try {
                    String result = Utils.httpGet("api/getNodes",params,getContext());
                    if(result.length()>2){
                        data.putString("data",result);
                    }else{
                        data.putString("data",null);
                    }
                    msg.setData(data);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    Log.e(TAG, "error========"+e.getMessage());
                }
            }

        }
    };
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("data");
            if(null!=result){
                try{
                    data = new Gson().fromJson(result,new TypeToken<List<NodeModel>>() {}.getType());
                    adapter.setList(data);
                    adapter.notifyDataSetChanged();
                    Utils.saveObject(getContext(),Utils.getCurrentDate(),"yuanguanhua_date");
                    Utils.saveObject(getContext(),result,"yuanguanhua_nodes");
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
