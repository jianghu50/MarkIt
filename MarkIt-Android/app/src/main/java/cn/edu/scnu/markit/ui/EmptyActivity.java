package cn.edu.scnu.markit.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import cn.edu.scnu.markit.MainActivity;

/**
 * Created by jialin on 2016/4/28.
 */
public class EmptyActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("isLoginSuccess",0);
        boolean is_Login = sharedPreferences.getBoolean("loginSucceed", false);

        Intent intent = null;
        if (is_Login){
           intent = new Intent(this,MainActivity.class);
        }else {
            intent = new Intent(this,LoginRegisterActivity.class);
        }
        startActivity(intent);
    }
}
