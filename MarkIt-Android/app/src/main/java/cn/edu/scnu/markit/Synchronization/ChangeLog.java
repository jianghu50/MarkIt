package cn.edu.scnu.markit.Synchronization;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Kasper on 2016/6/27.
 * 同步表
 * 数据结构
 */
public class ChangeLog extends BmobObject{
    private String type;//该Change的类型，可为联系人、笔记;联系人="CONTACT" 笔记="NOTE"

    private String operation;//该Change的操作类型，可为增、删、改，增="CREATE" 改="EDIT" 删="DELETE"

    /** 最特殊的 **/
    private String relationId;//该Change的操作对象的Id，即联系人或笔记的Id,
    // 当为笔记时，该Id应为 归属联系人的id + “@” + 笔记的Id

    private String userId;//该Change的操作对象的归属用户Id，即联系人或笔记的归属人（用户）Id

    private String content;//该Change的文本内容，可为联系人姓名或笔记的文本内容

    private BmobFile image;//该Change的图片内容，只能为笔记的图片

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }
}
