using MarkIt.SignInAndSignUp.View;
using MarkIt.Util;
using System;
using System.Windows;
using System.Windows.Media.Imaging;
using cn.bmob.io;
using cn.bmob.json;

namespace MarkIt
{
    public partial class SignInWindow: Window
    {
        private Service service = Service.Instance;

        public SignInWindow()
        {
            InitializeComponent();

            Uri iconUri = new Uri("pack://application:,,,/Resources/logo.ico", UriKind.RelativeOrAbsolute);
            this.Icon = BitmapFrame.Create(iconUri);

        }

        //关闭窗口
        private void closeButton_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }

        //登录按钮，成功则跳转到这界面
        private void signInButton_Click(object sender, RoutedEventArgs e)
        {
            if(emailTextbox.Text == "") {
                emailMessage.Content = "邮箱为空";
            } else if(passwordBox.Password == "") {
                passwordMessage.Content = "密码为空";
            } else {
                var future = service.Bmob.LoginTaskAsync<BmobUser>(emailTextbox.Text, passwordBox.Password);
                try {
                    string s = JsonAdapter.JSON.ToDebugJsonString(future.Result);
                    //判断该账号是否验证
                    if("True" == BmobUser.CurrentUser.emailVerified.ToString()) {
                        //让用户进入主程序
                        MainWindow mainWindow = new MainWindow();
                        mainWindow.Show();
                        this.Close();
                    } else {
                        //继续停留在登录界面，待用户验证后再点击登录
                        emailMessage.Content = "账号未验证";
                    }
                } catch {
                    passwordMessage.Content = "账号或密码错误";

                }
            }
        }

        //跳转到注册界面
        private void signUpButton_Click(object sender, RoutedEventArgs e)
        {
            SignUpWindow signUpWindow = new SignUpWindow();
            signUpWindow.Show();
            this.Close();
        }

        private void forgetButton_Click(object sender, RoutedEventArgs e)
        {
            if(emailTextbox.Text != "") {
                var future = service.Bmob.ResetTaskAsync(emailTextbox.Text);
                try {
                    string s = JsonAdapter.JSON.ToDebugJsonString(future.Result);
                    passwordMessage.Content = "已发送邮件";
                } catch {
                    emailMessage.Content = "邮箱错误";
                }
            } else {
                emailMessage.Content = "邮箱为空";
            }
        }

        private void changeButton_Click(object sender, RoutedEventArgs e)
        {

        }

        //private void LogInButton_Click(object sender, RoutedEventArgs e)
        //{
        //    this.DialogResult = Convert.ToBoolean(1);
        //    this.Close();
        //}
    }
}
