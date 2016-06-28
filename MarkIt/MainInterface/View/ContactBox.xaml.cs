using System;
using System.Windows;
using System.Windows.Media.Imaging;

namespace MarkIt.MainInterface
{
    public delegate void AddContactDelegate(string text);
    public delegate void EditContactDelegate(string text);

    public partial class ContactBox: Window
    {
        bool isAdd = false;

        public ContactBox(bool isAdd)
        {
            InitializeComponent();

            Uri iconUri = new Uri("pack://application:,,,/Resources/logo.ico", UriKind.RelativeOrAbsolute);
            this.Icon = BitmapFrame.Create(iconUri);

            this.isAdd = isAdd;
        }

        public event AddContactDelegate didAddContactDelegate;
        public event EditContactDelegate editContactDelegate;

        private void confirmButton_Click(object sender, RoutedEventArgs e)
        {
            if(isAdd) {
                didAddContactDelegate(contactTextBox.Text);
            } else {
                editContactDelegate(contactTextBox.Text);
            }
            
            this.Close();
        }

        private void cancelButton_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }

}
