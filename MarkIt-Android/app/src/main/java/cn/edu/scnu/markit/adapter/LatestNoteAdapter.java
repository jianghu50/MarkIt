package cn.edu.scnu.markit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.edu.scnu.markit.R;
import cn.edu.scnu.markit.javabean.LatestNoteOfContacts;

/**
 * Created by jialin on 2016/6/2.
 */
public class LatestNoteAdapter extends BaseAdapter {
    private List<LatestNoteOfContacts> list =null;
    private Context mContext = null;

    public LatestNoteAdapter(Context mContext, List<LatestNoteOfContacts> list){
        this.mContext=mContext;
        this.list=list;
    }
    public int getCount() {
        return this.list.size();
    }
    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final LatestNoteOfContacts noteOfContacts = list.get(position);
        if(view==null){
            viewHolder = new ViewHolder();
            view=  LayoutInflater.from(mContext).inflate(R.layout.record_list_item, null);
            viewHolder.name=(TextView) view.findViewById(R.id.Record_name);
            viewHolder.text = (TextView)view.findViewById(R.id.Record_text);
            viewHolder.time = (TextView)view.findViewById(R.id.Record_time);
            view.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.name.setText(noteOfContacts.getContactName());
        viewHolder.text.setText(noteOfContacts.getNote());
        viewHolder.time.setText(noteOfContacts.getDate());

        return view;
    }
    final static class ViewHolder {
        TextView name;
        TextView text;
        TextView time;
    }
}
