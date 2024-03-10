package main;

import java.util.Scanner;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

public class TeacherHandler {
	MongoCollection<Document> collection;
	Scanner sc = new Scanner(System.in);

	public TeacherHandler(MongoCollection<Document> collection) {
		this.collection = collection;

	}

	void insertTeacher() {
		System.out.println("Insert teacher id:");
		String id = sc.nextLine();
		while (!id.matches("TE\\d{3}")) {
			System.err.println("Not valid ID, format should be: TE000");
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
			System.err.println("A teacher with id:" + id + " already exists.");
		}
	}

	void insertTeacher(int n) {
//		List<Document> teachers = new ArrayList<Document>();
//		for (int i = 0; i < n; i++) {
//			System.out.println("Insert teacher id:");
//			String id = sc.nextLine();
//			while (!id.matches("TE\\d{3}")) {
//				System.err.println("Not valid ID, format should be: TE000");
//				id = sc.nextLine();
//			}
//			System.out.println("Insert teacher name:");
//			String name = sc.nextLine();
//			System.out.println("Insert teacher surname:");
//			String surname = sc.nextLine();
//			System.out.println("Insert teacher age:");
//			int age = sc.nextInt();
//			System.out.println("Insert teacher salary:");
//			float salary = sc.nextFloat();
//			System.out.println("Insert teacher deptMgr (boolean):");
//			boolean deptMgr = sc.nextBoolean();
//			Teacher st = new Teacher(id, name, surname, age, salary, deptMgr);
//			teachers.add(st.createDocument());
//		}
//		collection.insertMany(teachers);
		
		// Same as StudentHandler, error catching > efficiency.
		for (int i = 0; i < n; i++) {
			insertTeacher();
		}

	}

	void selectTeacher() {
		System.out.println("To search for a teacher, write the field you are looking for and its value separated by ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		Document d = new Document(command[0], command[1]);
		collection.find(d).forEach(t -> System.out.println(new JSONObject(t.toJson()).toString(4)));
	}

		// TODO: Comprobar que filtre correctamente
	void selectTeacher(String f) {
		System.out.println("To search for a teacher, write the field you are looking for and its value separated by ':'");
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
	
		// TODO: Comprobar que funcione
	void selectManagers() {
		Bson f = Filters.eq("deptMgr", true);
		collection.find(f).forEach(t -> System.out.println(new JSONObject(t.toJson()).toString(4)));
	}
	
	void updateTeacher() {
		System.out
				.println("To search for a teacher, write the field you are looking for and its value separated by ':'");
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
			System.err.println("That teacher doesn't exists.");
		}
	}
}

