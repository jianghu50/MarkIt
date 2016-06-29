package cn.edu.scnu.markit.javabean;

/**
 * 保存首页中，以客户为主的联系人item
 * Created by jialin on 2016/6/1.
 */
public class LatestNoteOfContacts {
    /**
     * 联系人id,便于点击时，根据id,查询其全部笔记
     */
    private String contactId;
    private String contactName;
    private String note;
    private String date;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
