package cn.edu.scnu.markit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.edu.scnu.markit.javabean.User;

/**
 * Created by Kasper on 2016/4/21.
 * 用户管理
 * 我这里提供一些登录、注册、邮箱验证和改密码等一系列的函数方法
 * 但是具体的调用函数的逻辑就需要Android组的两位@老板@佳霖讨论了
 * 可以根据示例直接调用。
 */
public class TestUserActivity extends TestBaseActivity {

    final String my_email = "909392891@qq.com";
    final String my_password = "123456";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_user_activity);

        mListview = (ListView) findViewById(R.id.listview);
        mAdapter = new ArrayAdapter<String>(this, R.layout.test_user_list_item,
                R.id.tv_item, getResources().getStringArray(R.array.bmob_user_list));
        mListview.setAdapter(mAdapter);
        mListview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                testBmob(position + 1);
            }
        });

    }

    private void testBmob(int pos) {
        switch (pos) {
            case 1://测试注册
                testSignUp();
                break;
            case 2://测试登录
                testLogin();
                break;
            case 3://测试当前存在的用户
                testGetCurrentUser();
                break;
            case 4://测试退出
                testLogOut();
                break;
            case 5://更新用户信息
                updateUser();
                break;
            case 6://验证密码
                checkPassword();
                break;
            case 7://通过邮箱来重置密码
                testResetPassword();
                break;
            case 8://邮箱验证
                emailVerify();
                break;
            case 9://查找用户
                testFindBmobUser();
                break;
            case 10://通过邮箱和密码登陆
                loginByEmailPwd();
                break;
            case 11://根据旧密码和新密码来修改当前用户密码
                updateCurrentUserPwd();
                break;
            default:
                break;
        }
    }

    /**
     * 注册用户 我这里写得不专业，主要是想说明主要调用的方法，以及方法参数的说明
     * @Method signUp(Context context, SaveListener listener) 实现注册功能
     * 注册一定得调用 @Method setUsername(String name) Username和Password都不能为null
     * 这里提一下注册的流程 一般是填写好邮箱、密码后，提示用户注册成功了，但是用户需要去
     * 验证邮箱，否则下次登录时先判断@Method getEmailVerified()，false的话不让登录，要去验证。
     * 后续可以再来讨论。
     */
    @SuppressLint("UseValueOf")
    private void testSignUp() {
        final User myUser = new User();
        //把邮箱同时设置为Username
        myUser.setUsername(my_email);
        myUser.setEmail(my_email);
        myUser.setPassword(my_password);
        myUser.signUp(this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast("注册成功:" + myUser.getEmail() + "-"
                        + myUser.getObjectId() + "-" + myUser.getCreatedAt()
                        + "-" + myUser.getSessionToken() + ",是否验证：" + myUser.getEmailVerified());
                //接下来应该进行验证邮箱或者其他UI跳转
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                toast("注册失败:" + msg);
            }
        });
    }

    /**
     * 登陆用户
     * @Method login(Context context, SaveListener listener) 实现登录功能
     * 这个登录方法是必须使用 Username + Password 来登录的
     * 一般创建一个父类对象来实现登录
     */
    private void testLogin() {


        final BmobUser bu2 = new BmobUser();
        bu2.setUsername(my_email);
        bu2.setPassword(my_password);
        bu2.login(this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast(bu2.getUsername() + "登陆成功");
                testGetCurrentUser();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                toast("登陆失败:" + code + "," + msg);
            }
        });

       /* Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);*/
    }

    /**
     * 获取本地用户
     * @Method getCurrentUser(Context context, java.lang.Class<T> clazz)
     * 一般用来获取本地用户的账户名字
     * 或者检测用户需不需要登录
     */
    private void testGetCurrentUser() {
		User myUser = BmobUser.getCurrentUser(this, User.class);
		if (myUser != null) {
			Log.i("bmob","本地用户信息:objectId = " + myUser.getObjectId() + ",name = "
                    + myUser.getEmail());
		} else {
			toast("本地用户为null,请登录。");
		}
        //V3.4.5版本新增加getObjectByKey方法获取本地用户对象中某一列的值
        // @Method getObjectByKey(Context context, java.lang.String key) 要注册/登录后才能调用！
//        String username = (String) BmobUser.getObjectByKey(this, "username");
//        Log.i("bmob", "username：" + username);

    }

    /**
     * 清除本地用户
     * @Method logOut(Context context)
     * 退出登录状态，会清除本地用户信息
     */
    private void testLogOut() {
        BmobUser.logOut(this);
    }

    /**
     * 更新用户操作并同步更新本地的用户信息
     * @Method update(Context context, UpdateListener listener)
     * 注意，@Method updateUser()，这个方法非常重要，用来上传用户信息
     * 一般在调用@Method update(Context context, UpdateListener listener)时，进行setter操作
     * 现在由于主要是测试用户注册、登录、改密码之类的用户管理操作
     * 所以没有添加 “联系人列表” 以及对应的“信息列表”，以后是需要再来这里更改的
     * 我注释的句子是例子，示例以后要怎样去添加联系人，请不要随意更改。
     */
    private void updateUser() {
        final User bmobUser = BmobUser.getCurrentUser(this, User.class);
        if (bmobUser != null) {
            User newUser = new User();
            //-----------------------普通setter操作-------------------------------
            //number类型 如更改用户的年龄之类的，不允许用户更改他登录的用户名，但允许更改
            // 他的nickname昵称，虽然我们没有。 例子而已
//			newUser.setAge(25);
            //---------------------数组操作(add、addAll、addUnique、addAllUnique)---------------------------------------
            //这里就是我们以后的 “联系人列表” 因为我们的每个用户要对应于各自的联系人，所以是Object
            //添加Object类型的数组,Object数组调用addAllUnique、addUnique方法后本地用户信息未支持去重
            // BankCard 对应于 Contact(联系人)
//            List<BankCard> cards =new ArrayList<BankCard>();
//            cards.add(new BankCard("建行", "111"));
//            newUser.addAll("cards", cards);
            //添加String类型的数组--String数组支持去重  --- 这个可以不去管先
//			newUser.addAllUnique("hobby", Arrays.asList("游泳"));
//			//----------------------setValue方式更新用户信息（必须先保证更新的列存在，
//                                              否则会报internal error）----------------------------
            //这是另外一种更新的方法
//			//更新number
//			newUser.setValue("age",25);
//			//更新整个Object
//			newUser.setValue("banker",person);
//			//更新String数组
//			newUser.setValue("hobby",Arrays.asList("看书","游泳"));
////			//更新某个Object的值
//			newUser.setValue("mainCard.cardNumber","10011");
//			//更新数组中某个Object
//			newUser.setValue("cards.0", new BankCard("工行", "10086"));
            //更新数组中某个Object的某个字段的值
//			newUser.setValue("cards.0.bankName", "中行");
            // ------- 以上就是进行update之前需要进行的操作以及示例 勿删！-----------
            newUser.update(this,bmobUser.getObjectId(),new UpdateListener() {

                @Override
                public void onSuccess() {
                    //本地的用户信息均已更新成功，可在此调用getCurrentUser方法来获取最新的用户信息
                    testGetCurrentUser();
                }

                @Override
                public void onFailure(int code, String msg) {
                    toast("更新用户信息失败:" + msg);
                }
            });
        } else {
            toast("本地用户为null,请登录。");
        }
    }

    /**
     * 验证旧密码是否正确
     * 涉及到数据库查询，因此需要有BmobQuery对象
     * 但此方法可由可无，因为修改密码初可能需要
     */
    private void checkPassword() {
        BmobQuery<User> query = new BmobQuery<User>();
        //获取本地用户
        final User bmobUser = BmobUser.getCurrentUser(this, User.class);
        // 如果你传的密码是正确的，那么arg0.size()的大小是1，这个就代表你输入的旧密码是正确的，
        // 否则是失败的
        //此处的“123456”应该从UI中获取，例如支付宝支付账款时要输入密码一样
        query.addWhereEqualTo("password", "123456");
        query.addWhereEqualTo("email", bmobUser.getEmail());
        query.findObjects(this, new FindListener<User>() {

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(List<User> arg0) {
                // TODO Auto-generated method stub
                toast("查询密码成功:" + arg0.size());
            }
        });
    }

    /**
     * 重置密码
     * 通过邮箱来重置密码
     * @Method resetPasswordByEmail(Context context, java.lang.String email,
     *                                                ResetPasswordByEmailListener listener)
     */
    private void testResetPassword() {
        //此处的“email”应该从UI中获取字符串b
        //final String email = "123456789@qq.com";
        BmobUser.resetPasswordByEmail(this, my_email, new ResetPasswordByEmailListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast("重置密码请求成功，请到" + my_email + "邮箱进行密码重置操作");
            }

            @Override
            public void onFailure(int code, String e) {
                // TODO Auto-generated method stub
                toast("重置密码失败:" + e);
            }
        });
    }

    /**
     * 验证邮件
     * 传说中的邮箱验证
     * @Method 	requestEmailVerify(Context context, java.lang.String email,
     *                                              EmailVerifyListener listener)
     * 用户注册时需要调用
     */
    private void emailVerify() {
        // email的内容应该从用户注册UI中获取
        //final String email = "75727433@qq.com";
        BmobUser.requestEmailVerify(this, my_email, new EmailVerifyListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast("请求验证邮件成功，请到" + my_email + "邮箱中进行激活账户。");
            }

            @Override
            public void onFailure(int code, String e) {
                // TODO Auto-generated method stub
                toast("请求验证邮件失败:" + e);
            }
        });
    }

    /**
     * 查询用户
     * 通过用户名来查询服务器端上的数据
     */
    private void testFindBmobUser() {
        BmobQuery<User> query = new BmobQuery<User>();
        //“lucky”应该从UI界面处获取
        query.addWhereEqualTo("email", "909392891@qq.com");
        query.findObjects(this, new FindListener<User>() {

            @Override
            public void onSuccess(List<User> object) {
                // TODO Auto-generated method stub
                toast("查询用户成功：" + object.size());

            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                toast("查询用户失败：" + msg);
            }
        });
    }

    /**
     * 通过邮箱和密码登录
     * 实际上，我们的APP是没有用户名的
     * @Method loginByAccount(Context context, java.lang.String account,
     *                         java.lang.String password, LogInListener<T> listener)
     * 参数中的account 等于 email
     */

    private void loginByEmailPwd(){
        BmobUser.loginByAccount(this, my_email, my_password, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                // TODO Auto-generated method stub
                if (user != null) {
                    toast(user.getEmail() + "登陆成功");
                    testGetCurrentUser();
                    Log.i("bmob", "" + user.getEmail() + "-" + user.getObjectId() + "-"
                            + user.getEmailVerified());
                }
            }
        });
    }

    /**修改当前用户密码
     * @Method updateCurrentUserPassword(Context context, java.lang.String oldPwd,
     *                          java.lang.String newPwd, UpdateListener listener)
     */
    private void updateCurrentUserPwd() {
        //测试时注意改回来
        final String new_password = "1234567";
        BmobUser.updateCurrentUserPassword(this, my_password, new_password, new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast("密码修改成功，可以用新密码进行登录");
                //此时应该自动退出，请求用户重新以新密码登录
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                toast("密码修改失败："+msg+"("+code+")");
            }
        });
    }

}
