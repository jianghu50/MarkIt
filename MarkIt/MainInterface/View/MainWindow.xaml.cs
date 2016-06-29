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
using Microsoft.Office.Interop.Word;
using System.Reflection;

namespace MarkIt.MainInterface
{
    public partial class MainWindow: System.Windows.Window
    {
        MainWindowViewModel viewModel = new MainWindowViewModel(BmobUser.CurrentUser);
        List<ContactObject> contacts = new List<ContactObject>();

        public MainWindow()
        {
            InitializeComponent();

            Uri iconUri = new Uri("pack://application:,,,/Resources/logo.ico", UriKind.RelativeOrAbsolute);
            this.Icon = BitmapFrame.Create(iconUri);

            viewModel.contactsDidChangedDelegate += new ContactsDidChangedDelegate(contactDidChangedAction);
            viewModel.queryAllContacts();

            viewModel.notesDidChangedDelegate += new NotesDidChangedDelegate(notesDidChangedAction);

            contactsListBox.SelectionMode = SelectionMode.Single;
        }

        private void contactDidChangedAction(List<ContactObject> contacts)
        {
            Dispatcher.Invoke(delegate {
                this.contacts = contacts;
                contactsListBox.Items.Clear();
                foreach(ContactObject contact in contacts) {
                    contactsListBox.Items.Add(contact.contactName);
                }
                //sortContacts();
            });
        }

        private void addContact_Click(object sender, RoutedEventArgs e)
        {
            ContactBox contactWindow = new ContactBox(true);
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
                //sortContacts();
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

        private void contactsListBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            int selectedIndex = contactsListBox.SelectedIndex;
            if(selectedIndex != -1) {
                noteListBox.Items.Clear();
                viewModel.queryNote(contacts[selectedIndex]);
            }
        }

        private void notesDidChangedAction(List<NoteObject> notes)
        {
            Dispatcher.Invoke(delegate {
                noteListBox.Items.Clear();
                foreach(NoteObject note in notes) {
                    noteListBox.Items.Add(note);
                }
            });
        }

        private void addNoteButton_Click(object sender, RoutedEventArgs e)
        {
            int selectedIndex = contactsListBox.SelectedIndex;
            if(selectedIndex != -1) {
                viewModel.addNote(contacts[selectedIndex], noteTextBox.Text);
                noteTextBox.Clear();
            }
        }

        private void deleteContact_Click(object sender, RoutedEventArgs e)
        {
            int selectedIndex = contactsListBox.SelectedIndex;
            if(selectedIndex != -1) {
                viewModel.deleteContact(contacts[selectedIndex]);
                noteListBox.Items.Clear();
            }
        }

        private void editContact_Click(object sender, RoutedEventArgs e)
        {
            int selectedIndex = contactsListBox.SelectedIndex;
            if(selectedIndex != -1) {
                ContactBox contactWindow = new ContactBox(false);
                contactWindow.contactTextBox.Text = contacts[selectedIndex].contactName;
                contactWindow.editContactDelegate += new EditContactDelegate(didEditContactAction);
                contactWindow.ShowDialog();
            }
        }

        private void didEditContactAction(string name)
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
                int selectedIndex = contactsListBox.SelectedIndex;
                viewModel.editContact(contacts[selectedIndex], name);
            }
        }

        // 同步
        private void sync_Click(object sender, RoutedEventArgs e)
        {
            viewModel.queryAllContacts();
        }

        // 导出
        private void export_Click(object sender, RoutedEventArgs e)
        {
            object oMissing = Missing.Value;
            object oEndOfDoc = "\\endofdoc"; /* \endofdoc is a predefined bookmark */

            _Application oWord;
            _Document oDoc;
            oWord = new Microsoft.Office.Interop.Word.Application();
            oWord.Visible = true;
            oDoc = oWord.Documents.Add(ref oMissing, ref oMissing,
                ref oMissing, ref oMissing);

            for(int i = 0; i < noteListBox.Items.Count; i++) {
                Paragraph oPara1;
                oPara1 = oDoc.Content.Paragraphs.Add(ref oMissing);
                NoteObject note = (NoteObject)noteListBox.Items[i];
                oPara1.Range.Text = note.updatedAt + "\n" + note.text+"\n";
                oPara1.Range.InsertParagraphAfter();
            }
        }

        private void copyNote_Click(object sender, RoutedEventArgs e)
        {
            NoteObject note = (NoteObject)noteListBox.Items[noteListBox.SelectedIndex];
            Clipboard.SetText(note.text);
        }

        private void editNote_Click(object sender, RoutedEventArgs e)
        {
            int selectedIndex = noteListBox.SelectedIndex;
            if(selectedIndex != -1) {
                NoteBox noteWindow = new NoteBox();
                NoteObject note = (NoteObject)noteListBox.Items[selectedIndex];
                noteWindow.noteTextBox.Text = note.text;
                noteWindow.editNoteDelegate += new EditNoteDelegate(didEditNoteAction);
                noteWindow.ShowDialog();
            }
        }

        private void didEditNoteAction(string newNoteText)
        {
            ContactObject contact = this.contacts[contactsListBox.SelectedIndex];
            NoteObject note = (NoteObject)noteListBox.Items[noteListBox.SelectedIndex];
            viewModel.editNote(contact,note, newNoteText);
        }

        private void deleteNote_Click(object sender, RoutedEventArgs e)
        {
            int selectedIndex = contactsListBox.SelectedIndex;
            if(selectedIndex != -1) {
                ContactObject contact = this.contacts[selectedIndex];
                NoteObject note = (NoteObject)noteListBox.Items[noteListBox.SelectedIndex];
                viewModel.deleteNote(contact, note);
            }

        }
    }

}
