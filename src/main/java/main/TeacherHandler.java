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
		System.out.println("Insert teacher id (TE000):");
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

	// TODO: Comprobar que filtre correctamente
	void selectTeacher(String f) {
		System.out
				.println("To search for a teacher, write the field you are looking for and its value separated by ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		int aux = 0;
		boolean exception = false;
		try {
			aux = Integer.parseInt(command[1]);
		} catch (NumberFormatException e) {
			exception = true;
			Document d = new Document(command[0], command[1]);
			Bson proj = Projections.fields(Projections.include(f), Projections.excludeId());
			if (collection.countDocuments(d) == 0)
				System.err.println("A teacher with " + temp + " does not exists.");
			else
				collection.find(d).projection(proj)
						.forEach(t -> System.out.println(new JSONObject(t.toJson()).toString(4)));
		}
		if (!exception) {
			Document d = new Document(command[0], aux);
			Bson proj = Projections.fields(Projections.include(f), Projections.excludeId());
			if (collection.countDocuments(d) == 0)
				System.err.println("A teacher with " + temp + " does not exists.");
			else
				collection.find(d).projection(proj)
						.forEach(t -> System.out.println(new JSONObject(t.toJson()).toString(4)));
		}
	}

	// TODO: Comprobar que funcione
	void selectManagers() {
		Bson f = Filters.eq("deptMgr", true);
		collection.find(f).forEach(t -> System.out.println(new JSONObject(t.toJson()).toString(4)));
	}

}
