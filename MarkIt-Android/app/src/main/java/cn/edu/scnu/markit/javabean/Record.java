package cn.edu.scnu.markit.javabean;

/**
 * Created by Administer on 2016/5/23.
 */
public class Record {
    private String name;
    private String text;
    private String time;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name= name;
    }
    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text= text;
    }
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time= time;
    }
}
