using System;
using System.Windows;

namespace MarkIt
{
    public partial class LoginAndSignUpWindow : Window
    {
        public LoginAndSignUpWindow()
        {
            InitializeComponent();

        }

        private void LogInButton_Click(object sender, RoutedEventArgs e)
        {
            this.DialogResult = Convert.ToBoolean(1);
            this.Close();
        }
    }
}
