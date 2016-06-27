package cn.edu.scnu.markit.floatwindow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.scnu.markit.R;
import cn.edu.scnu.markit.adapter.FloatContactViewAdapter;
import cn.edu.scnu.markit.util.MyDatabaseManager;

/**
 * Created by jialin on 2016/5/11.
 */
public class FloatContactView extends RelativeLayout implements View.OnTouchListener,AdapterView.OnItemClickListener,View.OnClickListener{
    public static int viewWidth;
    public static int viewHeight;

    /**
     * 保存上下文
     */
    private Context mContext = null;

    /**
     * 最外层布局
     */
    private View floatContactLayout = null;

    /**
     * 可操作部分View
     */
    private View contentView = null;

    /**
     * 可见部分View
     */
    private View visibleView = null;

    private TextView mAddTextView = null;
    private EditText mEditText = null;
    private TextView mSearchTextView = null;
    private ListView mContactListView = null;

    //private String[] mContacts = null;

    private SQLiteDatabase db;

    private List<String> mContactList = new ArrayList<String>();

    private FloatContactViewAdapter<String> adapter = null;

    private IFloatContactEventListener mFloatContactEventListener = null;

    public FloatContactView(Context context,IFloatContactEventListener listener) {
        super(context);

        mFloatContactEventListener = listener;

        floatContactLayout = LayoutInflater.from(context).inflate(R.layout.contact_dialog_layout, this);
        contentView = findViewById(R.id.float_contact_view);
        visibleView = findViewById(R.id.float_contact_view_visible);

        floatContactLayout.setOnTouchListener(this);

        //floatWindowLayout.setOnKeyListener(this);       //点击返回按键无法关闭悬浮窗，此处有待解决

        viewWidth = contentView.getLayoutParams().width;
        viewHeight = contentView.getLayoutParams().height;

        mContext = context;

        getContacts();

        initViews();
    }



    private void initViews(){
        mAddTextView = (TextView)findViewById(R.id.contact_dialog_addIcon);
        mAddTextView.setOnClickListener(this);

        mEditText = (EditText)findViewById(R.id.contact_dialog_editText);

        mSearchTextView = (TextView)findViewById(R.id.contact_dialog_searchIcon);
        mSearchTextView.setOnClickListener(this);

        mContactListView = (ListView)findViewById(R.id.contact_dialog_listView);
        setListViewHeight();      //设置listView高度
        mContactListView.setAdapter(contactAdapter());
        mContactListView.setOnItemClickListener(this);
    }

    private FloatContactViewAdapter<String> contactAdapter(){

        //List<String> list = Arrays.asList(contacts);   //字符串数组转换成list

        adapter = new FloatContactViewAdapter<String>(mContext,R.layout.listview_item,mContactList);
        return adapter;
    }

    private void setListViewHeight(){
        if (mContactList.size() > 5){
            ViewGroup.LayoutParams layoutParams = mContactListView.getLayoutParams();
            layoutParams.height = MyWindowManager.getScreenHeight(mContext) / 2 - 50;
            mContactListView.setLayoutParams(layoutParams);
        }
    }
    private void getContacts(){
        mContactList = MyDatabaseManager.queryContacts(MyDatabaseManager.userId);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String contact = ((TextView)view).getText().toString();

        // mEditText.setText(contact);
        removeContactDialog(contact);
    }

    private void removeContactDialog(String contact){
        mFloatContactEventListener.floatContactEvent(contact);
        MyWindowManager.removeFloatContact(mContext);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.i("对话框","FloatContactOnTouch");

        int x = (int)event.getX();
        int y = (int)event.getY();

        Rect rect = new Rect();
        visibleView.getGlobalVisibleRect(rect);

        if (!rect.contains(x,y)){
            MyWindowManager.removeFloatContact(mContext);
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.contact_dialog_addIcon:
                addNewContact();
                break;
            case R.id.contact_dialog_searchIcon:
                break;
            default:
                break;
        }
    }

    private void addNewContact(){
        //Log.i("addNewContact","isClicked");
        String newContact = mEditText.getText().toString();
       /* db = MyDatabaseManager.dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("contactName",newContact);

        db.insert("Contacts",null,cv);*/



        if (newContact.length() == 0){
            Log.i("addNewContact", "isClicked");
            Toast.makeText(mContext,R.string.pleaseInputNewContact,Toast.LENGTH_SHORT).show();
        }else {
            mContactList.add(newContact);
            setListViewHeight();      //设置listView高度
            adapter.notifyDataSetChanged();

            MyDatabaseManager.insertContact(newContact);

            Log.i("addNewContact", newContact);
            //removeContactDialog(newContact);
        }
    }
    /**
     * 接口回传Dialog的值
     */
    public interface IFloatContactEventListener {
        void floatContactEvent(String contact);
    }
}
