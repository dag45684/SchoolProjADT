package main;

import org.bson.Document;

public class Subject {

		String idSubject;
		String name; 
		int hours; 

	public Subject(String idSubject, String name, int hours) {
		super();
		this.idSubject = idSubject;
		this.name = name;
		this.hours = hours;
	}

	Document createDocument() {
		Document d = new Document("_id", idSubject);
		d.put("name", name);
		d.put("hours", hours);
		return d;
	}

}
