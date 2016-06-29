using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace MarkIt.MainInterface
{
    public delegate void EditNoteDelegate(string text);

    public partial class NoteBox: Window
    {
        public NoteBox()
        {
            InitializeComponent();

            Uri iconUri = new Uri("pack://application:,,,/Resources/logo.ico", UriKind.RelativeOrAbsolute);
            this.Icon = BitmapFrame.Create(iconUri);
        }

        public event EditNoteDelegate editNoteDelegate;

        private void confirmButton_Click(object sender, RoutedEventArgs e)
        {
            editNoteDelegate(noteTextBox.Text);

            this.Close();
        }

        private void cancelButton_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }
}
