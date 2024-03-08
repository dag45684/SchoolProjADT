package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Main {
	static MongoClient mc;
	static MongoDatabase db;

	public static void main(String[] args) {
		mc = MongoClients.create(new ConnectionString("mongodb://localhost:27017"));
		db = mc.getDatabase("school");
		db.createCollection("students");
		db.createCollection("teachers");
		db.createCollection("subjects");
		db.listCollectionNames().forEach(System.out::println);

//		 TeachersHandler th = new TeachersHandler(db.getCollection("teachers"));
//		 th.insertTeacher();
//		 th.selectTeacher();

//		 
//		 SubjectHandler sh = new SubjectHandler(db.getCollection("subjects"));
//		 sh.insertSubject();

//		 sh.selectSubject();
			
		insertSubject();
	}

	public static void insertSubject() {
		MongoCollection<Document> teachers = db.getCollection("teachers");
		MongoCollection<Document> subjects = db.getCollection("subjects");
		Scanner sc = new Scanner(System.in);
		System.out.println("Para buscar un profesor, escribe el campo sobre el que buscas y el"
				+ " valor del campo separados por ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("El formato no es el especificado");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		Document filtro = new Document(command[0], command[1]);

		System.out.println("Introduce el id de las asignaturas que quieras añadir, " + "separadas por ','");
		temp = sc.nextLine();
		ArrayList<String> subs = new ArrayList<>();
		if (temp.contains(",")) {
			for (String s : temp.split(",")) {
				subs.add(s);
			}
		} else {
			subs.add(temp);
		}
		
		ArrayList<String> rm = new ArrayList<>();
		subs.forEach(t -> {
			teachers.find(new Document("asignaturas", t)).forEach(e -> rm.add(t));
		});
		subs.removeAll(rm);
		
		subs.forEach(e -> subjects.find(new Document("_id", e)).forEach(t -> teachers.updateOne(filtro, new Document("$push", new Document("asignaturas", e)))));
		sc.close();
	}

}
