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

using MarkIt;
using cn.bmob.exception;
using cn.bmob.io;
using cn.bmob.json;
using cn.bmob.tools;

using MarkIt.Util;
using MarkIt.SignInAndSignUp.Model;

namespace MarkIt.SignInAndSignUp.View
{
    /// <summary>
    /// Interaction logic for TestUserData.xaml
    /// 这个测试类只有简单的 注册 登录 修改密码 验证邮箱，以后还需要添加上传用户信息，即同步这个功能，建议不要删除这个测试类先。
    /// 有什么问题可以@Kasper联系
    /// </summary>
    public partial class TestUserData : Window
    {
        //这里用我自己的号
        private string userName = "73124589@qq.com";
        private string passWord = "123456";
        private string testInfo = "测试信息：";
        private Service service = Service.Instance;

        public TestUserData()
        {
            InitializeComponent();
        }

        /// <summary>
        /// 注册 @method service.Bmob.CreateTaskAsync<TestUserObject>(TestUserObject user)
        /// 注意 用户的用户名不能为空，我们只能人为地把用户的用户名设置地跟邮箱一样
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btn1_Click(object sender, RoutedEventArgs e)
        {
            //注册用户
            UserObject user = new UserObject();
            //示例
            //user.username = username.Text;
            //user.password = password.Text;
            //user.sex = new BmobBoolean(false);
            //user.age = 50;
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!
            user.username = userName;
            user.email = userName;
            user.password = passWord;
            //输出测试信息
            Console.WriteLine(testInfo);
            var future = service.Bmob.CreateTaskAsync<UserObject>(user);
            try
            {
                string s = JsonAdapter.JSON.ToDebugJsonString(future.Result);
                Console.WriteLine(s);
            }
            catch
            {
                MessageBox.Show("注册失败，原因：" + future.Exception.InnerException.ToString());
            }
        }

        /// <summary>
        /// 登录 @method service.Bmob.LoginTaskAsync<BmobUser>(string username, string password)
        /// 其实，登录前应该要判断邮箱有没有验证的。我这里添加上判断方法吧。
        /// 不知道你们有什么想法，可以@Kasper讨论
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btn2_Click(object sender, RoutedEventArgs e)
        {
            //输出测试信息
            Console.WriteLine(testInfo);
            var future = service.Bmob.LoginTaskAsync<BmobUser>(userName, passWord);
            try
            {
                string s = JsonAdapter.JSON.ToDebugJsonString(future.Result);
                Console.WriteLine(s);
            }
            catch
            {
                MessageBox.Show("登录失败，原因：" + future.Exception.InnerException.ToString());
            }
            Console.WriteLine(JsonAdapter.JSON.ToDebugJsonString(BmobUser.CurrentUser));
            //判断有没有验证是需要登录后才能判断的
            //if ( "true" == BmobUser.CurrentUser.emailVerified.ToString())
            //{
                //让用户进入主程序
            //}
            //else
            //{
                //继续停留在登录界面，待用户验证后再点击登录
            //}
        }

        /// <summary>
        /// 修改密码 -- 忘记密码 发送邮件到邮箱
        /// @Method service.Bmob.ResetTaskAsync(string email/userName)
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btn3_Click(object sender, RoutedEventArgs e)
        {
            //输出测试信息
            Console.WriteLine(testInfo);
            //userName = Email
            var future = service.Bmob.ResetTaskAsync(userName);
            try
            {
                string s = JsonAdapter.JSON.ToDebugJsonString(future.Result);
                Console.WriteLine(s);
            }
            catch
            {
                MessageBox.Show("发送邮件失败，原因：" + future.Exception.InnerException.ToString());
            }
        }

        /// <summary>
        /// 退出登录
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btn4_Click(object sender, RoutedEventArgs e)
        {
            BmobUser.LogOut();
            //输出测试信息
            Console.WriteLine(testInfo);
            Console.WriteLine(JsonAdapter.JSON.ToDebugJsonString(BmobUser.CurrentUser));
        }

        /// <summary>
        /// 验证新用户的邮箱 这里不用担心同个邮箱可以注册多个账户，因为username是唯一的，同时username = email了。
        /// 注意，最好在验证前判断当前用户是不是已经验证了
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btn5_Click(object sender, RoutedEventArgs e)
        {
            //输出测试信息
            Console.WriteLine(testInfo);

            //userName = Email
            var future = service.Bmob.EmailVerifyTaskAsync(userName);
            try
            {
                string s = JsonAdapter.JSON.ToDebugJsonString(future.Result);
                Console.WriteLine(s);
            }
            catch
            {
                MessageBox.Show("验证邮件发送失败，原因：" + future.Exception.InnerException.ToString());
            }
        }
    }
}
