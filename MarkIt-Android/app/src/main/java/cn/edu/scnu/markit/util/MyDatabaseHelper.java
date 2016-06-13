package cn.edu.scnu.markit.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jialin on 2016/5/23.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    /**
     * 用户表
     */
    public static final String CREATE_USERS_TABLE = "create table Users ("
            + "id text, "
            + "userName text, "
            + "password text"
            + ")";
    /**
     * 联系人表
     */
    public static final String CREATE_CONTACTS_TABLE = "create table Contacts ("
            + "id integer primary key autoincrement, "
            + "contactName text, "
            + "userId text"
            + ")";
    /**
     * 聊天笔记表
     */
    public static final String CREATE_NOTES_TABLE = "create table Notes ("
            + "id integer primary key autoincrement, "
            + "contactId integer, "
            + "contactName text, "
            + "note text, "
            + "image text, "
            + "date text"
            + ")";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
