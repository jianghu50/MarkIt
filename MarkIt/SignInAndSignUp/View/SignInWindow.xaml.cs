using MarkIt.MainInterface;
using MarkIt.Util;
using System;
using System.Windows;
using System.Windows.Media.Imaging;
using cn.bmob.io;
using cn.bmob.json;
using System.Windows.Forms;
using System.IO.IsolatedStorage;
using System.IO;

namespace MarkIt.SignInAndSignUp
{
    public partial class SignInWindow: Window
    {
        private Service service = Service.Instance;

        // MARK: - Lifecycle

        public SignInWindow()
        {
            InitializeComponent();

            Uri iconUri = new Uri("pack://application:,,,/Resources/logo.ico", UriKind.RelativeOrAbsolute);
            this.Icon = BitmapFrame.Create(iconUri);

            // 查看保存的账号密码
            try {
                IsolatedStorageFile isolatedStorage = IsolatedStorageFile.GetUserStoreForAssembly();
                StreamReader srReader = new StreamReader(new IsolatedStorageFileStream("user", FileMode.OpenOrCreate, FileAccess.Read, FileShare.Read, isolatedStorage));
                if(!srReader.EndOfStream) {
                    string email = srReader.ReadLine();
                    string password = srReader.ReadLine();
                    if(loginWithEmailAndPassword(email, password)) {
                        showMainWindow();
                    } else {
                        this.Show();
                    }
                } else {
                    this.Show();
                }
                srReader.Close();
            } catch(Exception ex) {
                System.Windows.MessageBox.Show("Read email and password error!!!\n" + ex.Message);
                throw;
            }
        }

        // MARK: - Actions

        private void signInButton_Click(object sender, RoutedEventArgs e)
        {
            if(emailTextbox.Text == "") {
                emailMessage.Content = "请你先输入邮箱";
            } else if(passwordBox.Password == "") {
                passwordMessage.Content = "请你先输入密码";
            } else {
                string email = emailTextbox.Text;
                string password = passwordBox.Password;
                if(loginWithEmailAndPassword(email, password) == true) {
                    // 保存用户登录信息
                    if(rememberCheckbox.IsChecked == true) {
                        try {
                            IsolatedStorageFile isolatedStorage = IsolatedStorageFile.GetUserStoreForAssembly();
                            StreamWriter srWriter = new StreamWriter(new IsolatedStorageFileStream("user", FileMode.Open, FileAccess.Write, FileShare.Write, isolatedStorage));
                            srWriter.WriteLine(emailTextbox.Text.Trim());
                            srWriter.WriteLine(passwordBox.Password);
                            srWriter.Flush();
                            srWriter.Close();
                        } catch(Exception sx) {
                            System.Windows.MessageBox.Show("Write email and password error!!!\n" + sx.Message);
                            return;
                        }    
                    }
                    showMainWindow();
                }
            }
        }

        private bool loginWithEmailAndPassword(String email, String password)
        {
            var future = service.Bmob.LoginTaskAsync<BmobUser>(email, password);
            try {
                string s = JsonAdapter.JSON.ToDebugJsonString(future.Result);
                if("True" == BmobUser.CurrentUser.emailVerified.ToString()) {
                    return true;
                }
            } catch {
                passwordMessage.Content = "账号或密码错误";
            }
            return false;
        }

        private void showMainWindow()
        {
            MainWindow mainWindow = new MainWindow();
            mainWindow.Show();
            this.Close();
        }


        // 跳转到注册界面
        private void signUpButton_Click(object sender, RoutedEventArgs e)
        {
            SignUpWindow signUpWindow = new SignUpWindow();
            signUpWindow.Show();
            this.Close();
        }

        private void forgetPasswordButton_Click(object sender, RoutedEventArgs e)
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

        private void changePasswordButton_Click(object sender, RoutedEventArgs e)
        {

        }

        private void closeButton_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }

    }
}
