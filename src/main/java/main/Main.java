package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Main {

	static MongoClient mc;
	static MongoDatabase db;
	static TeacherHandler th;
	static SubjectHandler sh;
	static StudentHandler ah;
	static MongoCollection<Document> teachers;
	static MongoCollection<Document> students;
	static MongoCollection<Document> subjects;

	public static void main(String[] args) {

		initialize();
		boolean out = false;
		String command;
		Scanner scanner = new Scanner(System.in);

		while (!out) {
			System.out.println("SCHOOL PERSONNEL AND STUDENT MANAGEMENT:");
			System.out.println("Escriba 'help' para mostrar la ayuda o 'exit' para cerrar la aplicacion.");
			System.out.print(">");
			command = scanner.nextLine();

			switch (command.substring(0, command.indexOf(' ') == -1 ? command.length() : command.indexOf(' '))) {
			case "help":
				help();
				break;
			case "sel":
				if (command.contains("--a")) {
					ah.selectStudent();
					break;
				}
				if (command.contains("--t")) {
					th.selectTeacher();
					break;
				}
				if (command.contains("--s")) {
					sh.selectSubject();
					break;
				}
				break;
			case "new":
				if (command.contains("--a")) {
					if (command.contains("--\\d+")) {
						command = command.replaceAll("\\D+", "");
						ah.insertStudent(Integer.parseInt(command));
						break;
					}
					ah.insertStudent();
					break;
				}
				if (command.contains("--t")) {
					if (command.contains("--\\d+")) {
						command = command.replaceAll("\\D+", "");
						th.insertTeacher(Integer.parseInt(command));
						break;
					}
					th.insertTeacher();
					break;
				}
				if (command.contains("--s")) {
					if (command.contains("--\\d+")) {
						command = command.replaceAll("\\D+", "");
						sh.insertSubject(Integer.parseInt(command));
						break;
					}
					sh.insertSubject();
					break;
				}
				break;
			case "assign":
				assignStudent();
				break;
			case "add":
				if (command.contains("--st")) {
					insertSubjectIntoTeacher();
					break;
				}
				if (command.contains("--sa")) {
					insertSubjectIntoStudent();
					break;
				}
				break;
			case "exit":
				out = true;
				break;
			default:
				System.out.println("Ese comando no se reconoce, intentelo de nuevo. Si necesita ayuda, escriba help para mostrarla\n");

			}

		}
//		 TeachersHandler th = new TeachersHandler(db.getCollection("teachers"));
//		 th.insertTeacher();
//		 th.selectTeacher();

//		 
//		 SubjectHandler sh = new SubjectHandler(db.getCollection("subjects"));
//		 sh.insertSubject();

//		 sh.selectSubject();

	}

	public static void help() {
		System.out.println();
		System.out.println("\t You have the following options:");
		System.out.println();
		System.out.println("\t\t DATA MANIPULATION:");
		System.out.println("\t\t\t - To add a new student, type: 'new --a'");
		System.out.println("\t\t\t - To add a new teacher, type: 'new --t'");
		System.out.println("\t\t\t - To add a new subject, type: 'new --s'");
		System.out.println("\t\t\t - To add multiple students, type: 'new --a --N' where N is the number you want to insert");
		System.out.println("\t\t\t - To add multiple subjects, type: 'new --s --N' where N is the number you want to insert");
		System.out.println("\t\t\t - To assign students to a teacher, type: 'assign'");
		System.out.println("\t\t\t - To assign subjects to a teacher, type: 'add --st'");
		System.out.println("\t\t\t - To assign subjects to a student, type: 'add --sa'");
		System.out.println();
		System.out.println("\t\t DATA CONSULTING:");
		System.out.println("\t\t\t - To search by students, type: 'sel --a'");
		System.out.println("\t\t\t - To search by teachers, type: 'sel --t'");
		System.out.println("\t\t\t - To search by subjects, type: 'sel --s'");
		System.out.println();
	}

	public static void initialize() {
		mc = MongoClients.create(new ConnectionString("mongodb://localhost:27017"));
		db = mc.getDatabase("school");
		db.createCollection("students");
		db.createCollection("teachers");
		db.createCollection("subjects");
		th = new TeacherHandler(db.getCollection("teachers"));
		ah = new StudentHandler(db.getCollection("students"));
		sh = new SubjectHandler(db.getCollection("subjects"));
		teachers = db.getCollection("teachers");
		subjects = db.getCollection("subjects");
		students = db.getCollection("students");
	}

	public static void insertSubjectIntoTeacher() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Para buscar un profesor, escribe el campo sobre el que buscas y el valor del campo separados por ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("El formato no es el especificado");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		Document filtro = new Document(command[0], command[1]);

		System.out.println("Introduce el id de las asignaturas que quieras añadir, separadas por ','");
		temp = sc.nextLine();
		// TODO: Comprobar que funcione
		while (!temp.matches("^(SB\\d{3})(,(SB\\d{3}))*$")) {
			System.err.println("El formato no es el especificado");
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
		subs.forEach(t -> teachers.find(new Document("asignaturas", t)).forEach(e -> rm.add(t)));
		subs.removeAll(rm);
		subs.forEach(e -> subjects.find(new Document("_id", e)).forEach(t -> teachers.updateOne(filtro, new Document("$push", new Document("asignaturas", e)))));
		sc.close();
	}
	
	public static void insertSubjectIntoStudent() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Para buscar un estudiante, escribe el campo sobre el que buscas y el valor del campo separados por ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("El formato no es el especificado");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		Document filtro = new Document(command[0], command[1]);

		System.out.println("Introduce el id de las asignaturas que quieras añadir, separadas por ','");
		temp = sc.nextLine();
		// TODO: Comprobar que funcione
		while (!temp.matches("^(SB\\d{3})(,(SB\\d{3}))*$")) {
			System.err.println("El formato no es el especificado");
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
		subs.forEach(t -> students.find(new Document("asignaturas", t)).forEach(e -> rm.add(t)));
		subs.removeAll(rm);
		subs.forEach(e -> subjects.find(new Document("_id", e)).forEach(t -> students.updateOne(filtro, new Document("$push", new Document("asignaturas", e)))));
		sc.close();
	}
	
	public static void assignStudent() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Para buscar un profesor, escribe el campo sobre el que buscas y el valor del campo separados por ':'");
		String temp = sc.nextLine();
		while (!temp.matches(".\\w+:\\w+")) {
			System.err.println("El formato no es el especificado");
			temp = sc.nextLine();
		}
		String[] command = temp.split(":");
		Document filtro = new Document(command[0], command[1]);

		System.out.println("Introduce el id de los alumnos que quieras asignarle al profesor, separadas por ','");
		temp = sc.nextLine();
		// TODO: Comprobar que funcione
		while (!temp.matches("^(ST\\d{3})(,(ST\\d{3}))*$")) {
			System.err.println("El formato no es el especificado");
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
		subs.forEach(t -> teachers.find(new Document("asignaturas", t)).forEach(e -> rm.add(t)));
		subs.removeAll(rm);
		subs.forEach(e -> students.find(new Document("_id", e)).forEach(t -> teachers.updateOne(filtro, new Document("$push", new Document("alumnos", e)))));
		sc.close();
	}
}
