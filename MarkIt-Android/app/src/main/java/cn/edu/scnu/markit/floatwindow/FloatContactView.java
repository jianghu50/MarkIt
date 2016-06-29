package cn.edu.scnu.markit.floatwindow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
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

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.edu.scnu.markit.R;
import cn.edu.scnu.markit.adapter.FloatContactViewAdapter;
import cn.edu.scnu.markit.javabean.Contact;
import cn.edu.scnu.markit.javabean.User;
import cn.edu.scnu.markit.util.DataManager;
import cn.edu.scnu.markit.util.DataSyncManager;

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

    private SQLiteDatabase db;

    private DataManager dataManager = DataManager.getDataManager();

    private List<Contact> mContactList ;

    private FloatContactViewAdapter adapter = null;

    private IFloatContactEventListener mFloatContactEventListener = null;

    private Handler mHandler = null;

    public static final int ADD_CONTACT_SUCCESS = 1;

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

        mContactList = dataManager.getContactList();

        initHandler();

        initViews();
    }



    private void initHandler(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (ADD_CONTACT_SUCCESS == msg.what){
                    Log.i("size---->", String.valueOf(mContactList.size()));

                    FloatContactViewAdapter newAdapter = new FloatContactViewAdapter(mContext,mContactList);
                    mContactListView.setAdapter(newAdapter);

                }
            }
        };
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

    private FloatContactViewAdapter contactAdapter(){

        adapter = new FloatContactViewAdapter(mContext, mContactList);
        return adapter;
    }

    private void setListViewHeight(){

        if (mContactList.size() > 5){
            ViewGroup.LayoutParams layoutParams = mContactListView.getLayoutParams();
            layoutParams.height = MyWindowManager.getScreenHeight(mContext) / 2 - 50;
            mContactListView.setLayoutParams(layoutParams);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contact addContact = (Contact)mContactListView.getItemAtPosition(position);

        DataManager.getDataManager().setContact(addContact);
        String contact = ((TextView)view).getText().toString();
        removeContactDialog(contact);


    }

    private void removeContactDialog(String contact){

        mFloatContactEventListener.floatContactEvent(contact);
        MyWindowManager.removeFloatContact(mContext);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

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

        String newContact = mEditText.getText().toString();

        if (newContact.length() == 0){
            Log.i("addNewContact", "isClicked");
            Toast.makeText(mContext,R.string.pleaseInputNewContact,Toast.LENGTH_SHORT).show();
        }else {



            DataSyncManager.createContact(mContext, newContact);

            //添加完联系人，再次全部读取网上的联系人
            User myUser = BmobUser.getCurrentUser(mContext, User.class);

            final String userId = myUser.getObjectId();

            Log.i("UserId------->", userId);

            BmobQuery<Contact> query = new BmobQuery<>();

            query.addWhereEqualTo("user", myUser);
            query.setLimit(100);
            query.addWhereEqualTo("isDelete", false);
            query.findObjects(mContext, new FindListener<Contact>() {
                @Override
                public void onSuccess(List<Contact> list) {
                    if (list.size() != 0) {
                        DataManager.getDataManager().setContactList(list);
                        mContactList = list;

                        setListViewHeight();      //设置listView高度

                        Message msg = Message.obtain();
                        msg.what = ADD_CONTACT_SUCCESS;
                        mHandler.sendMessage(msg);
                    } else {
                        Log.i("TAG----->", "list size is 0");

                    }
                }
                @Override
                public void onError(int i, String s) {

                }
            });
            Log.i("addNewContact", newContact);

        }
    }
    /**
     * 接口回传Dialog的值
     */
    public interface IFloatContactEventListener {
        void floatContactEvent(String contact);
    }
}
