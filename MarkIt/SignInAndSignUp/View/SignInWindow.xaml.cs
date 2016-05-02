using MarkIt.SignInAndSignUp.View;
using System;
using System.Windows;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace MarkIt
{
    public partial class SignInWindow : Window
    {
        public SignInWindow()
        {
            InitializeComponent();

            Uri iconUri = new Uri("pack://application:,,,/Resources/logo.ico", UriKind.RelativeOrAbsolute);
            this.Icon = BitmapFrame.Create(iconUri);

        }

        private void closeButton_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }

        private void signInButton_Click(object sender, RoutedEventArgs e)
        {

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

        }

        //private void LogInButton_Click(object sender, RoutedEventArgs e)
        //{
        //    this.DialogResult = Convert.ToBoolean(1);
        //    this.Close();
        //}
    }
}
