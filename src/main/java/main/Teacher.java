package main;

import org.bson.Document;

class Teacher {
	String idTeacher;
	String name;
	String surname;
	int age;
	float salary;
	boolean deptMgr;

	public Teacher(String idTeacher, String name, String surname, int age, float salary, boolean deptMgr) {
		this.idTeacher = idTeacher;
		this.name = name;
		this.surname = surname;
		this.age = age;
		this.salary = salary;
		this.deptMgr = deptMgr;
	}

	Document createDocument() {
		Document d = new Document("_id", idTeacher);
		d.put("name", name);
		d.put("surname", surname);
		d.put("age", age);
		d.put("salary", salary);
		d.put("deptMgr", deptMgr);
		return d;
	}
	
	void structure() {
		System.out.println("TEACHERS:");
		System.out.println("\t - _id");
		System.out.println("\t - name");
		System.out.println("\t - surname");
		System.out.println("\t - age");
		System.out.println("\t - salary");
		System.out.println("\t - deptMgr");
		System.out.println("\t - students");
		System.out.println("\t - subjects");
	}

}
