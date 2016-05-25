using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;

using cn.bmob.api;
using cn.bmob.io;
using cn.bmob.json;
using cn.bmob.tools;

using MarkIt.SignInAndSignUp.View;
using MarkIt.Util;

namespace MarkIt
{
    /// <summary>
    /// App.xaml 的交互逻辑
    /// </summary>
    public partial class App : Application
    {
        private Service service = Service.Instance;

        private void Application_Startup(object sender, StartupEventArgs e)
        {

            service.Bmob.initialize("99a6b5c065255271a22d63836764b33b", "f805552192fbb9e448e734f4af1e5070");

            //在这里打开窗体。
            SignInWindow window = new SignInWindow();
            window.Show();

          //  TestUserData window = new TestUserData();
          //  window.Show();
        }


    }
}
