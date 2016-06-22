using System;
using System.Collections;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Forms;
using System.Windows.Media.Imaging;

namespace MarkIt.MainInterface
{
    public partial class MainWindow: Window
    {
        MainWindowViewModel viewModel = new MainWindowViewModel();

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

            //listBox.Items.Add("一哥");
            //listBox.Items.Add("二哥");

            contactsListBox.SelectionMode = System.Windows.Controls.SelectionMode.Single;
            contextMenu.Items.Add("删除联系人");
        }

        private void CreateContact_Click(object sender, RoutedEventArgs e)
        {
            AddContactBox contactWindow = new AddContactBox();

            //注册contactWindow_MyEvent方法的MyEvent事件
            contactWindow.MyEvent += new MyDelegate(contactWindow_MyEvent);
            contactWindow.ShowDialog();
        }

        private void listBox_SelectionChanged(object sender, System.Windows.Controls.SelectionChangedEventArgs e)
        {

        }

        // 联系人排序
        private void Sorted(System.Windows.Controls.ListBox listBox)
        {
            ArrayList contacts = new ArrayList();
            foreach(String o in listBox.Items) {
                contacts.Add(o);
            }
            contacts.Sort();
            listBox.Items.Clear();
            foreach(String o in contacts) {
                listBox.Items.Add(o);
            }
        }

        //处理
        void contactWindow_MyEvent(string text)
        {
            bool isOrNotRepeated = false;
            //判断联系人姓名是否重复
            foreach(String o in contactsListBox.Items) {

                if(o.Equals(text)) {
                    System.Windows.MessageBox.Show("联系人姓名重复，请重新输入", "提示", MessageBoxButton.OK, MessageBoxImage.Information);
                    isOrNotRepeated = true;
                }
            }
            if(isOrNotRepeated == false) {
                this.contactsListBox.Items.Add(text);
                Sorted(contactsListBox);
            }
        }

        private void contactsListBox_ContextMenuOpening(object sender, ContextMenuEventArgs e)
        {
            if(contactsListBox.SelectedItem == null)
                e.Handled = true;
        }
    }

}
