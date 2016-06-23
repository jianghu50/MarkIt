using System;
using System.Windows;
using System.Windows.Media.Imaging;

namespace MarkIt.MainInterface
{
    public delegate void AddContactDelegate(string text);

    public partial class AddContactBox: Window
    {
        public AddContactBox()
        {
            InitializeComponent();

            Uri iconUri = new Uri("pack://application:,,,/Resources/logo.ico", UriKind.RelativeOrAbsolute);
            this.Icon = BitmapFrame.Create(iconUri);
        }

        public event AddContactDelegate didAddContactDelegate;

        private void confirmButton_Click(object sender, RoutedEventArgs e)
        {
            //触发事件，将联系人信息回传到listbox中
            didAddContactDelegate(contactTextBox.Text);
            this.Close();
        }

        private void cancelButton_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }

}
