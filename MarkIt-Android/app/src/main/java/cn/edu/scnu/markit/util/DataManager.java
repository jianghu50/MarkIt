package cn.edu.scnu.markit.util;

import java.util.List;

import cn.edu.scnu.markit.javabean.Contact;

/**
 * Created by jialin on 2016/6/28.
 */
public class DataManager {

    private List<Contact> contactList;

    private Contact contact;

    private static DataManager singleDataManager = new DataManager();
    private DataManager(){

    }

    public static DataManager getDataManager(){
        return singleDataManager;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
