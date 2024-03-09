package main;

import org.bson.Document;

class Student {
	String idStudent;
	String name;
	String surname;
	int age;

	public Student(String idStudent, String name, String surname, int age) {
		super();
		this.idStudent = idStudent;
		this.name = name;
		this.surname = surname;
		this.age = age;
	}

	Document createDocument() {
		Document d = new Document("_id", idStudent);
		d.put("name", name);
		d.put("surname", surname);
		d.put("age", age);
		return d;
	}

}
