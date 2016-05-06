package cn.edu.scnu.markit.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.edu.scnu.markit.R;

/**
 * Created by jialin on 2016/5/1.
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener{

    private EditText mEmail = null;
    private EditText mNewPassword = null;
    private EditText mEnsurePassword = null;

    private Button mEnsureButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_layout);

        initViews();
    }

    private void initViews(){
        mEmail = (EditText)findViewById(R.id.forgetPassword_email_edit);
        mNewPassword = (EditText)findViewById(R.id.forgetPassword_password_edit);
        mEnsurePassword = (EditText)findViewById(R.id.forgetPassword_ensure_email_edit);

        mEnsureButton = (Button)findViewById(R.id.forgetPassword_down);
        mEnsureButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.forgetPassword_down){

            String email = mEmail.getText().toString();
            String password = mNewPassword.getText().toString();
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

            finish();
        }
    }
}
