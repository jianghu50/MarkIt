package cn.edu.scnu.markit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.edu.scnu.markit.R;
import cn.edu.scnu.markit.javabean.Contact;

/**
 * Created by jialin on 2016/5/19.
 */
public class FloatContactViewAdapter extends BaseAdapter {

    private List<Contact> mContactList;
    private Context mContext;

    public FloatContactViewAdapter(Context context ,List<Contact> contactList) {
        mContactList = contactList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mContactList.size();
    }

    @Override
    public Object getItem(int position) {
        return mContactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Contact contact = mContactList.get(position);

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

        viewHolder.mTextView.setText(contact.getContactName());
        return view;
        //return super.getView(position, convertView, parent);
    }

    class ViewHolder{
        TextView mTextView;
    }
}
