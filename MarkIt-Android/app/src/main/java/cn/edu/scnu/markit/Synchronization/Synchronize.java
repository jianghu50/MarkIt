package cn.edu.scnu.markit.Synchronization;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import cn.edu.scnu.markit.config.MarkItConstants;
import cn.edu.scnu.markit.javabean.Contact;
import cn.edu.scnu.markit.javabean.Note;
import cn.edu.scnu.markit.javabean.User;


/**
 * Created by Kasper on 2016/6/27.
 * 同步函数
 * 该函数需要实现：
 * 1.查询ChaneLog表，以最近一次的同步时间查询，取得最新的数据
 * 2.将取得的数据按type分为联系人、笔记，交operation函数操作
 * 3.operation函数需要判别operation的类型，然后交Create、edit、delete函数操作
 * 4.在上述3个函数中与本地数据库进行交互，将数据插入本地数据库
 * 5.更新同步时间
 *
 * 完成情况：
 * 只剩下插入本地数据库语句未实现
 */
public class Synchronize {

    /** 记录同步时间 preference = "用户Id-time",按用户建立 **/
    private SharedPreferences syncSharedPreference;

    private User user; //设置用户

    private Context mContext; // 取得主界面的Context

    /**
     * 构造函数
     */
    public Synchronize() {}

    public Synchronize(Context context, User user){
        this.mContext = context;
        this.user = user;
        // MODE_PRIVATE：默认操作模式，和直接传0效果相同，
        // 表示只有当前应用程序才可以对这个SharedPreferences文件进行读写
        this.syncSharedPreference = context.getSharedPreferences(this.user.getObjectId()+"-time", 0);
    }

    public Synchronize(Context context, User user, SharedPreferences sp ){
        this.mContext = context;
        this.user = user;
        this.syncSharedPreference = sp;
    }

    /**
     * 运行同步
     */
    public void Run(){
        // 直接执行查询ChangeLog表
        CheckChangeLog();
    }

    /**
     * 查询ChangeLog表
     */
    public void CheckChangeLog(){
        BmobQuery<ChangeLog> query = new BmobQuery<ChangeLog>();
        //查询本用户(userId)的所有ChangeLog数据。
        query.addWhereEqualTo("userId", user.getObjectId());
        //返回1000（max）条数据，如果不加上这条语句，默认返回10条数据，当数据量非常大时需要优化
        query.setLimit(1000);
        //获取最近同步的时间
        Date time = getSyncDate();
        //为null的情况为第一次使用preference，直接从changelog上拿数据即可
        if ( time == null){
            //执行查询方法
            query.findObjects(mContext, new FindListener<ChangeLog>() {
                @Override
                public void onSuccess(List<ChangeLog> object) {
                    // TODO Auto-generated method stub
                    //toast("查询成功：共"+object.size()+"条数据。");
                    for (ChangeLog changeLogs : object) {
                        //按type分类、operation操作分类
                        if( changeLogs.getType().equals(MarkItConstants.CHANGELOG_TYPE_CONTACT) ){
                            //type类型为联系人时
                            analyzeContact(changeLogs);
                        }
                        else {
                            //type类型为笔记时
                            analyzeNote(changeLogs);
                        }
                    }
                }
                @Override
                public void onError(int code, String msg) {
                    // TODO Auto-generated method stub
                    Log.d("Synchronize","CheckChangeLog() : time = null. query = error");
                    //输出错误信息
                    Toast toast = Toast.makeText(mContext,
                            "同步失败", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        } else {
            // 如果想查询指定日期之后的数据，则可以使用addWhereGreaterThan
            // 或addWhereGreaterThanOrEqualTo（包含当天）来查询。
            query.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(time));
            //执行查询方法
            query.findObjects(mContext, new FindListener<ChangeLog>() {
                @Override
                public void onSuccess(List<ChangeLog> object) {
                    // TODO Auto-generated method stub
                    //toast("查询成功：共" + object.size() + "条数据。");
                    for (ChangeLog changeLogs : object) {
                        //按type分类、operation操作分类
                        if( changeLogs.getType().equals(MarkItConstants.CHANGELOG_TYPE_CONTACT) ){
                            //type类型为联系人时
                            analyzeContact(changeLogs);
                        }
                        else {
                            //type类型为笔记时
                            analyzeNote(changeLogs);
                        }
                    } // end for
                }

                @Override
                public void onError(int code, String msg) {
                    // TODO Auto-generated method stub
                    Log.d("Synchronize", "CheckChangeLog() : time = null. query = error");
                    //输出错误信息
                    Toast toast = Toast.makeText(mContext,
                            "同步失败", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        } // end else
        // 同步完成后 更新同步时间
        updateSyncSharedPreference();
    }

    /**
     * 联系人操作分类
     * 分析操作类型，并将操作插入到本地数据库中
     */
    public void analyzeContact(ChangeLog contact){
        //new 一个contact 设置好内容，然后插入或update到本地数据库中
        Contact tempContact = new Contact();
        //操作类型为创建时
        if( contact.getOperation().equals(MarkItConstants.CHANGELOG_OPERATION_CREATE) ){
            //注意，是取得RelationId，而不是objectId
            tempContact.setObjectId(contact.getRelationId());
            //获取姓名，因为已经确定是联系人，所以Content为姓名
            tempContact.setContactName(contact.getContent());
            //绑定用户
            tempContact.setUser(this.user);
            //设置删除标记
            tempContact.setIsDelete(false);

            //新建并插入本地数据库
        }
        //操作类型为更改时
        else if( contact.getOperation().equals(MarkItConstants.CHANGELOG_OPERATION_EDIT) ){
            tempContact.setObjectId(contact.getRelationId());
            tempContact.setContactName(contact.getContent());

            //更新本地数据库语句 ...
        }
        //操作类型为删除时
        else if( contact.getOperation().equals(MarkItConstants.CHANGELOG_OPERATION_DELETE)) {
            tempContact.setObjectId(contact.getRelationId());
            //设置删除标记
            tempContact.setIsDelete(true);

            //更新本地数据库语句 ...
        }
        Log.d("Synchronize","analyzeContact() : " + contact.getOperation());
    }

    /**
     * 笔记操作分类
     * 分析操作类型，并将操作插入到本地数据库中
     */
    public void analyzeNote(ChangeLog note){
        //new 一个note 设置好内容，然后插入或update到本地数据库中
        Note tempNote = new Note();
        Contact tempContact = new Contact();
        //先拆分RelationId，笔记（Note）的RelationId为联系人（contactId）+ “@” + 笔记（noteId）
        String contactId = note.getRelationId().split("@")[0];
        tempContact.setObjectId(contactId);
        String noteId = note.getRelationId().split("@")[1];

        //操作类型为创建时
        if( note.getOperation().equals(MarkItConstants.CHANGELOG_OPERATION_CREATE) ){
            tempNote.setObjectId(noteId);
            // 获取内容，因为已经确定是笔记，所以Content为文本
            // 可能需要添加判断是否为null的操作
            tempNote.setText(note.getContent());
            // 获取图片
            // 可能需要添加判断是否为null的操作
            tempNote.setImage(note.getImage());
            // 绑定联系人
            tempNote.setContact(tempContact);
            //设置删除标记
            tempNote.setIsDelete(false);

            //插入本地数据库
        }
        //操作类型为更改时
        else if( note.getOperation().equals(MarkItConstants.CHANGELOG_OPERATION_EDIT) ){
            tempNote.setObjectId(noteId);
            tempNote.setText(note.getContent());
            tempNote.setImage(note.getImage());

            //更新本地数据库语句 ...
        }
        //操作类型为删除时
        else if( note.getOperation().equals(MarkItConstants.CHANGELOG_OPERATION_DELETE) ){
            tempNote.setObjectId(noteId);
            //设置删除标记
            tempNote.setIsDelete(true);
            //更新本地数据库语句 ...
        }
        Log.d("Synchronize","analyzeContact() : " + note.getOperation());
    }

    /**
     * 获取最近同步时间
     */
    public Date getSyncDate(){
        //获取时间，默认值为null。
        String start = syncSharedPreference.getString("date","null");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date  = null;
        if(!start.equals("null")){
            try {
                date = sdf.parse(start);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Log.d("Synchronize","getSyncDate() : " + start);
        return date;
    }

    /**
     * 同步完成后设置最新的同步时间
     */
    public void updateSyncSharedPreference() {
        /** 获得系统当前时间 **/
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String currentTime = formatter.format(curDate);
        //让configuration处于编辑状态
        SharedPreferences.Editor editor = syncSharedPreference.edit();
        //存放数据
        editor.putString("date",currentTime);
        //完成提交
        editor.commit();
        Log.d("Synchronize", "updateSyncSharedPreference() : " + currentTime);
    }

/////////////////////////////////////////////////////////////////////////////////////
////////////////////////                 Setter           //////////////////////////


    /**
     * 设置记录同步时间sharedpreference
     * @param syncSharedPreference 由MainActivity传入
     */
    public void setSyncSharedPreference(SharedPreferences syncSharedPreference) {
        this.syncSharedPreference = syncSharedPreference;
    }

    /**
     * 设置用户，在用户登录成功时应该设置
     * @param user 由MainActivity传入
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 设置Context，在用户登录成功时设置
     * @param Context 由MainActivity传入
     */
    public void setContext(Context Context) {
        this.mContext = Context;
    }
}
