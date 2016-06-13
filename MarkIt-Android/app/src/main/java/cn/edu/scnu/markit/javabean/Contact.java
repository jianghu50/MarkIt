package cn.edu.scnu.markit.javabean;

/**
 * Created by jialin on 2016/6/2.
 */
public class Contact {
    /**
     * 便于对笔记进行插入操作，及联系人的删除操作
     */
    private int id;
    private String contactName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }


}
