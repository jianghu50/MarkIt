package cn.edu.scnu.markit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

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

    private List<AllNotesOfContact> list;

    private int contactId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_all_notes);

        Intent intent = getIntent();
        contactId = intent.getIntExtra("contactId",-1);
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
        notesAdapter = new NotesAdapter(this,R.layout.item_contact_all_notes,list);
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
