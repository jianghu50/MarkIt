package cn.edu.scnu.markit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.scnu.markit.MainActivity;
import cn.edu.scnu.markit.R;
import cn.edu.scnu.markit.adapter.NotesAdapter;
import cn.edu.scnu.markit.javabean.AllNotesOfContact;
import cn.edu.scnu.markit.util.MyDatabaseManager;

/**
 * Created by jialin on 2016/6/3.
 */
public class ContactNotesActivity extends AppCompatActivity{

    private ListView listView = null;

    private NotesAdapter notesAdapter = null;

    private List<AllNotesOfContact> list = new ArrayList<AllNotesOfContact>();

    private String contactId;

    private Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_all_notes);

        Intent intent = getIntent();
        contactId = intent.getStringExtra("contactId");
        String contactName = intent.getStringExtra("contactName");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle(contactName);

        initViews();
        setAdapter();


    }

    private void setAdapter() {
        list = MyDatabaseManager.queryAllNotesForContact(contactId);
       /* User myUser = BmobUser.getCurrentUser(this, User.class);

        final BmobQuery<Contact> queryContacts = new BmobQuery<Contact>();

        //queryContacts.addWhereEqualTo("user", myUser);
        queryContacts.addWhereEqualTo("objectId", contactId);

        queryContacts.findObjects(this, new FindListener<Contact>() {
            @Override
            public void onSuccess(List<Contact> listContact) {

                BmobQuery<Note> queryNotes = new BmobQuery<Note>();
                queryNotes.addWhereEqualTo("contact", listContact.get(0).getObjectId());
                queryNotes.addWhereNotEqualTo("isDelete", true);
                queryNotes.order("-createdAt");

                *//*queryNotes.findObjects(this, new FindListener<Note>() {
                    @Override
                    public void onSuccess(List<Note> list) {
                        for (Note note : list) {
                            AllNotesOfContact contactNote = new AllNotesOfContact();
                            contactNote.setNote(note.getText());
                            contactNote.setDate(note.getCreatedAt());
                            contactNote.setNoteId(note.getObjectId());

                            //list.add(contactNote);
                            listAdapter.add(contactNote);
                        }

                        notesAdapter = new NotesAdapter(mContext, R.layout.item_contact_all_notes, listAdapter);
                        listView.setAdapter(notesAdapter);
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });*//*

            }

            @Override
            public void onError(int i, String s) {

            }
        });*/
        //list = DataSyncManager.queryNotesOfContact(this,contactId);

        notesAdapter = new NotesAdapter(mContext, R.layout.item_contact_all_notes, list);
        listView.setAdapter(notesAdapter);
    }

    private void initViews(){
        listView = (ListView)findViewById(R.id.listView_contactNotes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_notes,menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_note:
                break;
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
