using MarkIt.SignInAndSignUp.Model;
using MarkIt.Util;
using cn.bmob.json;
using System;
using System.Windows;


namespace MarkIt.SignInAndSignUp.View
{
    /// <summary>
    /// SignUpWindow.xaml 的交互逻辑
    /// </summary>
    public partial class SignUpWindow : Window
    {
        private Service service = Service.Instance;
        //判定邮箱注册成功
        Boolean emailSuccess = false;
        //判定密码一致
        Boolean samePassword = false;

        public SignUpWindow()
        {
            InitializeComponent();
        }

        //返回到登录界面
        private void backButton_Click(object sender, RoutedEventArgs e)
        {
            SignInWindow signInWindow = new SignInWindow();
            signInWindow.Show();
            this.Close();
        }

        private void comfirmSignUpButton_Click(object sender, RoutedEventArgs e)
        {
            if (signUpEmailTextBox.Text == "") { emailIsOrNotSignUp.Content = "邮箱为空"; }
            else if (signUpPasswordBox.Password == "") { matchThePassword.Content = "密码为空"; }
            else
            {
                //注册用户
                UserObject user = new UserObject();

                user.username = signUpEmailTextBox.Text;
                user.password = signUpPasswordBox.Password;
                user.email = signUpEmailTextBox.Text;

                //判断邮箱是否可用
                var future = service.Bmob.CreateTaskAsync<UserObject>(user);
                try
                {
                    string s = JsonAdapter.JSON.ToDebugJsonString(future.Result);
                    //    emailIsOrNotSignUp.Content = "邮箱可用";
                    emailSuccess = true;
                }
                catch
                {
                    emailIsOrNotSignUp.Content = "邮箱已注册";
                }

                //判断前后输入密码是否一致
                if (signUpPasswordBox.Password != comfirmSignUpPasswordBox.Password)
                {
                    matchThePassword.Content = "密码不一致";
                }
                else
                {
                    samePassword = true;
                    //    matchThePassword.Content = "密码一致";
                }

                //判断注册是否成功
                if (emailSuccess && samePassword)
                {
                    //发送激活邮件
                    var future1 = service.Bmob.EmailVerifyTaskAsync(user.username);
                    try
                    {
                        string s = JsonAdapter.JSON.ToDebugJsonString(future1.Result);
                        MessageBox.Show("注册成功，请检查邮件激活账号", "提示", MessageBoxButton.OK, MessageBoxImage.Information);

                        //跳转到登录窗口
                        SignInWindow signInWindow = new SignInWindow();
                        signInWindow.Show();
                        this.Close();
                    }
                    catch
                    {
                        MessageBox.Show("发送邮件失败", "提示", MessageBoxButton.OK, MessageBoxImage.Error);
                    }

                }

            }
        }
    }
}
