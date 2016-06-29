package cn.edu.scnu.markit.util;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.edu.scnu.markit.javabean.Contact;
import cn.edu.scnu.markit.javabean.Note;
import cn.edu.scnu.markit.javabean.User;

/**
 * Created by jialin on 2016/6/20.
 */
public class DataSyncManager {


    public static void createContact(Context context,String contactName){
        User myUser = BmobUser.getCurrentUser(context, User.class);
        //新建联系人
        Contact contact = new Contact();
        //设置姓名
        contact.setContactName(contactName);
        //添加一对多关联
        contact.setUser(myUser);
        //设置删除字段为否
        contact.setIsDelete(false);
        //将数据上传到云端：save(Context context,SaveListener listener)
        contact.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                //添加成功后可以在我们的服务器上查看到该数据

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }
    public static  String contactId = "";
    public static String getContactId(Context context, final String contactName){
        final BmobQuery<Contact> contact = new BmobQuery<>();
        User myUser = BmobUser.getCurrentUser(context, User.class);
        contact.addWhereNotEqualTo("contactName", contactName);
        contact.addWhereEqualTo("user", myUser);

        contact.findObjects(context, new FindListener<Contact>() {
            @Override
            public void onSuccess(List<Contact> list) {
                contactId = list.get(0).getObjectId();
                Log.i("ContactId", contactId);
            }

            @Override
            public void onError(int i, String s) {

            }
        });

        return contactId;
    }

    public static List<String> contactList = new ArrayList<String>();

    public static List<String> queryContatcs(Context context){

        User myUser = BmobUser.getCurrentUser(context, User.class);

        BmobQuery<Contact> query = new BmobQuery<>();

        query.addWhereEqualTo("user", myUser);
        query.setLimit(100);
        query.addWhereNotEqualTo("isDelete", true);
        query.findObjects(context, new FindListener<Contact>() {
            @Override
            public void onSuccess(List<Contact> list) {
                for (Contact contact : list) {
                    contactList.add(contact.getContactName());

                    Log.i("TAG", "query contact success");
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
        Log.i("TAG",contactList.toString());
        return contactList;
    }

    public static void deleteContact(final Context context,String contactName){
        User myUser = BmobUser.getCurrentUser(context, User.class);

        Contact contact = new Contact();
        contact.setContactName(contactName);
        contact.setUser(myUser);

        BmobQuery<Contact> query = new BmobQuery<>();
        query.addWhereEqualTo("contact", contact);
        query.setLimit(1);

        query.findObjects(context, new FindListener<Contact>() {
            @Override
            public void onSuccess(List<Contact> list) {
                String id = list.get(0).getObjectId();

                final Contact conDelete = new Contact();
                conDelete.setIsDelete(true);

                conDelete.update(context, id, new UpdateListener() {
                    @Override
                    public void onSuccess() {

                        Log.i("TAG", "delete contact success");

                        //删除联系人对应笔记

                        BmobQuery<Note> notesOfConatct = new BmobQuery<Note>();
                        notesOfConatct.addWhereEqualTo("contact", conDelete);
                        notesOfConatct.findObjects(context, new FindListener<Note>() {
                            @Override
                            public void onSuccess(List<Note> list) {
                                for (Note note : list) {
                                    note.setIsDelete(true);
                                    note.update(context, note.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void onSuccess() {
                                            Log.i("TAG", "delete notes of Contact Success");
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onError(int i, String s) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }

    //创建笔记
    public static void createNote(Context context,String contactId,String text){
        User myUser = BmobUser.getCurrentUser(context, User.class);

        Contact contact = new Contact();

        contact.setObjectId(contactId);

        Note note = new Note();
        note.setContact(contact);
        note.setIsDelete(false);
        note.setText(text);
        note.setImage(null);

        note.save(context, new SaveListener() {
            @Override
            public void onSuccess() {


            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }


  /*  public static String noteObjectId;
    public static String getNoteObjectId(Context context,String contactId,String text){

        BmobQuery<Note> queryNotes = new BmobQuery<Note>();

        Log.i("getNoteObjectId",contactId);

        Contact contact = new Contact();
        contact.setObjectId(contactId);

        queryNotes.addWhereEqualTo("isDelete", false);
        queryNotes.addWhereEqualTo("contact", contact);
        queryNotes.addWhereEqualTo("text", text);

        queryNotes.findObjects(context, new FindListener<Note>() {
            @Override
            public void onSuccess(List<Note> list) {
                if (list.size() != 0){
                    noteObjectId = list.get(0).getObjectId();
                }else {
                    Log.i("data noteId------>","size is 0");
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });

        return noteObjectId;
    }*/
/*
    public static List<LatestNoteOfContacts> latestNoteOfContacts = new ArrayList<LatestNoteOfContacts>();
    //查询每个联系人的最新笔记
    public static List<LatestNoteOfContacts> queryLatestNotes(final Context context){
        User myUser = BmobUser.getCurrentUser(context, User.class);

        final BmobQuery<Contact> queryContacts = new BmobQuery<>();

        queryContacts.addWhereEqualTo("user", myUser);
        queryContacts.addWhereNotEqualTo("isDelete", false);
        queryContacts.findObjects(context, new FindListener<Contact>() {
            @Override
            public void onSuccess(List<Contact> listContact) {
                for (Contact contact : listContact) {
                    final String contactId = contact.getObjectId();
                    BmobQuery<Note> queryNotes = new BmobQuery<Note>();
                    queryNotes.setLimit(1);
                    queryNotes.addWhereNotEqualTo("isDelete", true);
                    queryNotes.order("-createdAt");
                    queryNotes.findObjects(context, new FindListener<Note>() {
                        @Override
                        public void onSuccess(List<Note> listNote) {
                            LatestNoteOfContacts note = new LatestNoteOfContacts();
                            note.setContactName(listNote.get(0).getContact().getContactName());
                            note.setNote(listNote.get(0).getText());
                            note.setDate(listNote.get(0).getCreatedAt());
                            note.setContactId(contactId);

                            latestNoteOfContacts.add(note);
                        }

                        @Override
                        public void onError(int i, String s) {

                        }
                    });
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });

        return latestNoteOfContacts;

    }

    public static List<AllNotesOfContact> allNotesOfContacts = new ArrayList<AllNotesOfContact>();

    //查询一个联系人的所有笔记
    public static List<AllNotesOfContact> queryNotesOfContact(final Context context, final String contactId){
        User myUser = BmobUser.getCurrentUser(context, User.class);

        final BmobQuery<Contact> queryContacts = new BmobQuery<>();

        //queryContacts.addWhereEqualTo("user", myUser);
        queryContacts.addWhereEqualTo("objectId",contactId);

        queryContacts.findObjects(context, new FindListener<Contact>() {
            @Override
            public void onSuccess(List<Contact> listContact) {

                BmobQuery<Note> queryNotes = new BmobQuery<Note>();
                queryNotes.addWhereEqualTo("contact", listContact.get(0).getObjectId());
                queryNotes.addWhereNotEqualTo("isDelete",true);
                queryNotes.order("-createdAt");
                queryNotes.findObjects(context, new FindListener<Note>() {
                    @Override
                    public void onSuccess(List<Note> list) {
                        for (Note note : list) {
                            AllNotesOfContact contactNote = new AllNotesOfContact();
                            contactNote.setNote(note.getText());
                            contactNote.setDate(note.getCreatedAt());
                            contactNote.setNoteId(note.getObjectId());

                            allNotesOfContacts.add(contactNote);
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });

            }

            @Override
            public void onError(int i, String s) {

            }
        });

        return allNotesOfContacts;

    }*/

    //删除笔记 标记 isDelete 字段为false
    public static void deleteNote(Context context,String noteId){
        Note note = new Note();
        note.setIsDelete(true);
        note.update(context, noteId, new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.i("TAG", "delete note success");
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    //修改笔记
    public static void modifyNote(Context context,String noteId,String text){
        Note note = new Note();
        note.setText(text);
        note.update(context, noteId, new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.i("TAG","modify note success");
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
