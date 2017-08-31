package com.example.testsqlite;

public class Note {
	protected int id;
	protected String note;
	protected String lastUpdate;
	
	public Note(int id, String note, String lastUpdate) {
		this.id = id;
		this.note = note;
		this.lastUpdate = lastUpdate;
	}
	
	public String getNote() {
		return note;
	}
	
	public String getLastUpdate() {
		return lastUpdate;
	}
	
	public int getId() {
		return id;
	}
}
