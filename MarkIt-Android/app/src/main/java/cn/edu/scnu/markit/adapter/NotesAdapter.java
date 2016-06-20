package cn.edu.scnu.markit.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.edu.scnu.markit.R;
import cn.edu.scnu.markit.javabean.AllNotesOfContact;

/**
 * Created by jialin on 2016/6/3.
 */
public class NotesAdapter extends ArrayAdapter<AllNotesOfContact> {
    private Context mContext;

    public NotesAdapter(Context context, int resource, List<AllNotesOfContact> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final AllNotesOfContact notes = (AllNotesOfContact)getItem(position);

        Log.i("notes",notes.getNote());

        View view;
        final ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_contact_all_notes,null);
            viewHolder = new ViewHolder();
            viewHolder.mTextDate = (TextView)view.findViewById(R.id.textView_date);
            viewHolder.mEditText = (EditText)view.findViewById(R.id.editText_notes);
            viewHolder.mTextArrow = (TextView)view.findViewById(R.id.textView_edit);
            viewHolder.relativeLayout = (RelativeLayout)view.findViewById(R.id.date_arrow_layout);
            view.setTag(viewHolder);

        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.textView_edit){
                    int textNumber = notes.getNote().length();
                   // Toast.makeText(mContext,"arrow is click",Toast.LENGTH_SHORT).show();
                    if (textNumber > 15){
                        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams)viewHolder.mEditText.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        viewHolder.mEditText.setLayoutParams(layoutParams);

                        /*viewHolder.mTextArrow.setVisibility(View.GONE);
                        viewHolder.mTextDate.setVisibility(View.GONE);*/
                        viewHolder.relativeLayout.setVisibility(View.GONE);
                    }


                    viewHolder.mEditText.setFocusableInTouchMode(true);
                    viewHolder.mEditText.setFocusable(true);
                    viewHolder.mEditText.requestFocus();


                }

            }
        };

       // viewHolder.mTextDate.setText(notes.getNote());
        viewHolder.mEditText.setText(notes.getNote());
        viewHolder.mTextArrow.setOnClickListener(listener);
        return view;
        //return super.getView(position, convertView, parent);
    }

    class ViewHolder{
        EditText mEditText;
        TextView mTextDate;
        TextView mTextArrow;
        RelativeLayout relativeLayout;
    }
}
