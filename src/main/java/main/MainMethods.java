package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
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
		System.out.println("\t\t\t - To assign students to a teacher, type: 'assign'");
		System.out.println("\t\t\t - To assign subjects to a teacher, type: 'add --st'");
		System.out.println("\t\t\t - To assign subjects to a student, type: 'add --sa'");
		System.out.println();
		System.out.println("\t\t\t - To add multiple students, type: 'new --a --N' where N is the number you want to insert");
		System.out.println("\t\t\t - To add multiple subjects, type: 'new --s --N' where N is the number you want to insert");
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
		System.out.println("\t\t\t - To search specific fields of a student, type: 'sp --a --F' where F is the field you want.");
		System.out.println("\t\t\t - To search specific fields of a teacher, type: 'sp --t --F' where F is the field you want.");
		System.out.println("\t\t\t - To search specific fields of a subject, type: 'sp --s --F' where F is the field you want.");
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
		Scanner sc = new Scanner(System.in);
		System.out.println("To search for a teacher, write the field you are looking for and its value separated by ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		Document filtro = new Document(command[0], command[1]);

		System.out.println("Introduce the subject ids you want to add, separated by ','");
		temp = sc.nextLine();
		while (!temp.matches("^(SB\\d{3})(,(SB\\d{3}))*$")) {
			System.err.println("Wrong format");
			temp = sc.nextLine();
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
		subs.forEach(e -> Main.subjects.find(new Document("_id", e)).forEach(t -> Main.teachers.updateOne(filtro, new Document("$push", new Document("subjects", e)))));
		sc.close();
	}
	
		// TODO: Comprobar que funcione
	public static void insertSubjectIntoStudent() {
		Scanner sc = new Scanner(System.in);
		System.out.println("To search for a student, write the field you are looking for and its value separated by ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		Document filtro = new Document(command[0], command[1]);

		System.out.println("Introduce the subject ids you want to add, separated by ','");
		temp = sc.nextLine();
		// TODO: Comprobar que funcione
		while (!temp.matches("^(SB\\d{3})(,(SB\\d{3}))*$")) {
			System.err.println("Wrong format");
			temp = sc.nextLine();
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
		subs.forEach(e -> Main.subjects.find(new Document("_id", e)).forEach(t -> Main.students.updateOne(filtro, new Document("$push", new Document("subjects", e)))));
		sc.close();
	}
	
		// TODO: Comprobar que funcione
	public static void assignStudent() {
		Scanner sc = new Scanner(System.in);
		System.out.println("To search for a teacher, write the field you are looking for and its value separated by ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		Document filtro = new Document(command[0], command[1]);
		System.out.println("Introduce the student ids you want to add to the teacher, separated by ','");
		temp = sc.nextLine();
		// TODO: Comprobar que funcione
		while (!temp.matches("^(ST\\d{3})(,(ST\\d{3}))*$")) {
			System.err.println("Wrong format");
			temp = sc.nextLine();
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
		subs.forEach(e -> Main.students.find(new Document("_id", e)).forEach(t -> Main.teachers.updateOne(filtro, new Document("$push", new Document("students", e)))));
		sc.close();
	}
	
	public static void findCommonSubjects() {
		Scanner sc = new Scanner(System.in);
		System.out.println("To search for a teacher, write the field you are looking for and its value separated by ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		Document tea = new Document(command[0], command[1]);
		System.out.println("To search for a student, write the field you are looking for and its value separated by ':'");
		temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("Wrong format");
			temp = sc.nextLine();
		}
		command = temp.split(":");
		Document stu = new Document(command[0], command[1]);

		// TODO: Comprobar que funcione
		Bson proj = Projections.fields(Projections.include("subjects"), Projections.excludeId());
		ArrayList<String> sub = new ArrayList<>();
		Main.th.collection.find(tea).projection(proj).forEach(e -> Arrays.asList(e.get("subjects")).forEach(t -> sub.add((String) t)));
		Main.ah.collection.find(stu).projection(proj).forEach(e -> { if(sub.contains(e)) System.out.println(e); });
		sc.close();
	}

}
