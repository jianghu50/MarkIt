package cn.edu.scnu.markit.util;

import java.util.ArrayList;
import java.util.List;

import cn.edu.scnu.markit.javabean.CharacterParser;
import cn.edu.scnu.markit.javabean.SortModel;

/**
 * Created by jialin on 2016/6/1.
 */
public class SortContacts {
    private static CharacterParser characterParser = CharacterParser.getInstance();

    /**
     * 对联系人按首字母进行排序
     * @param contacts  参数为联系人数组
     * @return          返回联系人列表
     */
    public static List<SortModel> sortContactsByPinyin(String [] contacts){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<contacts.length; i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(contacts[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(contacts[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }
}
