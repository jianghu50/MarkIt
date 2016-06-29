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
    public delegate void ContactsDidChangedDelegate(List<ContactObject> contacts);
    public delegate void NotesDidChangedDelegate(List<NoteObject> notes);

    class MainWindowViewModel
    {
        private Service service = Service.Instance;
        private BmobUser user;

        private const string contactTableName = "Contact";
        private const string noteTableName = "Note";

        public event ContactsDidChangedDelegate contactsDidChangedDelegate;
        public event NotesDidChangedDelegate notesDidChangedDelegate;

        public MainWindowViewModel(BmobUser user)
        {
            this.user = user;
        }
        
        // 添加联系人
        public void addContact(string name)
        {
            ContactObject contact = new ContactObject();
            contact.contactName = name;
            contact.user = user;

            service.Bmob.Create(contactTableName, contact, (resp, exception) =>
            {
                if(exception != null) {
                    MessageBox.Show("添加联系人失败, 失败原因为： " + exception.Message);
                    return;
                }
                queryAllContacts();
            });
        }

        // 删除联系人
        public void deleteContact(ContactObject contact)
        {
            service.Bmob.Delete(contactTableName, contact.objectId, (resp, exception) =>
            {
                if(exception != null) {
                    MessageBox.Show("删除联系人失败, 失败原因为： " + exception.Message);
                    return;
                }

                this.queryAllContacts();
            });
        }

        // 编辑联系人
        public void editContact(ContactObject contact, string newName)
        {
            ContactObject newContact = new ContactObject();
            newContact.contactName = newName;

            service.Bmob.Update(contactTableName, contact.objectId, newContact, (resp, exception) =>
            {
                if(exception != null) {
                    MessageBox.Show("编辑联系人, 失败原因为： " + exception.Message);
                    return;
                }

                this.queryAllContacts();
            });
        }

        // 查询所有联系人
        public void queryAllContacts()
        {
            var query = new BmobQuery();        
            query.WhereEqualTo("user", new BmobPointer<BmobUser>(user));            
            
            service.Bmob.Find<ContactObject>(contactTableName, query, (resp, exception) =>
            {
                if(exception != null) {
                    MessageBox.Show("查询联系人失败, 失败原因为： " + exception.Message);
                    return;
                }

                List<ContactObject> contacts = resp.results;
                contactsDidChangedDelegate(contacts);
            });
        }

        // 查询contact的所有聊天记录
        public void queryNote(ContactObject contact)
        {
            var query = new BmobQuery();
            query.OrderByDescending("updatedAt");
            query.WhereEqualTo("contact", new BmobPointer<ContactObject>(contact));
            service.Bmob.Find<NoteObject>(noteTableName, query, (resp, exception) => {
                if(exception != null) {
                    MessageBox.Show("查询聊天列表失败, 失败原因为： " + exception.Message);
                    return;
                }

                List<NoteObject> notes = resp.results;
                notesDidChangedDelegate(notes);
            });
        }

        // 添加聊天内容
        public void addNote(ContactObject contact, string noteContent)
        {
            NoteObject note = new NoteObject();
            note.contact = contact;
            note.text = noteContent;
            note.image = null;

            service.Bmob.Create(noteTableName, note, (resp, exception) =>
            {
                if(exception != null) {
                    MessageBox.Show("添加聊天内容失败, 失败原因为： " + exception.Message);
                    return;
                }

                this.queryNote(contact);
            });
        }

        // 删除聊天内容
        public void deleteNote(ContactObject contact, NoteObject note)
        {
            service.Bmob.Delete(noteTableName, note.objectId, (resp, exception) => {
                if(exception != null) {
                    MessageBox.Show("删除聊天内容失败, 失败原因为： " + exception.Message);
                    return;
                }

                this.queryNote(contact);
            });
        }

        // 编辑聊天内容
        public void editNote(ContactObject contact, NoteObject note, string newNoteText)
        {
            NoteObject newNote = new NoteObject();
            newNote.text = newNoteText;

            service.Bmob.Update(noteTableName, note.objectId, newNote, (resp, exception) => {
                if(exception != null) {
                    MessageBox.Show("编辑聊天内容, 失败原因为： " + exception.Message);
                    return;
                }

                this.queryNote(contact);
            });
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
