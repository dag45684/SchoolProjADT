package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;

public class StudentHandler {
	MongoCollection<Document> collection;
	Scanner sc = new Scanner(System.in);

	public StudentHandler(MongoCollection<Document> collection) {
		this.collection = collection;

	}

	void insertStudent() {
		System.out.println("Insert student id:");
		String id = sc.nextLine();
		while (!id.matches("ST\\d{3}")) {
			System.err.println("EL ID no es valido, por favor manten un formato ST000");
			id = sc.nextLine();
		}
		System.out.println("Insert students name:");
		String name = sc.nextLine();
		System.out.println("Insert students surname:");
		String surname = sc.nextLine();
		System.out.println("Insert students age:");
		int age = sc.nextInt();
		Student st = new Student(id, name, surname, age);
		try {
			collection.insertOne(st.createDocument());
		}catch (MongoWriteException e) {
			System.err.println("El ID especificado ya existe");
		}
		sc.reset();

	}

	void insertStudent(int n) {
		List<Document> students = new ArrayList<Document>();
		for (int i = 0; i < n; i++) {
			System.out.println("Insert student id:");
			String id = sc.nextLine();
			while (!id.matches("ST\\n{3}")) {
				System.err.println("EL ID no es valido, por favor manten un formato ST000");
				id = sc.nextLine();
			}
			System.out.println("Insert students name:");
			String name = sc.nextLine();
			System.out.println("Insert students surname:");
			String surname = sc.nextLine();
			System.out.println("Insert students age:");
			int age = sc.nextInt();
			Student st = new Student(id, name, surname, age);
			students.add(st.createDocument());
		}
		collection.insertMany(students);
		sc.reset();

	}

	void selectStudent() {
		System.out.println("Para buscar un alumno, escribe el campo sobre el que buscas y el"
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

class Student {
	String idStudent;
	String name;
	String surname;
	int age;
	Subject[] subjects;

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