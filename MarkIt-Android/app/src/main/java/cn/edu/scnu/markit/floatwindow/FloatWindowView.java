package cn.edu.scnu.markit.floatwindow;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.scnu.markit.R;
import cn.edu.scnu.markit.javabean.Contact;
import cn.edu.scnu.markit.util.DataManager;
import cn.edu.scnu.markit.util.DataSyncManager;
import cn.edu.scnu.markit.util.InsertPictureUtils;

/**
 * Created by jialin on 2016/5/2.
 */
public class FloatWindowView extends RelativeLayout implements View.OnClickListener,View.OnTouchListener,View.OnKeyListener {
    /**
    * 记录大悬浮窗的宽度
    */
    public static int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;

    /**
     * 编辑框
     */
    private EditText mEditText = null;

    /**
     * 左边联系人图标
     */
    private TextView mContactIcon = null;

    /**
     * 联系人名字
     */
    private TextView mContactTextView = null;

    /**
     * 右边联系人箭头
     */
    private TextView mContactArrow = null;

    /**
     * 截图图标
     */
    private TextView mScreenShot = null;

    /**
     * 取消和添加按钮
     */
    private TextView mCancelTextView = null;
    private TextView mAddTextView = null;

    /**
     * 保存上下文
     */
    private Context mContext = null;

    /**
     * 最外层布局
     */
    private View floatWindowLayout = null;

    /**
     * 可操作部分View
     */
    private View contentView = null;

    public FloatWindowView(final Context context) {
        super(context);
        floatWindowLayout = LayoutInflater.from(context).inflate(R.layout.float_window_layout, this);
        contentView = findViewById(R.id.float_window_view);

        floatWindowLayout.setOnTouchListener(this);

        //floatWindowLayout.setOnKeyListener(this);       //点击返回按键无法关闭悬浮窗，此处有待解决

        viewWidth = contentView.getLayoutParams().width;
        viewHeight = contentView.getLayoutParams().height;

        mContext = context;



        initViews();


    }

    /**
     * 初始化控件
     */
    private void initViews(){
        mEditText = (EditText)findViewById(R.id.float_window_editText);
        mEditText.addTextChangedListener(textWatcher);      //监控EditText是否有输入

        mContactIcon = (TextView)findViewById(R.id.float_window_contact_icon);
        mContactIcon.setOnClickListener(this);

        mContactTextView = (TextView)findViewById(R.id.float_window_contact);

        mContactArrow = (TextView)findViewById(R.id.float_window_contact_arrow);
        mContactArrow.setOnClickListener(this);

        mScreenShot = (TextView)findViewById(R.id.float_window_screenShot);
        mScreenShot.setOnClickListener(this);

        mCancelTextView = (TextView)findViewById(R.id.float_window_cancel);
        mAddTextView = (TextView)findViewById(R.id.float_window_add);

        mCancelTextView.setOnClickListener(this);
        mAddTextView.setOnClickListener(this);


    }

    TextWatcher textWatcher = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() != 0){

                mAddTextView.setTextColor(getResources().getColor(R.color.blue));   //EditText有输入时，改变textView的颜色
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0){
                mAddTextView.setTextColor(getResources().getColor(R.color.text_gray));  //输入完毕，若字符长度为空时，为没输入
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.float_window_cancel:
                MyWindowManager.removeFloatWindow(mContext);
                MyWindowManager.createFloatButton(mContext);
                break;
            case R.id.float_window_add:
                addContactNote();
                break;
            case R.id.float_window_contact_arrow:
            case R.id.float_window_contact_icon:
                MyWindowManager.createFloatContact(mContext,mContactTextView);      //创建联系人列表对话框
                break;
            case R.id.float_window_screenShot:
                getScreenShot();
                break;
            default:
                break;
        }
    }

    private void addContactNote(){
        String note = mEditText.getText().toString();

        if (note.length() == 0){
            Toast.makeText(mContext,R.string.hint_input_content,Toast.LENGTH_SHORT).show();
            return;
        }else {
            String contactName = mContactTextView.getText().toString();
            if (contactName.equals("联系人")){
                Toast.makeText(mContext,R.string.select_contact,Toast.LENGTH_SHORT).show();
                return;
            }else {

                Contact contact = DataManager.getDataManager().getContact();
                Log.i("addContact",contact.getContactName() + " " + contact.getObjectId());

                DataSyncManager.createNote(mContext, contact.getObjectId(), note);
                MyWindowManager.removeFloatWindow(mContext);
                }


            }

    }

        /**
         * 打开图库，获取截图
         */
    private void getScreenShot() {
        InsertPictureUtils.insertPicture(mContext,mEditText);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("悬浮窗","WindowViewOnTouch");

        int x = (int)event.getX();
        int y = (int)event.getY();

        Rect rect = new Rect();
        contentView.getGlobalVisibleRect(rect);

        if (!rect.contains(x,y)){
            MyWindowManager.createFloatButton(mContext);
            MyWindowManager.removeFloatWindow(mContext);
        }

        return false;
    }



    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            MyWindowManager.removeFloatWindow(mContext);
        }
        return false;
    }
}
