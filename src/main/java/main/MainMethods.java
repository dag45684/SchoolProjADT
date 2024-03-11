package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;

public class MainMethods {

	public static void initialize() {
		Main.mc = MongoClients.create(new ConnectionString("mongodb://localhost:27017"));
		Main.db = Main.mc.getDatabase("school");
		Main.db.createCollection("students");
		Main.db.createCollection("teachers");
		Main.db.createCollection("subjects");
		Main.th = new TeacherHandler(Main.db.getCollection("teachers"));
		Main.ah = new StudentHandler(Main.db.getCollection("students"));
		Main.sh = new SubjectHandler(Main.db.getCollection("subjects"));
		Main.teachers = Main.db.getCollection("teachers");
		Main.subjects = Main.db.getCollection("subjects");
		Main.students = Main.db.getCollection("students");
	}

	public static void help() {
		System.out.println();
		System.out.println("\t You have the following options:");
		System.out.println();
		System.out.println("\t\t DATA MANIPULATION:");
		System.out.println();
		System.out.println("\t\t\t - To add a new student, type: 'new --a'");
		System.out.println("\t\t\t - To add a new teacher, type: 'new --t'");
		System.out.println("\t\t\t - To add a new subject, type: 'new --s'");
		System.out.println();
		System.out.println("\t\t\t - To update a student, type: 'update --a'");
		System.out.println("\t\t\t - To update a teacher, type: 'update --t'");
		System.out.println("\t\t\t - To update a subject, type: 'update --s'");
		System.out.println();
		System.out.println("\t\t\t - To delete a student, type: 'delete --a'");
		System.out.println("\t\t\t - To delete a teacher, type: 'delete --t'");
		System.out.println("\t\t\t - To delete a subject, type: 'delete --s'");
		System.out.println();
		System.out.println("\t\t\t - To assign students to a teacher, type: 'assign'");
		System.out.println("\t\t\t - To assign subjects to a teacher, type: 'add --st'");
		System.out.println("\t\t\t - To assign subjects to a student, type: 'add --sa'");
		System.out.println();
		System.out.println(
				"\t\t\t - To add multiple students, type: 'new --a --N' where N is the number you want to insert");
		System.out.println(
				"\t\t\t - To add multiple subjects, type: 'new --s --N' where N is the number you want to insert");
		System.out.println();
		System.out.println("\t\t\t ------ ");
		System.out.println();
		System.out.println("\t\t DATA CONSULTING:");
		System.out.println();
		System.out.println("\t\t\t - To consult available fields in each class, type: 'fields'");
		System.out.println();
		System.out.println("\t\t\t - To search by students, type: 'sel --a'");
		System.out.println("\t\t\t - To search by teachers, type: 'sel --t'");
		System.out.println("\t\t\t - To search by subjects, type: 'sel --s'");
		System.out.println();
		System.out.println("\t\t\t - To search for common subjects Teacher-Student, type: 'sel --rel-ts'");
		System.out.println("\t\t\t - To consult department manager roled teachers, type: 'sel --rel-mg'");
		System.out.println();
		System.out.println(
				"\t\t\t - To search specific fields of a student, type: 'sp --a --F' where F is the field you want.");
		System.out.println(
				"\t\t\t - To search specific fields of a teacher, type: 'sp --t --F' where F is the field you want.");
		System.out.println(
				"\t\t\t - To search specific fields of a subject, type: 'sp --s --F' where F is the field you want.");
		System.out.println();
		System.out.println("\t\t\t ------ ");
		System.out.println();
	}

	public static void showFields() {
		System.out.println("TEACHERS:");
		System.out.println("\t - _id");
		System.out.println("\t - name");
		System.out.println("\t - surname");
		System.out.println("\t - age");
		System.out.println("\t - subjects");
		System.out.println();
		System.out.println("TEACHERS:");
		System.out.println("\t - _id");
		System.out.println("\t - name");
		System.out.println("\t - surname");
		System.out.println("\t - age");
		System.out.println("\t - salary");
		System.out.println("\t - deptMgr");
		System.out.println("\t - students");
		System.out.println("\t - subjects");
		System.out.println();
		System.out.println("TEACHERS:");
		System.out.println("\t - _id");
		System.out.println("\t - name");
		System.out.println("\t - hours");
		System.out.println();
	}

	// TODO: Comprobar que funcione
	public static void insertSubjectIntoTeacher() {
		System.out
				.println("To search for a teacher, write the field you are looking for and its value separated by ':'");
		String temp = Main.scanner.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = Main.scanner.nextLine();
		}
		String[] command = temp.split(":");
		Document filtro = new Document(command[0], command[1]);

		System.out.println("Introduce the subject ids you want to add, separated by ','");
		temp = Main.scanner.nextLine();
		while (!temp.matches("^(SB\\d{3})(,(SB\\d{3}))*$")) {
			System.err.println("Wrong format");
			temp = Main.scanner.nextLine();
		}

		ArrayList<String> subs = new ArrayList<>();
		if (temp.contains(",")) {
			for (String s : temp.split(",")) {
				subs.add(s);
			}
		} else {
			subs.add(temp);
		}

		ArrayList<String> rm = new ArrayList<>();
		subs.forEach(t -> Main.teachers.find(new Document("asignaturas", t)).forEach(e -> rm.add(t)));
		subs.removeAll(rm);
		subs.forEach(e -> Main.subjects.find(new Document("_id", e))
				.forEach(t -> Main.teachers.updateOne(filtro, new Document("$push", new Document("subjects", e)))));
	}

	// TODO: Comprobar que funcione
	public static void insertSubjectIntoStudent() {
		System.out
				.println("To search for a student, write the field you are looking for and its value separated by ':'");
		String temp = Main.scanner.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = Main.scanner.nextLine();
		}
		String[] command = temp.split(":");
		Document filtro = new Document(command[0], command[1]);

		System.out.println("Introduce the subject ids you want to add, separated by ','");
		temp = Main.scanner.nextLine();
		while (!temp.matches("^(SB\\d{3})(,(SB\\d{3}))*$")) {
			System.err.println("Wrong format");
			temp = Main.scanner.nextLine();
		}

		ArrayList<String> subs = new ArrayList<>();
		if (temp.contains(",")) {
			for (String s : temp.split(",")) {
				subs.add(s);
			}
		} else {
			subs.add(temp);
		}

		ArrayList<String> rm = new ArrayList<>();
		subs.forEach(t -> Main.students.find(new Document("asignaturas", t)).forEach(e -> rm.add(t)));
		subs.removeAll(rm);
		subs.forEach(e -> Main.subjects.find(new Document("_id", e))
				.forEach(t -> Main.students.updateOne(filtro, new Document("$push", new Document("subjects", e)))));
	}

	// TODO: Comprobar que funcione
	public static void assignStudent() {
		System.out
				.println("To search for a teacher, write the field you are looking for and its value separated by ':'");
		String temp = Main.scanner.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = Main.scanner.nextLine();
		}
		String[] command = temp.split(":");
		Document filtro = new Document(command[0], command[1]);
		System.out.println("Introduce the student ids you want to add to the teacher, separated by ','");
		temp = Main.scanner.nextLine();
		// TODO: Comprobar que funcione
		while (!temp.matches("^(ST\\d{3})(,(ST\\d{3}))*$")) {
			System.err.println("Wrong format");
			temp = Main.scanner.nextLine();
		}

		ArrayList<String> subs = new ArrayList<>();
		if (temp.contains(",")) {
			for (String s : temp.split(",")) {
				subs.add(s);
			}
		} else {
			subs.add(temp);
		}

		ArrayList<String> rm = new ArrayList<>();
		subs.forEach(t -> Main.teachers.find(new Document("asignaturas", t)).forEach(e -> rm.add(t)));
		subs.removeAll(rm);
		subs.forEach(e -> Main.students.find(new Document("_id", e))
				.forEach(t -> Main.teachers.updateOne(filtro, new Document("$push", new Document("students", e)))));
	}

	@SuppressWarnings("unlikely-arg-type")
	public static void findCommonSubjects() {
		System.out
				.println("To search for a teacher, write the field you are looking for and its value separated by ':'");
		String temp = Main.scanner.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = Main.scanner.nextLine();
		}
		String[] command = temp.split(":");
		Document tea = new Document(command[0], command[1]);
		System.out
				.println("To search for a student, write the field you are looking for and its value separated by ':'");
		temp = Main.scanner.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = Main.scanner.nextLine();
		}
		command = temp.split(":");
		Document stu = new Document(command[0], command[1]);

		// TODO: Comprobar que funcione
		Bson proj = Projections.fields(Projections.include("subjects"), Projections.excludeId());
		ArrayList<String> sub = new ArrayList<>();
		Main.th.collection.find(tea).projection(proj)
				.forEach(e -> Arrays.asList(e.get("subjects")).forEach(t -> sub.add((String) t)));
		Main.ah.collection.find(stu).projection(proj).forEach(e -> {
			if (sub.contains(e))
				System.out.println(e);
		});
	}

	public static void select(String collectionName) {
		MongoCollection<Document> collection = getCollection(collectionName);
		System.out.println("To search for " + collectionName
				+ ", write the field you are looking for and its value separated by ':' or 'all' to show the entire collection.");
		String temp = Main.scanner.nextLine();
		if (temp.equals("all")) {
			collection.find().forEach(t -> System.out.println(new JSONObject(t.toJson()).toString(4)));
		} else {
			while (!temp.matches(".\\w+:\\w+")) {
				System.err.println("Wrong format");
				temp = Main.scanner.nextLine();
			}
			String[] command = temp.split(":");
			int aux = 0;
			boolean exception = false;
			Document d = new Document();
			try {
				aux = Integer.parseInt(command[1]);
			} catch (NumberFormatException e) {
				exception = true;
				d.put(command[0], command[1]);
			}
			if (!exception)
				d.put(command[0], aux);
			if (collection.countDocuments(d) == 0)
				System.err.println(collectionName + " with " + temp + " does not exists.");
			collection.find(d).forEach(t -> System.out.println(new JSONObject(t.toJson()).toString(4)));
		}
	}

	private static MongoCollection<Document> getCollection(String collectionName) {
		switch (collectionName) {
		case "students":
			return Main.students;
		case "teachers":
			return Main.teachers;
		default:
			return Main.subjects;
		}
	}

	public static void delete(String collectionName) {
		MongoCollection<Document> collection = getCollection(collectionName);
		System.out.println("To search for " + collectionName
				+ ", write the field you are looking for and its value separated by ':'");
		String temp = Main.scanner.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = Main.scanner.nextLine();
		}
		String[] command = temp.split(":");
		Document query = new Document();
		int aux = 0;
		boolean exception = false;
		try {
			aux = Integer.parseInt(command[1]);
		} catch (NumberFormatException e) {
			exception = true;
			query.put(command[0], command[1]);
		}
		if (!exception)
			query.put(command[0], aux);
		if (collection.countDocuments(query) == 0) {
			System.err.println(collectionName + " with " + temp + " does not exist.");
		} else {
			collection.deleteOne(query);
		}
	}

	public static void update(String collectionName) {
		MongoCollection<Document> collection = getCollection(collectionName);
		System.out.println("To search for " + collectionName
				+ ", write the field you are looking for and its value separated by ':'");
		String temp = Main.scanner.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = Main.scanner.nextLine();
		}
		String[] command = temp.split(":");

		// We search for the document, if it doesnt exist, ends.
		Document existing = new Document();
		int aux = 0;
		boolean exception = false;

		// This is because if the field is a number we cannot use String
		try {
			aux = Integer.parseInt(command[1]);
		} catch (NumberFormatException e) {
			exception = true;
			existing.put(command[0], command[1]);
		}
		if (!exception)
			existing.put(command[0], aux);
		if (collection.countDocuments(existing) == 0) {
			System.err.println(collectionName + " with " + temp + " does not exist.");
		} else {
			System.out.println(
					"Using the same format, write the field you want to update and its value separated by ':'");
			temp = Main.scanner.nextLine();
			while (!temp.matches(".\\w+:\\w+")) {
				System.err.println("Wrong format");
				temp = Main.scanner.nextLine();
			}
			command = temp.split(":");
			aux = 0;
			exception = false;
			try {
				aux = Integer.parseInt(command[1]);
			} catch (NumberFormatException e) {
				exception = true;
				Document newD = new Document();
				newD.put(command[0], command[1]);
				Document updateQuery = new Document();
				updateQuery.put("$set", newD);
				collection.updateOne(existing, updateQuery);

			}
			if (!exception) {
				Document newD = new Document();
				newD.put(command[0], aux);
				Document updateQuery = new Document();
				updateQuery.put("$set", newD);
				collection.updateOne(existing, updateQuery);

			}
		}
	}
}
