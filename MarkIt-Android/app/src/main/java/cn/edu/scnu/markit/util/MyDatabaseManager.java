package cn.edu.scnu.markit.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.scnu.markit.javabean.AllNotesOfContact;
import cn.edu.scnu.markit.javabean.LatestNoteOfContacts;

/**
 * Created by jialin on 2016/5/23.
 */
public class MyDatabaseManager {

    public static String userId;

    public static MyDatabaseHelper dbHelper;

    private static SQLiteDatabase db;

    private static final String USER_TABLE = "Users";
    private static final String CONTACT_TABLE = "Contacts";
    private static final String NOTE_TABLE = "Notes";

    /*
    设置时间格式
     */
    private static  SimpleDateFormat    formatter    =   new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");

    public static void insertUser(String id,String userName,String password){
        db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("id",id);
        cv.put("userName",userName);
        cv.put("password",password);

        db.insert(USER_TABLE, null, cv);


    }

    /**
     * 添加联系人
     * @param contact 联系人姓名
     */
    public static void insertContact(String contact){
        db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("contactName",contact);
        cv.put("userId",userId);

        db.insert(CONTACT_TABLE, null, cv);

    }
    /**
     * 查询用户对应添加的联系人
     * @param userId 查询的用户ID
     * @return  联系人列表
     */
    public static List<String> queryContacts(String userId){
        List<String> list = new ArrayList<String>();

        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(CONTACT_TABLE,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do{
                /*Contact contact = new Contact();
                contact.setId(cursor.getInt(cursor.getColumnIndex("id")));
                contact.setContactName(cursor.getString(cursor.getColumnIndex("contactName")));*/
                String contact = cursor.getString(cursor.getColumnIndex("contactName"));
                list.add(contact);
            }while (cursor.moveToNext());
        }

        cursor.close();

        return list;
    }

    /**
     * 获取联系人id
     * @param contactName
     * @return
     */
    public static int getContactId(String contactName){
        int id;
        db = dbHelper.getWritableDatabase();
        String where = "contactName=?";
        String []whereValue = {contactName};
        Cursor cursor = db.query(CONTACT_TABLE,new String[]{"id"},where,whereValue,null,null,null,null);
        if (cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex("id"));
            return id;
        }

        return -1;
    }


    /**
     * 删除联系人
     * @param contactId 联系人id
     */
    public static void deleteContact(int contactId){
        db = dbHelper.getWritableDatabase();
        String where = "id=?";
        String[] whereArgs = {String.valueOf(contactId)};
        db.delete(CONTACT_TABLE, where, whereArgs);
    }

    /**
     * 插入笔记
     * @param note  插入的笔记
     * @param contactName   对应的联系人
     */
    public static void insertNote(String note,String contactName){
        db = dbHelper.getWritableDatabase();


        Date    curDate    =   new Date(System.currentTimeMillis());//获取当前时间
        String    dateStr    =    formatter.format(curDate);

        int contactId = getContactId(contactName);

        ContentValues cv = new ContentValues();
        cv.put("contactId",contactId);
        cv.put("note",note);
        cv.put("contactName",contactName);
        cv.put("image",0);
        cv.put("date",dateStr);

        db.insert(NOTE_TABLE, null, cv);

    }

    /**
     * 查询每个客户的最新的一条消息，并按时间递减排序
     * @return
     */
    public static List<LatestNoteOfContacts> queryLatestNoteForContacts(){
        List<LatestNoteOfContacts> list = new ArrayList<LatestNoteOfContacts>();
        db = dbHelper.getWritableDatabase();
        //String sql = "select * from (select * from `Notes` order by `date` desc) `temp`  group by contactId order by `date` desc";

        String sql2 = "select * from Notes group by contactId having max(date) order by date desc";

        Cursor cursor = db.rawQuery(sql2,null);

        if (cursor.moveToFirst()){
            do {
                LatestNoteOfContacts latestNoteOfContacts = new LatestNoteOfContacts();
                latestNoteOfContacts.setContactId(cursor.getInt(cursor.getColumnIndex("contactId")));
                latestNoteOfContacts.setContactName(cursor.getString(cursor.getColumnIndex("contactName")));
                latestNoteOfContacts.setNote(cursor.getString(cursor.getColumnIndex("note")));
                latestNoteOfContacts.setDate(cursor.getString(cursor.getColumnIndex("date")));
                list.add(latestNoteOfContacts);

                Log.i("contactId--------->",String.valueOf(latestNoteOfContacts.getContactId()));
            }while (cursor.moveToNext());

        }
        return list;
    }

    /**
     * 查询一个客户的所有信息，按时间递减排序
     * @param contactId 客户id
     * @return
     */
    public static List<AllNotesOfContact> queryAllNotesForContact(int contactId){
        List<AllNotesOfContact> list = new ArrayList<AllNotesOfContact>();
        db = dbHelper.getWritableDatabase();
        String where = "contactId=?";
        String[] whereArgs = {String.valueOf(contactId)};
        Cursor cursor = db.query(NOTE_TABLE, new String[]{"id", "note", "date"}, where, whereArgs, null, null, "date" + " DESC");
        if (cursor.moveToFirst()){
            do {
                AllNotesOfContact allNotesOfContact = new AllNotesOfContact();
                allNotesOfContact.setNote(cursor.getString(cursor.getColumnIndex("note")));
                allNotesOfContact.setDate(cursor.getString(cursor.getColumnIndex("date")));
                allNotesOfContact.setNoteId(cursor.getInt(cursor.getColumnIndex("id")));
                list.add(allNotesOfContact);

            }while (cursor.moveToNext());

        }
        return list;
    }

    /**
     * 用于更新数据时，获取数据的id，更新数据后，作为查询的条件
     * @param note
     * @param contactName
     * @return
     */
    public static int getNoteId(String note,String contactName){
        int id;
        db = dbHelper.getWritableDatabase();
        String where = "contactName=? and note=?";
        String []whereValue = {contactName,note};
        Cursor cursor = db.query(NOTE_TABLE,new String[]{"id"},where,whereValue,null,null,null,null);
        if (cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex("id"));
            return id;
        }

        return -1;
    }

    /**
     * 更新笔记
     * @param note  更新的笔记
     * @param contactName   对应的联系人
     * @param id    对应笔记的ID
     */
    public static void updateNote(String note, String contactName,int id){
        db = dbHelper.getWritableDatabase();

        Date    curDate    =   new Date(System.currentTimeMillis());//获取当前时间
        String    dateStr    =    formatter.format(curDate);

        ContentValues cv = new ContentValues();
        cv.put("note",note);
        cv.put("contactName",contactName);
        cv.put("image","");
        cv.put("date",dateStr);

        db.update(NOTE_TABLE,cv,"id = ?",new String[]{String.valueOf(id)});
    }
}
