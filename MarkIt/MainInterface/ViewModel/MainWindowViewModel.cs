using cn.bmob.io;
using cn.bmob.json;
using MarkIt.SignInAndSignUp.Model;
using MarkIt.Util;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Windows;

namespace MarkIt.MainInterface
{
    class MainWindowViewModel
    {
        private Service service = Service.Instance;
        private BmobUser user;

        private const String contactTableName = "Contact";
        private const String noteTableName = "Note";

        public MainWindowViewModel(BmobUser user)
        {
            this.user = user;
        }
        
        // 添加联系人
        public void addContact(String name)
        {
            ContactObject contact = new ContactObject();
            contact.contactName = name;
            contact.user = user;

            var future = service.Bmob.CreateTaskAsync<ContactObject>(contact);
            try {
                string s = JsonAdapter.JSON.ToDebugJsonString(future.Result);
                MessageBox.Show("添加联系人成功\ncontact id："+future.Result.objectId);
            } catch {
                MessageBox.Show("创建失败，原因：" + future.Exception.InnerException.ToString());
            }
        }

        // 查询所有联系人
        public List<ContactObject> queryAllContacts()
        {
            var query = new BmobQuery();        
            query.WhereEqualTo("user", new BmobPointer<BmobUser>(user));
            //query.OrderByDescending("updatedAt");
            List<ContactObject> contacts = new List<ContactObject>();
            service.Bmob.Find<ContactObject>(contactTableName, query, (resp, exception) =>
            {
                if(exception != null) {
                    MessageBox.Show("查询失败, 失败原因为： " + exception.Message);
                    return;
                }
                contacts = resp.results;
            });

            string str = "";
            foreach(ContactObject contact in contacts) {
                str += contact.contactName;
            }
            MessageBox.Show("人数：" + contacts.Count + "所有联系人：\n");

            return contacts;
        }

        //编辑联系人
        private void EditContact()
        {
            //在构造的时候指定了数据表
            ContactObject contact = new ContactObject();
            contact.contactName = "你要修改的信息";
            contact.objectId = "你要修改的联系人的ID";
            //有2种新建方法，带有Task功能
            //public Task<UpdateCallbackData> UpdateTaskAsync(string tablename, string objectId, IBmobWritable data);
            //public Task<UpdateCallbackData> UpdateTaskAsync<T>(T data) where T : BmobTable;
            //保存数据，采用第二种方法，返回的future可以输出信息
            var future = service.Bmob.UpdateTaskAsync<ContactObject>(contact);

        }

        //删除联系人
        private void DeleteContact()
        {
            //在构造的时候指定了数据表
            ContactObject contact = new ContactObject();
            contact.objectId = "你要删除的联系人的ID";
            //有2种新建方法，带有Task功能
            //public Task<DeleteCallbackData> DeleteTaskAsync(string tablename, string objectId);
            //public Task<DeleteCallbackData> DeleteTaskAsync<T>(T data) where T : BmobTable;
            //保存数据，采用第二种方法,返回的future可以输出信息
            var future = service.Bmob.DeleteTaskAsync<ContactObject>(contact);
        }

        //新建笔记文本
        private void CreateNoteText()
        {
            //在构造的时候指定了数据表
            ContactObject contact = new ContactObject();
            contact.objectId = "你要绑定的联系人的ID";
            //在构造时指定了数据表
            NoteObject note = new NoteObject();
            //添加数据关联，即添加外键
            note.contact = contact;
            note.text = "用户输入的文本信息(可能含表情)";
            //因为是纯文本，无图片
            note.image = null;
            //有2种新建方法，带有Task功能
            //public Task<CreateCallbackData> CreateTaskAsync(string tablename, IBmobWritable data);data=contact
            //public Task<CreateCallbackData> CreateTaskAsync<T>(T data) where T : BmobTable;
            //保存数据，采用第二种方法,返回的future可以输出信息
            var future = service.Bmob.CreateTaskAsync<NoteObject>(note);
            // 用 future 来输出成功与否的信息
            //try
            //{
            //    string s = JsonAdapter.JSON.ToDebugJsonString(future.Result);
            //    Console.WriteLine(s);
            //}
            //catch
            //{
            //    MessageBox.Show("创建失败，原因：" + future.Exception.InnerException.ToString());
            //}

        }

        //新建笔记图片,方法的前面添加了 async 关键字
        private async void CreateNoteImage()
        {
            //先上传,此时的 Result是一个URL地址，可以存于本地数据库，其实我们的云端数据库上存储的也是url才对...
            var Result = await service.Bmob.FileUploadTaskAsync("本地图片的地址");
            //在构造的时候指定了数据表
            ContactObject contact = new ContactObject();
            contact.objectId = "你要绑定的联系人的ID";
            //在构造时指定了数据表
            NoteObject note = new NoteObject();
            //添加数据关联，即添加外键
            note.contact = contact;
            //文本应该为空，到时候便于确认是文本信息还是图片信息，但是也可以弄多个字段出来标识type。
            //可以讨论
            note.text = "";
            //设置图片
            note.image = Result;
            //有2种新建方法，带有Task功能
            //public Task<CreateCallbackData> CreateTaskAsync(string tablename, IBmobWritable data);data=contact
            //public Task<CreateCallbackData> CreateTaskAsync<T>(T data) where T : BmobTable;
            //保存数据，采用第二种方法,返回的future可以输出信息
            var future = service.Bmob.CreateTaskAsync<NoteObject>(note);
            //网站上说用下面这种办法..然而我不会。
            // callback方式异步请求处理，非阻塞访问
            //service.Bmob.Create<TestNoteObject>(note, (resp, ex) =>
            //{
            //    // http://msdn.microsoft.com/en-us/library/vstudio/zyzhdc6b(v=vs.100).aspx
            //    Invoke(new doUiThread(
            //        () =>
            //        {

            //        }
            //        ));
            //});
        }
        //跟上面那个乱七八糟的方法是同一条船的。
        //delegate void doUiThread();

        

        private void QueryNote()
        {
            //查找表中的全部数据（默认最多返回10条数据）,方便分页查询，后面我贴出分页查询的方法
            //先定义个查询对象
            var query = new BmobQuery();
            //按发布时间降序排列
            query.OrderByDescending("updatedAt");
            //获取当前点击的联系人信息
            //TestContactObject contact = UI中点击获取
            //查询当前用户的所有联系人，第一个参数为对应的字段，请注意
            //query.WhereEqualTo("contact", new BmobPointer<TestContactObject>(contact));
            // or use
            // query.WhereMatchesQuery("user", user);
            //查询的结果在future中
            var future = service.Bmob.FindTaskAsync<NoteObject>(noteTableName, query);
            //对返回结果进行处理,future本身就是一个list了。 不过可能需要类型转换一下，我也不会。
        }

        /**
         * 在数据比较多的情况下，你往往需要显示加载一部分数据就可以了，这样可以节省用户的流量和提升数据加载速度，
         * 提高用户体验。这时候，我们使用Limit方法就可以限制查询结果的数据条数。默认情况下Limit的值为10，示例代码如下：
         * BmobQuery query = new BmobQuery();
         * //设置最多返回20条记录
         * query.Limit("20");
         * 在Limit的基础上进行分页显示数据的一个比较合理的解决办法是：使用SKip方法，跳过前多少条数据。
         * 默认情况下Skip的值为10，示例代码如下：
         * BmobQuery query = new BmobQuery();
         * //忽略前20条数据
         * query.Skip(20); 
         * */

        /**
         * 关于图片的下载，在源码上找不到下载的方法，大概是通过本地来从网络读取url了，
         * 如果是存在本地数据库，一般是把图片复制到本地数据库中然后
         * 存储本地地址。即本地数据库最好是存储 本地地址+网络地址。
         * */

    }
}
