package cn.edu.scnu.markit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cn.edu.scnu.markit.R;

/**
 * Created by jialin on 2016/5/19.
 */
public class FloatContactViewAdapter<T> extends ArrayAdapter<T> {

    private Context mContext;

    public FloatContactViewAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String contact = (String)getItem(position);

        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_item,null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView)view.findViewById(R.id.listView_item);
            view.setTag(viewHolder);

        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.mTextView.setText(contact);
        return view;
        //return super.getView(position, convertView, parent);
    }

    class ViewHolder{
        TextView mTextView;
    }
}
