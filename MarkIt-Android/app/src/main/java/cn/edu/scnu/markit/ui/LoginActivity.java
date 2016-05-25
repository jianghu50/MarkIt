package cn.edu.scnu.markit.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import cn.edu.scnu.markit.R;
import cn.edu.scnu.markit.config.MarkItConstants;
import cn.edu.scnu.markit.util.CommonUtils;

/**
 * Created by jialin on 2016/4/28.
 * Description: login
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private EditText mEmail = null;
    private EditText mPassword = null;
    private CheckBox mCheckBox = null;
    private Button mLogin = null;
    private TextView mForgetPassword = null;

    private MyBroadcastReceiver receiver = new MyBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment_layout);

        initViews();

        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, filter);
    }

    private void initViews() {

        mEmail = (EditText)findViewById(R.id.login_email_edit);
        mPassword = (EditText)findViewById(R.id.login_password_edit);
        mCheckBox = (CheckBox)findViewById(R.id.login_checkBox);

        mLogin = (Button)findViewById(R.id.login_down);
        mLogin.setOnClickListener(this);

        mForgetPassword = (TextView)findViewById(R.id.login_forget_password);
        mForgetPassword.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_down:
                boolean isNerConnected = CommonUtils.isNetworkAvailable(this);
                if (!isNerConnected){
                    showToast(R.string.net_unAvailable);
                    return;
                }else {
                    login();
                }
                break;
            case R.id.login_forget_password:
                break;
            default:
                break;
        }
    }

    private void login() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            showToast(R.string.username_null);
            return;
        }

        if (TextUtils.isEmpty(password)){
            showToast(R.string.password_null);
            return;
        }

        //新建一个没有界面的的activity，判断进入登录界面还是主界面
        //配置文件记录登录成功，再次启动时，判断是否登录成功过，是则直接跳到主页面
        SharedPreferences sharedPreferences = getSharedPreferences("isLoginSuccess",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loginSucceed",true);
        editor.commit();

    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && MarkItConstants.NET_WORK_ACTION.equals(intent.getAction())) {
                finish();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
