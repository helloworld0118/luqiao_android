package com.work.wb.activity;





import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.work.wb.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramActivity extends AppCompatActivity {
    private GridView gvProgram;
    private SimpleAdapter simAdapter;
    private String title;
    private List<Map<String, Object>> program_list=new ArrayList<Map<String, Object>>();
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        Intent intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        title = intent.getStringExtra("title");
        toolbar.setTitle(title);
        toolbar.setSubtitle("选择分部工程(1/4)");
        setSupportActionBar(toolbar);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("image", R.mipmap.yuanguanhan144);
        map.put("text", "圆管涵");
        program_list.add(map);
        map = new HashMap<String, Object>();
        map.put("image", R.mipmap.gaibanhan144);
        map.put("text", "盖板涵");
        program_list.add(map);
        map = new HashMap<String, Object>();
        map.put("image", R.mipmap.xianghua144);
        map.put("text", "箱涵");
        program_list.add(map);
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        simAdapter = new SimpleAdapter(getApplicationContext(), program_list, R.layout.grid_item, from, to);
        gvProgram = findViewById(R.id.gridView_program);
        gvProgram.setAdapter(simAdapter);
        gvProgram.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {

                TextView textView = v.findViewById(R.id.text);
                Intent intent=new Intent(ProgramActivity.this,NodeActivity.class);
                intent.putExtra("program",textView.getText().toString());
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });
    }
}
