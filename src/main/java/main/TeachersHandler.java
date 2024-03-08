package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;

public class TeachersHandler {
	MongoCollection<Document> collection;
	Scanner sc = new Scanner(System.in);

	public TeachersHandler(MongoCollection<Document> collection) {
		this.collection = collection;

	}

	void insertTeacher() {
		System.out.println("Insert teacher id:");
		String id = sc.nextLine();
		while (!id.matches("TE\\d{3}")) {
			System.err.println("EL ID no es valido, por favor manten un formato TE000");
			id = sc.nextLine();
		}
		System.out.println("Insert teacher name:");
		String name = sc.nextLine();
		System.out.println("Insert teacher surname:");
		String surname = sc.nextLine();
		System.out.println("Insert teacher age:");
		int age = sc.nextInt();
		System.out.println("Insert teacher salary:");
		float salary = sc.nextFloat();
		System.out.println("Insert teacher deptMgr (boolean):");
		boolean deptMgr = sc.nextBoolean();
		Teacher st = new Teacher(id, name, surname, age, salary, deptMgr);
		try {
			collection.insertOne(st.createDocument());
		} catch (MongoWriteException e) {
			System.err.println("El ID especificado ya existe");
		}
		sc.reset();

	}

	void selectTeacher() {
		System.out.println("Para buscar un profesor, escribe el campo sobre el que buscas y el"
				+ " valor del campo separados por ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("El formato no es el especificado");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		Document d = new Document(command[0], command[1]);
		collection.find(d).forEach(t -> System.out.println(t.toJson()));
		sc.reset();

	}

	

}

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

}
