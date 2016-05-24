package cn.edu.scnu.markit.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.edu.scnu.markit.R;
import cn.edu.scnu.markit.javabean.Contact;
import cn.edu.scnu.markit.javabean.Note;
import cn.edu.scnu.markit.javabean.User;

/**
 * Created by Kasper on 2016/5/22.
 * 没有测试过，有任何问题请随时联系
 */
public class TestContactActivity extends TestBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_contact_activity);

        mListview = (ListView) findViewById(R.id.contact_listview);
        mAdapter = new ArrayAdapter<String>(this, R.layout.test_user_list_item,
                R.id.tv_item, getResources().getStringArray(R.array.bmob_contact_list));
        mListview.setAdapter(mAdapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                testBmob(position + 1);
            }
        });

        final User myUser = new User();
        //把邮箱同时设置为Username
        myUser.setUsername("1395872519@qq.com");
        myUser.setPassword("123456");
        myUser.login(this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast("登录成功:" + myUser.getEmail() + "-"
                        + myUser.getObjectId() + "-" + myUser.getCreatedAt()
                        + "-" + myUser.getSessionToken() + ",是否验证：" + myUser.getEmailVerified());
                //接下来应该进行验证邮箱或者其他UI跳转
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                toast("登录失败:" + msg);
            }
        });
    }

    private void testBmob(int pos) {
        switch (pos) {
            case 1://新建联系人
                testCreateContact();
                break;
            case 2://编辑联系人
                testEditContact();
                break;
            case 3://删除联系人
                testDeleteContact();
                break;
            case 4://新建文本笔记
                testCreateNote();
                break;
            case 5://添加图片
                testCreateImg();
                break;
            default:
                break;
        }
    }

    /**
     * 新建联系人
     */
    private void testCreateContact(){
        User myUser = BmobUser.getCurrentUser(this, User.class);
        //新建联系人
        Contact contact = new Contact();
        //设置姓名
        contact.setContactName("张三");
        //添加一对多关联
        contact.setUser(myUser);
        //将数据上传到云端：save(Context context,SaveListener listener)
        contact.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                //添加成功后可以在我们的服务器上查看到该数据。
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    /**
     * 编辑联系人
     */
    private void testEditContact(){
//        //编辑联系人的话，需要知道当前联系人的ID，一般而言，从云端中查询到该用户的所有联系人
//        //然后通过adapter将所有联系人显示出来，用户点击其中一个联系人，这时才获取到该联系人
//        //contact类对象，然后获取到该联系人的ID，
//        Contact c = new Contact();
//        c.setContactName("李四");
//        //update(Context context, objectID, UpdateListener listener)
//        c.update(TestContactActivity.this, ContactID, new UpdateListener() {
//            @Override
//            public void onSuccess() {
//                //更改联系人信息后的操作.....
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//
//            }
//        });
    }

    /**
     * 删除联系人
     */
    private void testDeleteContact(){
//        //删除联系人的话，跟编辑联系人信息一样需要知道当前联系人的ID，一般而言，从云端中查询到
//        // 该用户的所有联系人
//        //然后通过adapter将所有联系人显示出来，用户点击其中一个联系人，这时才获取到该联系人
//        //contact类对象，然后获取到该联系人的ID，然后再进行删除
//        Contact c = new Contact();
//        c.setObjectId("获取到的ID");
//        //delete(Context context, DeleteListener d)
//        c.delete(TestContactActivity.this, new DeleteListener() {
//            @Override
//            public void onSuccess() {
//                //删除成功后的操作.....
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//
//            }
//        });
    }

    /**
     * 新建笔记纯文本
     */
    private void testCreateNote(){
        Contact c = new Contact();
        c.setObjectId("获取到的ID");
        final Note note = new Note();
        note.setContact(c);//添加一对多关系，绑定联系人
        note.setText("文本笔记");
        note.setImage(null);//没有图片
        note.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                //新建成功后的操作...
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    /**
     * 新建笔记，图片类型
     */
    private void testCreateImg(){
        //picPath应该打开本地图库，获取本地图片的地址
        String picPath = "sdcard/temp.jpg";
        //用JAVA对象File生成bmob文件类型
        BmobFile bmobFile = new BmobFile(new File(picPath));
        //uploadlock(Context,UploadFileListener listener)
        bmobFile.uploadblock(this, new UploadFileListener() {

            @Override
            public void onSuccess() {
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                //其实，能拿到上传文件的完整地址的话，看下是不是网络地址，如果是网络地址，
                //那本地数据库里面的图片类型完全可以定义为String类型，因为怎样都是要从本地获取到
                //图片的地址“sdcard/temp.jpg”，如果返回网络地址，直接修改本地数据库里面的图片地址为：
                //本地地址+ “ & ”+ getFileUrl(context)，以后在本地加载就可以直接读本地地址，然是如果
                //在本地删除了那张图片，那我们还可以从网络上下载下来。这点Android小组自己讨论咯~
                //toast("上传文件成功:" + bmobFile.getFileUrl(this));
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }

            @Override
            public void onFailure(int code, String msg) {
                toast("上传文件失败：" + msg);
            }
        });

        Contact c = new Contact();
        c.setObjectId("获取到的ID");
        final Note note = new Note();
        note.setContact(c);//添加一对多关系，绑定联系人
        note.setText(null);//没有文本
        note.setImage(bmobFile);//添加图片
        note.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                //新建成功后的操作...
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    /**
     * 笔记查询和图片下载
     * 同时包含了查询特定联系人的所有笔记，
     */
    private void testDownloadImg(){
        //第一种方法
        BmobQuery<Note> query = new BmobQuery<Note>();
        Contact contact = new Contact();
        contact.setObjectId("联系人ID");
        //按条件查询，
        query.addWhereEqualTo("contact",new BmobPointer(contact));
        //按照时间降序,即最近添加的图片最新加载。
        query.order("-createdAt");
        query.findObjects(this, new FindListener<Note>() {
            @Override
            public void onSuccess(List<Note> object) {
                for (Note note : object) {
                    BmobFile bmobfile = note.getImage();
                    if(bmobfile!= null){
                        //调用下面示例的bmobfile.download方法
                    }
                }
            }
            @Override
            public void onError(int code, String msg) {
                toast("查询失败："+msg);
            }
        });
//        //第二种方法,构造bmobfile对象，BmobFile(String 图片命名,"",String 网络地址)，网络地址即为
//        // bmobFile.getFileUrl(Context);
//        BmobFile bmobfile =new BmobFile("xxx.png","",
//                "http://bmob-cdn-2.b0.upaiyun.com/2016/04/12/58eeed852a7542cb964600c6cc0cd2d6.png");

//        // 然后下载图片。
//        // download(Context context,File savePath, DownloadFileListener listener)：
//        // 此方法允许开发者指定文件存储目录和文件名
//        // 示例：
//        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
//        File saveFile = new File(Environment.getExternalStorageDirectory(), bmobfile.getFilename());
//        bmobfile.download(this,saveFile, new DownloadFileListener() {
//
//            @Override
//            public void onStart() {
//                toast("开始下载...");
//            }
//
//            @Override
//            public void onSuccess(String savePath) {
//                toast("下载成功,保存路径:"+savePath);
//            }
//
//            @Override
//            public void onProgress(Integer value, long newworkSpeed) {
//                Log.i("bmob","下载进度："+value+","+newworkSpeed);
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                toast("下载失败："+code+","+msg);
//            }
//        });

    }

    //提供简单的查询例子。对于数据查询的方法，目前有两种方法：
    //1.全部查询，例子即为下面的方法，另外可以参考 “失物招领源码”
    //2.分页查询(结合ListView)，本人认为这种方法是最佳方法，但需要android组仔细阅读
    // “分页查询结合ListView源码”，该源码阅读起来不会很难。
    private void testQueryContact(){
        User myUser = BmobUser.getCurrentUser(this, User.class);
        //查询全部联系人
        BmobQuery<Contact> query = new BmobQuery<Contact>();
        //按条件查询，特定用户的联系人
        query.addWhereEqualTo("usert",new BmobPointer(myUser));
        // 按照时间降序,即最近添加的联系人就在最前面
        query.order("-createdAt");
        query.findObjects(this, new FindListener<Contact>() {

            @Override
            public void onSuccess(List<Contact> contacts) {
                // TODO Auto-generated method stub
                //这里设置了清除adapter原有的数据重新加载进来，因为考虑到PC端也可以同步操作
                //ContactAdapter.clear();
                if (contacts == null || contacts.size() == 0) {
                    //没有数据的时候调用“提示错误窗口”
                    //showErrorView(0);
                    //通知adapter发生了数据变更，参考源码“失物招领”
                    //ContactAdapter.notifyDataSetChanged();
                    return;
                }
//                progress.setVisibility(View.GONE);
//                ContactAdapter.addAll(losts);
//                listview.setAdapter(ContactAdapter);
            }

            @Override
            public void onError(int code, String arg0) {
                // TODO Auto-generated method stub
                //showErrorView(0);
            }
        });
    }

}
