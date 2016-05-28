package cn.edu.scnu.markit;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.edu.scnu.markit.adapter.RecordAdapter;
import cn.edu.scnu.markit.adapter.SortAdapter;
import cn.edu.scnu.markit.javabean.CharacterParser;
import cn.edu.scnu.markit.javabean.PinyinComparator;
import cn.edu.scnu.markit.javabean.Record;
import cn.edu.scnu.markit.javabean.SortModel;
import cn.edu.scnu.markit.ui.LoginRegisterActivity;
import cn.edu.scnu.markit.ui.view.SideBar;

import cn.edu.scnu.markit.floatwindow.FloatWindowService;


public class MainActivity extends AppCompatActivity
{
    // test user by kasper 16.4.21
    //不用的时候直接把改变量有关的方法或调用全部删除干净 ctrl + F
    Button userTest;
    private ListView sortListView;
    private SideBar sideBar;
    /**
     * 显示字母的TextView
     */
    private TextView dialog;
    private SortAdapter sortAdapter;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private android.support.v7.widget.Toolbar main_toolbar;
    private android.support.v7.widget.Toolbar contact_toolbar;
    private ListView main_list;
    private RecordAdapter recordAdapter;

    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        leftDraw(); //左侧滑框
        initViews();//主界面
        // test user by kasper 16.4.21
    }
    private void initViews(){
        main_toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        main_list = (ListView) findViewById(R.id.listView2);
        List<Record> records = getRecordData();
        recordAdapter = new RecordAdapter(this,records);
        main_list.setAdapter(recordAdapter);

        setSupportActionBar(main_toolbar);
        main_toolbar.setTitle("MarkIt");
        main_toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        main_toolbar.setNavigationIcon(R.drawable.contact);
        main_toolbar.inflateMenu(R.menu.menu_main_toolbar);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, main_toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        main_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if(menuItemId==R.id.home){

                }
                return false;
            }
        });
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }
    private List<Record> getRecordData() {
        List<Record> records = new ArrayList<Record>();
        String [] names = getResources().getStringArray(R.array.date);
        String [] texts = getResources().getStringArray(R.array.text);
        for(int i=0;i<names.length;i++){
            Record record = new Record();
            record.setName(names[i]);
            record.setText(texts[i]);
            records.add(record);
        }
        return records;
    }

    private void leftDraw() {
        contact_toolbar =(Toolbar) findViewById(R.id.contact_toolbar);
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidebar);
        dialog = (TextView) findViewById(R.id.dialog);

        setSupportActionBar(contact_toolbar);
        contact_toolbar.setNavigationIcon(R.drawable.addcontact);
        contact_toolbar.setTitle("联系人");
        contact_toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        contact_toolbar.inflateMenu(R.menu.menu_main_toolbar);

        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = sortAdapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.listView);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

<<<<<<< HEAD
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                Toast.makeText(getApplication(), ((SortModel)sortAdapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
=======
//                //startActivity(new Intent(MainActivity.this, TestUserActivity.class));
//               startActivity(intent);

               Intent intent = new Intent(MainActivity.this,FloatWindowService.class);
               startService(intent);
                String[] data = {"林俊杰","王力宏","罗志祥","刘德华","李连杰","成龙","李宇春","张杰","汪峰",
                                    "王菲","章子怡","刘亦菲","苏有朋","古天乐","谢霆锋"};

               finish();
>>>>>>> upstream/develop
            }
        });

        SourceDateList = filledData(getResources().getStringArray(R.array.date));

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        sortAdapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(sortAdapter);


    }


    /**
     * 为ListView填充数据
     * @param date
     * @return
     */
    private List<SortModel> filledData(String [] date){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<date.length; i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
