using System;
using System.Windows;
using System.Windows.Media;


namespace MarkIt
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();

            LoginAndSignUpWindow login = new LoginAndSignUpWindow();
            login.ShowDialog();
            if (login.DialogResult != Convert.ToBoolean(1))
            {
                this.Close();
            }

        }

    }

}
