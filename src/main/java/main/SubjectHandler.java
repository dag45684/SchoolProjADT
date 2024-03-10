package main;

import java.util.Scanner;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;

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
			System.err.println("Not valid ID, format should be: SB000");
			id = sc.nextLine();
		}
		System.out.println("Insert subject name:");
		String name = sc.nextLine();
		System.out.println("Insert subject hours:");
		int hours = sc.nextInt();
		Subject st = new Subject(id, name, hours);
		try {
			collection.insertOne(st.createDocument());
		} catch (MongoWriteException e) {
			System.err.println("A subject with id:" + id + " already exists.");
		}
	}

	void insertSubject(int n) {
//		List<Document> subjects = new ArrayList<Document>();
//		for (int i = 0; i < n; i++) {
//			System.out.println("Insert subject id:");
//			String id = sc.nextLine();
//			while (!id.matches("SB\\d{3}")) {
//				System.err.println("Not valid ID, format should be: SB000");
//				id = sc.nextLine();
//			}
//			System.out.println("Insert subject name:");
//			String name = sc.nextLine();
//			System.out.println("Insert subject hours:");
//			int hours = sc.nextInt();
//			Subject st = new Subject(id, name, hours);
//			subjects.add(st.createDocument());
//		}
//		collection.insertMany(subjects);
		
		// Same issue as StudentHandler, error catching > efficiency.
		for (int i = 0; i < n; i++) {
			insertSubject();
		}

	}

	void selectSubject() {
		System.out.println("To search for a subject, write the field you are looking for and its value separated by ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format.");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		Document d = new Document(command[0], command[1]);
		collection.find(d).forEach(t -> System.out.println(new JSONObject(t.toJson()).toString(4)));
	}

		// TODO: Comprobar que filtre correctamente
	void selectSubject(String f) {
		System.out.println("To search for a subject, write the field you are looking for and its value separated by ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		Document d = new Document(command[0], command[1]);
		Bson proj = Projections.fields(Projections.include(f), Projections.excludeId());
		collection.find(d).projection(proj).forEach(t -> System.out.println(new JSONObject(t.toJson()).toString(4)));
	}
	
	void updateSubject() {
		System.out
				.println("To search for a subject, write the field you are looking for and its value separated by ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		Document existing = new Document();
		existing.put(command[0], command[1]);
		System.out.println("Using the same format, write the field you want to update and its value separated by ':'");
		temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = sc.nextLine();
		}
		command = temp.split(":");
		Document newD = new Document();
		newD.put(command[0], command[1]);
		Document updateQuery = new Document();
		updateQuery.put("$set", newD);
		try {
			collection.updateOne(existing, updateQuery);
		} catch (MongoWriteException e) {
			System.err.println("That subject doesn't exists.");
		}
	}
}
