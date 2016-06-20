package cn.edu.scnu.markit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.edu.scnu.markit.R;
import cn.edu.scnu.markit.javabean.Record;

/**
 * Created by Administer on 2016/5/23.
 */
public class RecordAdapter extends BaseAdapter {
    private List<Record> list =null;
    private Context mContext = null;

    public RecordAdapter(Context mContext, List<Record> list){
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
        final Record record = list.get(position);
        if(view==null){
            viewHolder = new ViewHolder();
            view=  LayoutInflater.from(mContext).inflate(R.layout.record_list_item, null);
            viewHolder.name=(TextView) view.findViewById(R.id.Record_name);
            viewHolder.text = (TextView)view.findViewById(R.id.Record_text);
            view.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.name.setText(record.getName());
        viewHolder.text.setText(record.getText());

        return view;
    }
    final static class ViewHolder {
        TextView name;
        TextView text;
        TextView time;
    }
}