package main;

import java.util.Scanner;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;

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
			System.err.println("Not valid ID, format should be: ST000");
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
		} catch (MongoWriteException e) {
			System.err.println("A student with id:" + id + " already exists.");
		}
	}

	void insertStudent(int n) {
		// We thought about using a loop with the non parameters insertStudent
		// but after all is way more efficient to use one insertMany
		// instead of multiple insertOne.
//		List<Document> students = new ArrayList<Document>();
//		for (int i = 0; i < n; i++) {
//			System.out.println("Insert student id:");
//			String id = sc.nextLine();
//			while (!id.matches("ST\\n{3}")) {
//				System.err.println("Not valid ID, format should be: ST000");
//				id = sc.nextLine();
//			}
//			System.out.println("Insert students name:");
//			String name = sc.nextLine();
//			System.out.println("Insert students surname:");
//			String surname = sc.nextLine();
//			System.out.println("Insert students age:");
//			int age = sc.nextInt();
//			Student st = new Student(id, name, surname, age);
//			students.add(st.createDocument());
//		}
//		collection.insertMany(students);

		// After a few tries, we decided to use this, sacrificing efficiency just to
		// catch the error if one id already exists in the collection.
		for (int i = 0; i < n; i++) {
			insertStudent();
		}
	}

	// TODO: Comprobar que filtre correctamente
	void selectStudent(String f) {
		System.out
				.println("To search for a student, write the field you are looking for and its value separated by ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		int aux = 0;
		try {
			aux = Integer.parseInt(command[1]);
		} catch (NumberFormatException e) {
			Document d = new Document(command[0], command[1]);
			Bson proj = Projections.fields(Projections.include(f), Projections.excludeId());
			collection.find(d).projection(proj)
					.forEach(t -> System.out.println(new JSONObject(t.toJson()).toString(4)));
		}
		System.err.println(aux);
		Document d = new Document(command[0], aux);
		Bson proj = Projections.fields(Projections.include(f), Projections.excludeId());
		System.out.println(collection.countDocuments(d));
		collection.find(d).projection(proj).forEach(t -> System.out.println(new JSONObject(t.toJson()).toString(4)));
	}
}
