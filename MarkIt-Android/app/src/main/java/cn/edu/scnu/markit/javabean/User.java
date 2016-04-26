package cn.edu.scnu.markit.javabean;

import cn.bmob.v3.BmobUser;

/**
 * Created by Kasper on 2016/4/21.
 * 继承BmobUser类，直接使用 username,password,email,emailVerified 即可
 * BmobUser除了从BmobObject继承的属性外，还有几个特定的属性：
 * username: 用户的用户名（必需）。
 * password: 用户的密码（必需）。
 * email: 用户的电子邮件地址（可选）。
 * emailVerified:邮箱认证状态（可选）。
 * mobilePhoneNumber：手机号码（可选）。
 * mobilePhoneNumberVerified：手机号码的认证状态（可选）。
 */
public class User extends BmobUser{

}
