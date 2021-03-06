package com.example.testsqlite;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
	static final String DATABASE_NAME = "mynote.db";
	static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_NAME = "notes";
	public static final String COL_ID = "_id";
	public static final String COL_NOTE = "note";
	public static final String COL_LASTUPDATE = "lastupdate";
	
	protected final Context context;
	protected DatabaseHelper dbHelper;
	protected SQLiteDatabase db;
	
	public DBAdapter(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(this.context);
	}
	
	
	/**
	 * SQLiteOpenHelper
	 * @author EP-025
	 *
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			StringBuilder sb = new StringBuilder();
			sb.append("CREATE TABLE " + TABLE_NAME + " (");
			sb.append(COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
			sb.append(COL_NOTE + " TEXT NOT NULL,");
			sb.append(COL_LASTUPDATE + " TEXT NOT NULL);");
			db.execSQL(sb.toString());

//			Date dateNow = new Date();
//			ContentValues values = new ContentValues();
//			values.put(COL_NOTE, "11111");
//			values.put(COL_LASTUPDATE, dateNow.toString());
//			db.insertOrThrow(TABLE_NAME, null, values);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}

	//
	// Adapter MEthods
	//
	public DBAdapter open() {
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		dbHelper.close();
	}

	//
	//App Methods
	//
	public boolean deleteAllNotes() {
		return db.delete(TABLE_NAME, null, null) > 0;
	}
	
	public boolean deleteNote(int id) {
		return db.delete(TABLE_NAME, COL_ID + "=" + id, null) > 0;
	}
	
	public Cursor getAllNotes() {
		return db.query(TABLE_NAME, null, null, null, null, null, null);
	}
	
	public void saveNote(String note) {
		Date dateNow = new Date();
		ContentValues values = new ContentValues();
		values.put(COL_NOTE, note);
		values.put(COL_LASTUPDATE, dateNow.toString());
		db.insertOrThrow(TABLE_NAME, null, values);
	}
	
	public void insertNote(String note) {
		Date dateNow = new Date();
		ContentValues values = new ContentValues();
		values.put(COL_NOTE, note);
		values.put(COL_LASTUPDATE, dateNow.toString());
		db.insertOrThrow(TABLE_NAME, null, values);
	}
}
