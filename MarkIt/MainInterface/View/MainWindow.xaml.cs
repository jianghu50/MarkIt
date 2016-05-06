using System;
using System.Windows;
using System.Windows.Media;
using System.Windows.Media.Imaging;

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

            Uri iconUri = new Uri("pack://application:,,,/Resources/logo.ico", UriKind.RelativeOrAbsolute);
            this.Icon = BitmapFrame.Create(iconUri);

            //LoginAndSignUpWindow login = new LoginAndSignUpWindow();
            //login.ShowDialog();
            //if (login.DialogResult != Convert.ToBoolean(1))
            //{
            //    this.Close();
            //}



        }

    }

}
