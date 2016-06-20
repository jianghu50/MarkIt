package cn.edu.scnu.markit.javabean;

/**
 * Created by jialin on 2016/6/1.
 */
public class AllNotesOfContact {
    /**
     * 笔记id,便于对笔记的更改，删除，进行操作
     */
    private int noteId;
    private String note;
    private String date;

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
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
