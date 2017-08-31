package com.example.testsqlite;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	static final String TAG = "MainActivity";
	static final int MENUITEM_ID_DELETE = 1;
	
	ListView itemListView;
	EditText noteEditText;
	Button saveButton;
	
	static DBAdapter dbAdapter;
	static NoteListAdapter listAdapter;
	static List<Note> noteList = new ArrayList<Note>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        itemListView = (ListView)findViewById(R.id.itemListView);
        itemListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.add(0, MENUITEM_ID_DELETE, 0, "Delete");
			}
		});
        noteEditText = (EditText)findViewById(R.id.memoEditText);
        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        
        dbAdapter = new DBAdapter(this);
        listAdapter = new NoteListAdapter();
        itemListView.setAdapter(listAdapter);
        
        loadNoteList();
    }
    
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case MENUITEM_ID_DELETE:
			AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo)item.getMenuInfo();
			Note note = noteList.get(menuInfo.position);
			final int noteId = note.getId();
			
			new AlertDialog.Builder(this)
			.setIcon(R.drawable.ic_launcher)
			.setTitle("Are you sure you want to delete this note?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dbAdapter.open();
					if(dbAdapter.deleteNote(noteId)) {
						Toast.makeText(getBaseContext(), "The note was successfully deleted.", Toast.LENGTH_SHORT).show();
						loadNoteList();
					}
					dbAdapter.close();
				}
			})
			.setNegativeButton("Calcel", null)
			.show();
			break;
		}
    	return true;
    }


	@Override
	public void onClick(View v) {
		if(v == saveButton) {
			dbAdapter.open();
			dbAdapter.saveNote(noteEditText.getText().toString());
			dbAdapter.close();
			noteEditText.setText("");
			loadNoteList();
		}
	}
	
	
	private void loadNoteList() {
		noteList.clear();
		
		//Read
		
		dbAdapter.open();
		Cursor c = dbAdapter.getAllNotes();
		
		if(c.moveToFirst()) {
			do {
				Note note = new Note(
						c.getInt(c.getColumnIndex(DBAdapter.COL_ID)), 
						c.getString(c.getColumnIndex(DBAdapter.COL_NOTE)), 
						c.getString(c.getColumnIndex(DBAdapter.COL_LASTUPDATE)));
				noteList.add(note);
			} while(c.moveToNext());
		}
		dbAdapter.close();
		listAdapter.notifyDataSetChanged();
	}
	
	private class NoteListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return noteList.size();
		}

		@Override
		public Object getItem(int position) {
			return noteList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView noteTextView;
			TextView lastUpdateTextView;
			View v = convertView;
			if(v == null) {
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.list_row, null);
			}
			Note note = (Note)getItem(position);
			if(note != null) {
				noteTextView = (TextView)v.findViewById(R.id.noteTextView);
				noteTextView.setText(note.getNote());
				lastUpdateTextView = (TextView)v.findViewById(R.id.lastupdateTextView);
				lastUpdateTextView.setText(note.getLastUpdate());
				v.setTag(R.id.noteTextView, note);				
			}
			return v;
		}
	}
    
}
