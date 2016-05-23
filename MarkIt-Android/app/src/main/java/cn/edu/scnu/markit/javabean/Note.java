package cn.edu.scnu.markit.javabean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Kasper on 2016/5/22.
 */
public class Note extends BmobObject {
    private String text;//笔记文本内容

    private BmobFile image;//笔记的图片

    private Contact contact;//一对多关系，对应的联系人

    public String getText() {
        return text;
    }

    public void setText(String content) {
        this.text = content;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }
}
