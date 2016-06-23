using cn.bmob.io;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media.Imaging;

namespace MarkIt.MainInterface
{
    public partial class MainWindow: Window
    {
        MainWindowViewModel viewModel = new MainWindowViewModel(BmobUser.CurrentUser);

        public MainWindow()
        {
            InitializeComponent();

            Uri iconUri = new Uri("pack://application:,,,/Resources/logo.ico", UriKind.RelativeOrAbsolute);
            this.Icon = BitmapFrame.Create(iconUri);

            List<ContactObject> contacts = viewModel.queryAllContacts();
            //string str = "";
            //foreach(ContactObject contact in contacts) {
            //    str += contact.contactName + "\n";
            //}
            //MessageBox.Show("所有联系人：\n" + str);

            contactsListBox.SelectionMode = SelectionMode.Single;
            contextMenu.Items.Add("删除联系人");        
        }

        private void createContact_Click(object sender, RoutedEventArgs e)
        {
            AddContactBox contactWindow = new AddContactBox();

            //注册contactWindow_MyEvent方法的MyEvent事件
            contactWindow.didAddContact += new AddContactDelegate(didAddContactAction);
            contactWindow.ShowDialog();
        }

        void didAddContactAction(string name)
        {
            bool isRepeated = false;
            //判断联系人姓名是否重复
            foreach(String contact in contactsListBox.Items) {
                if(contact.Equals(name)) {
                    MessageBox.Show("联系人姓名重复，请重新输入", "提示", MessageBoxButton.OK, MessageBoxImage.Information);
                    isRepeated = true;
                    break;
                }
            }

            if(isRepeated == false) {
                contactsListBox.Items.Add(name);
                viewModel.addContact(name);
                ArrayList contacts = sortContacts(contactsListBox.Items);
                contactsListBox.Items.Clear();
                foreach(String contact in contacts) {
                    contactsListBox.Items.Add(contact);
                }
            }
        }

        // 联系人排序
        private ArrayList sortContacts(ItemCollection contacts)
        {
            ArrayList list = new ArrayList();
            foreach(String contact in contacts) {
                list.Add(contact);
            }
            list.Sort();
            return list; 
        }

        private void contactsListBox_ContextMenuOpening(object sender, ContextMenuEventArgs e)
        {
            if(contactsListBox.SelectedItem == null)
                e.Handled = true;
        }
    }

}
