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

            listBox.Items.Add("一哥");
            listBox.Items.Add("二哥");
            listBox.Items.Add("三哥");
            listBox.Items.Add("四哥");
            listBox.Items.Add("五哥");
            listBox.Items.Add("六哥");
            listBox.Items.Add("七哥");
            listBox.Items.Add("八哥");
            listBox.Items.Add("九哥");
            listBox.Items.Add("十哥");
            listBox.Items.Add("十一哥");
            listBox.Items.Add("十二哥");
            listBox.Items.Add("十三哥");
            listBox.Items.Add("十四哥");
            listBox.Items.Add("十五哥");
            listBox.Items.Add("十六哥");
        }

        private void MenuItem_Click(object sender, RoutedEventArgs e)
        {

        }
    }

}
