package cn.edu.scnu.markit;

import android.app.Application;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.edu.scnu.markit.util.MyDatabaseHelper;
import cn.edu.scnu.markit.util.MyDatabaseManager;

/**
 * Created by Kasper on 2016/4/21.
 * 这个类是用来启动App时自动连接上云端。继承Application
 * APPID 是我们应用的密匙
 */
public class MarkItApplication extends Application{
    /**
     * SDK初始化也可以放到Application中
     */
    public static String APPID = "99a6b5c065255271a22d63836764b33b";


   /* public static final String IS_LOGIN_SUCCESS = "isLoginSuccess";

    public SharedPreferences sharedPreferences = null;

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
*/



    private static final String DATABASE_NAME = "UserStore.db";

    @Override
    public void onCreate() {
        super.onCreate();



        //设置BmobConfig
        BmobConfig config =new BmobConfig.Builder()
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                        //文件分片上传时每片的大小（单位字节），默认512*1024
                .setBlockSize(500*1024)
                .build();
        Bmob.getInstance().initConfig(config);
        //Bmob初始化
        Bmob.initialize(this, APPID);

        MyDatabaseManager.dbHelper = new MyDatabaseHelper(this,DATABASE_NAME,null,1);



       /* SQLiteDatabase db =  MyDatabaseManager.dbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + "Contacts");*/

    }
}
