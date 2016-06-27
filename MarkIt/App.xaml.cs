using System.Windows;
using MarkIt.SignInAndSignUp;
using MarkIt.Util;

namespace MarkIt
{
    public partial class App : Application
    {
        private Service service = Service.Instance;

        private void Application_Startup(object sender, StartupEventArgs e)
        {

            service.Bmob.initialize("99a6b5c065255271a22d63836764b33b", "f805552192fbb9e448e734f4af1e5070");

            SignInWindow window = new SignInWindow();
        }


    }
}
