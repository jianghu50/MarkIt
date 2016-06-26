package cn.edu.scnu.markit.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.scnu.markit.R;
import cn.edu.scnu.markit.adapter.FloatContactViewAdapter;
import cn.edu.scnu.markit.floatwindow.FloatContactView;
import cn.edu.scnu.markit.floatwindow.MyWindowManager;
import cn.edu.scnu.markit.util.InsertPictureUtils;
import cn.edu.scnu.markit.util.MyDatabaseManager;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditText = null;
    private TextView mContactIcon = null;
    private TextView mContactTextView = null;
    private TextView mContactArrow = null;
    private TextView mScreenShot = null;
    private TextView mCancelTextView = null;
    private TextView mAddTextView = null;
    private Context mContext = null;

    private ListView contactList = null;
    private EditText editContact = null;
    private TextView addContact = null;
    private FloatContactViewAdapter<String> adapter = null;
    private List<String> mContactList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        getSupportActionBar().setTitle("添加");
        mContext = this.getApplicationContext();
        initViews();
    }
    private void initViews() {
        mEditText = (EditText)findViewById(R.id.editText);
        mEditText.addTextChangedListener(textWatcher);

        mContactIcon = (TextView)findViewById(R.id.contact_icon);
        mContactIcon.setOnClickListener(this);

        mContactTextView = (TextView)findViewById(R.id.contact);
        mContactTextView.setOnClickListener(this);

        mContactArrow = (TextView)findViewById(R.id.contact_arrow);
        mContactArrow.setOnClickListener(this);

        mScreenShot = (TextView)findViewById(R.id.screenShot);
        mScreenShot.setOnClickListener(this);

        mCancelTextView = (TextView)findViewById(R.id.cancel);
        mAddTextView = (TextView)findViewById(R.id.add);

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
        switch (v.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.add:
                addContactNote();
                break;
            case R.id.contact_arrow:
            case R.id.contact:
            case R.id.contact_icon:    //创建联系人列表对话框
                chooseContact(v);
                break;
            case R.id.screenShot:
                getScreenShot();
                break;
            default:
                break;
        }
    }
    private void chooseContact(View v){
        FloatContactView floatContactView = new FloatContactView(mContext, new FloatContactView.IFloatContactEventListener() {
            @Override
            public void floatContactEvent(String contact) {
                //strFloatContact = contact;
                if (contact.length() != 0){
                    //textView.setText(contact);
                }
            }
        });
        View root = this.getLayoutInflater().inflate(R.layout.contact_dialog_layout, null);
        final PopupWindow popup = new PopupWindow(root, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        contactList =(ListView) root.findViewById(R.id.contact_dialog_listView);
        editContact = (EditText) root.findViewById(R.id.contact_dialog_editText);
        mContactList = MyDatabaseManager.queryContacts(MyDatabaseManager.userId);
        adapter = new FloatContactViewAdapter<String>(mContext,R.layout.listview_item,mContactList);
        contactList.setAdapter(adapter);
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(popup.isShowing()){
                    mContactTextView.setText(mContactList.get(position));
                    popup.dismiss();
                }
            }
        });
        addContact = (TextView) root.findViewById(R.id.contact_dialog_addIcon);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = editContact.getText().toString();
                if (text.length() == 0){
                    Log.i("addNewContact", "isClicked");
                    Toast.makeText(mContext,R.string.pleaseInputNewContact,Toast.LENGTH_SHORT).show();
                }else {
                    mContactList.add(text);
                    adapter.notifyDataSetChanged();
                    MyDatabaseManager.insertContact(text);
                    Log.i("addNewContact", text);
                    Toast.makeText(mContext,"添加联系人成功",Toast.LENGTH_SHORT).show();
                    //removeContactDialog(newContact);
                }
            }
        });
        popup.setAnimationStyle(R.style.contextMenuAnim);
        popup.showAtLocation(v,Gravity.CENTER,0,0);
        popup.setTouchable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setOutsideTouchable(true);
        popup.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId()==R.id.float_contact_view){
                    popup.dismiss();
                    return false;
                }
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
    }
    private void getScreenShot() {
        InsertPictureUtils.insertPicture(mContext,mEditText);
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
                MyDatabaseManager.insertNote(note,contactName);
                Toast.makeText(mContext,"添加成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}