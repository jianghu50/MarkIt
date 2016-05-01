package cn.edu.scnu.markit.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.listener.SaveListener;
import cn.edu.scnu.markit.MainActivity;
import cn.edu.scnu.markit.R;
import cn.edu.scnu.markit.javabean.User;
import cn.edu.scnu.markit.util.CommonUtils;

/**
 * Created by jialin on 2016/4/29.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener{

    private EditText mEmail = null;
    private EditText mPassword = null;
    private EditText mEnsurePassword = null;

    private Button mEnsureButton = null;

    private View registerView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        registerView = inflater.inflate(R.layout.register_fragment_layout,container,false);
        return registerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews(){
        mEmail = (EditText)registerView.findViewById(R.id.register_email_edit);
        mPassword = (EditText)registerView.findViewById(R.id.register_password_edit);
        mEnsurePassword = (EditText)registerView.findViewById(R.id.register_ensure_password_edit);

        mEnsureButton = (Button)registerView.findViewById(R.id.register_down);
        mEnsureButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_down){
            boolean isNerConnected = CommonUtils.isNetworkAvailable(getContext());
            if (!isNerConnected){
                showToast(R.string.net_unAvailable);
                return;
            }else {
                register();
            }


        }
    }

    private void register(){
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String ensurePassword = mEnsurePassword.getText().toString();


        if (TextUtils.isEmpty(email)){
            showToast(R.string.username_null);
            return;
        }

        if (TextUtils.isEmpty(password)){
            showToast(R.string.password_null);
            return;
        }

        if (TextUtils.isEmpty(ensurePassword)){
            showToast(R.string.ensurePassword_null);
            return;
        }

        if (!password.equals(ensurePassword)){
            showToast(R.string.password_different);
            return;
        }

        final User myUser = new User();
        //把邮箱同时设置为Username
        myUser.setUsername(email);
        myUser.setEmail(email);
        myUser.setPassword(password);
        myUser.signUp(getContext(), new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast("注册成功:" + myUser.getEmail() + "-"
                        + myUser.getObjectId() + "-" + myUser.getCreatedAt()
                        + "-" + myUser.getSessionToken() + ",是否验证：" + myUser.getEmailVerified());
                //接下来应该进行验证邮箱或者其他UI跳转
                Intent intent = new Intent(getContext(), MainActivity.class);
                getActivity().finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                toast("注册失败:" + msg);
            }
        });

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
