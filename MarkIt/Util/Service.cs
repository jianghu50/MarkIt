using cn.bmob.api;

namespace MarkIt.Util
{
    /// <summary>
    /// 这个类就当做是服务器，提供单例模式，里面有Bmob对象以便其他窗口类调用。
    /// 这个是@Kasper想出来的应急的方法，如果以后有其他的方法来调用Bmob对象的话可以考虑去掉这种模式。
    /// </summary>
    class Service
    {
        private static Service instance;

        private BmobWindows bmob;

        private Service()
        {
            bmob = new BmobWindows();
        }

        public static Service Instance
        {
            get 
            {
                if (instance == null)
                    instance = new Service();
                return instance;
            }
        }

        public BmobWindows Bmob
        {
            get { return bmob; }
        }
    }
}
