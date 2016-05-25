package cn.edu.scnu.markit.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.edu.scnu.markit.MainActivity;
import cn.edu.scnu.markit.R;
import cn.edu.scnu.markit.javabean.User;
import cn.edu.scnu.markit.ui.ForgetPasswordActivity;
import cn.edu.scnu.markit.util.CommonUtils;
import cn.edu.scnu.markit.util.MyDatabaseManager;

/**
 * Created by jialin on 2016/4/29.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText mEmail = null;
    private EditText mPassword = null;
    private CheckBox mCheckBox = null;
    private Button mLogin = null;
    private TextView mForgetPassword = null;

    private View loginView;

    final String my_email = "909392891@qq.com";
    final String my_password = "123456";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        loginView = inflater.inflate(R.layout.login_fragment_layout,container,false);
        return loginView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews(){

        mEmail = (EditText)loginView.findViewById(R.id.login_email_edit);
        mPassword = (EditText)loginView.findViewById(R.id.login_password_edit);
        mCheckBox = (CheckBox)loginView.findViewById(R.id.login_checkBox);

        mLogin = (Button)loginView.findViewById(R.id.login_down);
        mLogin.setOnClickListener(this);

        mForgetPassword = (TextView)loginView.findViewById(R.id.login_forget_password);
        mForgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_down:
                boolean isNerConnected = CommonUtils.isNetworkAvailable(getContext());
                if (!isNerConnected){
                   showToast(R.string.net_unAvailable);
                    return;
                }else {
                   login();
                }
                break;
            case R.id.login_forget_password:
                Intent intent = new Intent(getContext(), ForgetPasswordActivity.class);
                startActivity(intent);
               // getActivity().finish();
                break;
            default:
                break;
        }
    }

    private void login() {
        String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            showToast(R.string.username_null);
            return;
        }

        if (TextUtils.isEmpty(password)){
            showToast(R.string.password_null);
            return;
        }


        Log.i("bmobTest","email = " + email + "password = " + password);

        final boolean rememberMe = mCheckBox.isChecked();

        final BmobUser bu2 = new BmobUser();
        bu2.setUsername(email);
        bu2.setPassword(password);
        bu2.login(getContext(), new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast(bu2.getUsername() + "登陆成功");
                //testGetCurrentUser();

                //新建一个没有界面的的activity，判断进入登录界面还是主界面
                //配置文件记录登录成功,且用户选择记住操作，再次启动时，判断是否登录成功过，是则直接跳到主页面
                if (rememberMe){
                    SharedPreferences sharedPreferences;
                    sharedPreferences = getActivity().getSharedPreferences("isLoginSuccess",0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("loginSucceed",true);
                    editor.putString("userId",bu2.getObjectId());
                    editor.commit();
                }

                //添加用户到用户表
                MyDatabaseManager.insertUser(bu2.getObjectId(),bu2.getUsername(),password);

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

                getActivity().finish();

            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                toast("登陆失败:" + code + "," + msg);
            }
        });



    }

    /**
     * 获取本地用户
     * @Method getCurrentUser(Context context, java.lang.Class<T> clazz)
     * 一般用来获取本地用户的账户名字
     * 或者检测用户需不需要登录
     */
    private void testGetCurrentUser() {
        User myUser = BmobUser.getCurrentUser(getContext(), User.class);
        if (myUser != null) {
            Log.i("bmob", "本地用户信息:objectId = " + myUser.getObjectId() + ",name = "
                    + myUser.getEmail());
        } else {
            toast("本地用户为null,请登录。");
        }
    }

    public void showToast(int resId) {
        Toast mToast = null;
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), resId,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

    public void toast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
       // Log.d(TAG, msg);
    }
}
