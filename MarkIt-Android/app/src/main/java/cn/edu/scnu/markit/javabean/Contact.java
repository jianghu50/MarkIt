package cn.edu.scnu.markit.javabean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Kasper on 2016/5/22.
 * 联系人，对应于云端的Contact表，与用户为一对多的关系。
 *
 */
public class Contact extends BmobObject {
    private String contactName;//联系人姓名

    private User user;//对应的用户

    private Boolean isDelete;//是否删除

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
