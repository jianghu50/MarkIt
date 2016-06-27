using cn.bmob.io;
using MarkIt.SignInAndSignUp;
using MarkIt.Util;
using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.IO.IsolatedStorage;
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

            viewModel.contactsDidChangedDelegate += new ContactsDidChangedDelegate(contactDidChangedAction);
            viewModel.queryAllContacts();

            contactsListBox.SelectionMode = SelectionMode.Single;
            contextMenu.Items.Add("删除联系人");        
        }

        private void contactDidChangedAction(string[] contacts)
        {
            Dispatcher.Invoke( delegate {
                contactsListBox.Items.Clear();
                foreach(string contact in contacts) {
                    contactsListBox.Items.Add(contact);
                }
                sortContacts();
            });
            
        }

        private void addContact_Click(object sender, RoutedEventArgs e)
        {
            AddContactBox contactWindow = new AddContactBox();
            contactWindow.didAddContactDelegate += new AddContactDelegate(didAddContactAction);
            contactWindow.ShowDialog();
        }

        private void didAddContactAction(string name)
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
                sortContacts();
            }
        }

        // 联系人排序
        private void sortContacts()
        {
            List<string> contacts = new List<string>();
            foreach(string contact in contactsListBox.Items) {
                contacts.Add(contact);
            }
            contacts.Sort();

            contactsListBox.Items.Clear();
            foreach(string contact in contacts) {
                contactsListBox.Items.Add(contact);
            }
        }

        private void contactsListBox_ContextMenuOpening(object sender, ContextMenuEventArgs e)
        {
            if(contactsListBox.SelectedItem == null)
                e.Handled = true;
        }

        private void signOut_Clicked(object sender, RoutedEventArgs e)
        {
            try {
                // 注销则清空user文件
                IsolatedStorageFile isolatedStorage = IsolatedStorageFile.GetUserStoreForAssembly();
                StreamWriter srWriter = new StreamWriter(new IsolatedStorageFileStream("user", FileMode.Truncate, FileAccess.Write, FileShare.Write, isolatedStorage));
                srWriter.Close();

                SignInWindow signInWindow = new SignInWindow();
                signInWindow.Show();
                this.Close();
            } catch(Exception sx) {
                MessageBox.Show("delete user file error!!!\n" + sx.Message);
                return;
            }
        }

        private void exit_Click(object sender, RoutedEventArgs e)
        {

            this.Close(); 
        }

        
    }

}
