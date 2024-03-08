package main;

import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;

public class SubjectHandler {
	MongoCollection<Document> collection;
	Scanner sc = new Scanner(System.in);;

	public SubjectHandler(MongoCollection<Document> collection) {
		this.collection = collection;

	}

	void insertSubject() {
		System.out.println("Insert subject id:");
		String id = sc.nextLine();
		while (!id.matches("SB\\d{3}")) {
			System.err.println("EL ID no es valido, por favor manten un formato SB000");
			id = sc.nextLine();
		}
		System.out.println("Insert subject name:");
		String name = sc.nextLine();
		System.out.println("Insert subject hours:");
		int hours = sc.nextInt();
		Subject st = new Subject(id, name, hours);
		try {
			collection.insertOne(st.createDocument());
		}catch (MongoWriteException e) {
			System.err.println("El ID especificado ya existe");
		}
	}

	void selectSubject() {
		System.out.println("Para buscar una asignatura, escribe el campo sobre el que buscas y el"
				+ " valor del campo separados por ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("El formato no es el especificado");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		Document d = new Document(command[0], command[1]);
		collection.find(d).forEach(t -> System.out.println(t.toJson()));

	}

}
