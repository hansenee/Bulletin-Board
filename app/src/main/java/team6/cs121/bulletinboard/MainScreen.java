package team6.cs121.bulletinboard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class MainScreen extends Activity implements NoteModifier {

    private Button addNote;
    private ListView noteList;
    private EditText newNoteText;
    private BulletinBoard personalBoard;
    private NoteAdapter adapter;
    private final String FILE_NAME = "personalBoard";
    private BulletinBoardClickListener bulletinBoardClick;
    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.createNote:
                    createNote();
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        this.newNoteText = (EditText) findViewById(R.id.newNoteText);
        this.noteList = (ListView) findViewById(R.id.note_listview);
        this.bulletinBoardClick = new BulletinBoardClickListener(this.personalBoard, this);
        this.noteList.setOnItemClickListener(this.bulletinBoardClick);
        this.addNote = (Button) findViewById(R.id.createNote);
        this.addNote.setOnClickListener(this.clickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initBoard();
        adapter = new NoteAdapter(this, R.layout.note, personalBoard.getAllNotes());
        this.noteList.setAdapter(adapter);
    }

    private void initBoard() {
        File file = new File(this.getFilesDir(), FILE_NAME);
        if(file.exists()) {
            FileInputStream fis = null;
            try {
                fis = this.openFileInput(FILE_NAME);
                this.personalBoard = BulletinBoard.deserialize(fis);
            } catch (java.io.IOException e) {
                Log.e("ERROR", e.getMessage(), e);
            } catch (ClassNotFoundException e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        } else {
            this.personalBoard = new BulletinBoard("Personal Board");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(BulletinBoard.serialize(this.personalBoard));
            fos.close();
        } catch (java.io.IOException e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     *
     */
    private void createNote() {
        Note note = new Note(this.newNoteText.getText().toString());
        this.personalBoard.addNote(note);
        this.newNoteText.setText("");
        this.adapter.notifyDataSetChanged();
    }

    
    @Override
    public void removeNote(int index) {
        this.personalBoard.removeNote(index);
        this.adapter.notifyDataSetChanged();
    }
}
