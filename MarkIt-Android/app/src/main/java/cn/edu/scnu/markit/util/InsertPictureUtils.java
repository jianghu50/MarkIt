package cn.edu.scnu.markit.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.EditText;

import cn.edu.scnu.markit.R;


/**
 * Created by jialin on 2016/5/22.
 */
public class InsertPictureUtils {

    private static final String PIC_Length = "length";

    private static final int GET_IMAGE = 0X11;

    private static Context mContext;

    private static Activity myActivity;

    public static void insertPicture(Context context,EditText editText){

        mContext = context;
        //myActivity = (Activity)mContext;

        //getPicture();

        Editable editable = editText.getEditableText();

        int startPosition = editText.getSelectionStart();

        SpannableString spanString = new SpannableString(PIC_Length);

        Drawable drawable = context.getResources().getDrawable(R.drawable.float_icon);

        spanString.setSpan(new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE),0,spanString.length()
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        drawable.setBounds(2, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        editable.insert(startPosition, spanString);
    }

    private static void getPicture(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(intent);


       // myActivity.startActivityForResult(intent,GET_IMAGE);
        //mContext.startActivityForResult(intent, GET_IMAGE);
    }
  /*
   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case GET_IMAGE:
                if (resultCode == myActivity.RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = mContext.getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);//将路径对应的图片转成bitmap



                }
        }
    }*/
}
